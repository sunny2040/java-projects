package com.surge.vms.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.surge.vms.business.VendorCompanyService;
import com.surge.vms.business.VendorService;
import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;
import com.surge.vms.process.integrator.CamundaProcessInstanceService;
import com.surge.vms.util.CamundaIntegrator;
import com.surge.vms.util.FileUtil;
import com.surge.vms.util.MailUtil;

@Component
public class VmsScheduler {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Autowired
	private VendorService vendorService;
	@Autowired
	private VendorCompanyService vendorCompanyService;

	@Value("${camunda.processname}")
	private String processName;

	@Autowired
	private CamundaProcessInstanceService camundaProcessInstanceService;

	@Autowired
	private MailUtil mailUtil;

	private String[] hostList = { "http://localhost:8088/rest/engine" };

	private boolean camundaAvalability = false;

	private boolean getCamundaAvailability() {

		camundaAvalability = CamundaIntegrator.getServerStatus(hostList[0]);

		return camundaAvalability;

	}

	@Scheduled(fixedRate = 10000)
	public void startProcessDefnTask() {

		System.out.println("Regular task performed at " + dateFormat.format(new Date()));
		System.out.println("Current Thread : {}" + Thread.currentThread().getName());
		if (getCamundaAvailability()) {
			System.out.println("Camunda is RUNNING...");

			String vendorNewStatus = "InProgress";
			List<Vendor> vendorList = vendorService.getVendorByStatus("ACTIVE");
			List<VendorCompany> vendorCompanyList = vendorCompanyService.getAllVendorCompanyByStatus("NEW");
			if (null != vendorCompanyList && vendorCompanyList.size() > 0) {

				for (VendorCompany vendorCompany : vendorCompanyList) {

					Vendor vendor = vendorCompany.getVendor().stream().findFirst().get();

					ProcessInstanceDto processInstanceDto = camundaProcessInstanceService
							.startProcessDefinitions(processName, vendorCompany.getCompanyName());

					camundaProcessInstanceService.postVendorDataToWorkflow(processInstanceDto.getId(), vendor,
							vendorCompany);

					vendorService.updateVendorPID(vendor.getId(), processInstanceDto.getId(),
							processInstanceDto.getBusinessKey(), vendorNewStatus);
					vendor.setProcessInstanceId(processInstanceDto.getId());
					mailUtil.sendCheckVendorStatusLink(vendor);
				}

			} else {
				System.out.println("No Vendor in NEW Status");
			}

		} else {
			System.out.println("Camunda is DOWN...");
		}
	}

	@Scheduled(fixedRate = 10000)
	public void sendVendorReply() {

		System.out.println("sendVendorReply Called...");

		System.out.println("Regular task performed at " + dateFormat.format(new Date()));
		System.out.println("Current Thread : {}" + Thread.currentThread().getName());

		if (getCamundaAvailability()) {
			System.out.println("Camunda is RUNNING...");

			List<VendorCompany> vendorCompanyList = vendorCompanyService.getAllVendorCompanyByStatus("REPLIED");

			if (null != vendorCompanyList && vendorCompanyList.size() > 0) {

				for (VendorCompany vendorCompany : vendorCompanyList) {

					Vendor vendor = vendorCompany.getVendor().stream().findFirst().get();

					int respStatusCodeMsgListener = camundaProcessInstanceService
							.postMessageListenerTriggee("ReceivedClarification", vendor.getBussinessKey());

					int respStatusCode = camundaProcessInstanceService
							.postCamundaVariable(vendor.getProcessInstanceId(), "vendorReply", vendor.getVendorReply());

					respStatusCode = camundaProcessInstanceService.postCamundaVariable(vendor.getProcessInstanceId(),
							"approverQuery", vendor.getApproverQuery());
					if (respStatusCode == 204) {

						System.out.println("success");
						vendorService.updateVendorStatus(vendor.getId(), vendor.getProcessInstanceId(), "INPROGRESS");
					}

				}

			} else {
				System.out.println("No Vendor in REPLIED Status");
			}

		} else {
			System.out.println("Camunda is DOWN...");
		}

	}

	@Scheduled(fixedRate = 10000)
	public void sendVendorCompanyApprovedNotification() {

		System.out.println("sendVendorCompanyApprovedNotification Called...");

		System.out.println("Regular task performed at " + dateFormat.format(new Date()));
		System.out.println("Current Thread : {}" + Thread.currentThread().getName());

		List<VendorCompany> vendorCompanyList = vendorCompanyService.getAllVendorCompanyByStatus("APPROVED");

		if (null != vendorCompanyList && vendorCompanyList.size() > 0) {

			for (VendorCompany vendorCompany : vendorCompanyList) {

				Vendor vendor = vendorCompany.getVendor().stream().findFirst().get();

				mailUtil.sendVendorCompanyApprovalEMail(vendor);

			}

		} else {
			System.out.println("No Vendor in APPROVED Status");
		}

	}

	@Scheduled(fixedRate = 10000)
	public void sendVendorCompanyPendingQueryNotification() {

		System.out.println("sendVendorCompanyPendingQueryNotification Called...");

		System.out.println("Regular task performed at " + dateFormat.format(new Date()));
		System.out.println("Current Thread : {}" + Thread.currentThread().getName());

		List<VendorCompany> vendorCompanyList = vendorCompanyService.getAllVendorCompanyByStatus("PENDING");

		if (null != vendorCompanyList && vendorCompanyList.size() > 0) {

			for (VendorCompany vendorCompany : vendorCompanyList) {
				Vendor vendor = vendorCompany.getVendor().stream().findFirst().get();
				mailUtil.sendVendorCompanyClarificationEMail(vendor);

			}

		} else {
			System.out.println("No Vendor in PENDING Status");
		}

	}

	@Scheduled(fixedRate = 15000)
	public void sendVendorCompanyApplicationStatusEmail() {

		System.out.println("sendVendorCompanyApplicationStatusEmail Called...");

		System.out.println("Regular task performed at " + dateFormat.format(new Date()));
		System.out.println("Current Thread : {}" + Thread.currentThread().getName());

		List<VendorCompany> vendorCompanyList = vendorCompanyService.getAllVendorCompanyByStatus("NEW");

		if (null != vendorCompanyList && vendorCompanyList.size() > 0) {

			for (VendorCompany vendorCompany : vendorCompanyList) {
				Vendor vendor = vendorCompany.getVendor().stream().findFirst().get();
				if (null != vendor) {
					mailUtil.sendCheckVendorStatusLink(vendor);
				}

			}

		} else {
			System.out.println("No Vendor in NEW Status");
		}

	}

//
//	@Scheduled(initialDelay = 1000, fixedRate = 5000)
//	public void performDelayedTask() {
//
//		System.out.println("Delayed Regular task performed at "
//				+ dateFormat.format(new Date()));
//
//	}
//
//	@Scheduled(cron = "*/5 * * * * *")
//	public void performTaskUsingCron() {
//
//		System.out.println("Regular task performed using Cron at "
//				+ dateFormat.format(new Date()));
//
//	}
}