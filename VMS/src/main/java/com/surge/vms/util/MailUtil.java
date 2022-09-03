package com.surge.vms.util;

import java.util.Base64;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;

@Component
public class MailUtil {

	@Autowired
	TemplateEngine htmlTemplateEngine;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${activationKeyDelimiter}")
	private String activationKeyDelimiter;

	public void sendVendorCompanyApprovalEMail(Vendor vendor) {

		if (vendor != null && vendor.getEmail().length() > 0 && vendor.getProcessInstanceId().length() > 0) {

			String vendorRepEmail = vendor.getEmail();
			String pId = vendor.getProcessInstanceId();
			String activationKey = pId + activationKeyDelimiter + vendorRepEmail;
			
			
			String activationKeyEncodedString = Base64.getEncoder().encodeToString(activationKey.getBytes());

			// setting context of mail
			Context context = new Context();
			context.setVariable("name", vendor.getFirstName() + vendor.getLastName());
			context.setVariable("link","http://localhost:8080/vp-sign-up.html?activationToken=" + activationKeyEncodedString);
			context.setVariable("linkMsg", "Complete the approval process by clicking this link");
			context.setVariable("emailType", "Approve Email");

			String body = htmlTemplateEngine.process("email-template-htmlformat2", context);

			// Send the verification email
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper;
			try {
				helper = new MimeMessageHelper(message, true);
				helper.setTo(vendorRepEmail);
				helper.setSubject("Registration Approval  Notice");
				helper.setText(body, true);
			} catch (MessagingException e) {
				e.printStackTrace();//notify vms about the mail sending failure
			}

			javaMailSender.send(message);
			System.out.println("Registration Approval  Notice Mail Sent Successfully..............");

		} else {

		}

	}
	
	public void sendVendorCompanyClarificationEMail(Vendor vendor) {
		

		if (vendor != null && vendor.getEmail().length() > 0 && vendor.getProcessInstanceId().length() > 0) {

			String vendorRepEmail = vendor.getEmail();
			String pId = vendor.getProcessInstanceId();
			String activationKey = pId + activationKeyDelimiter + vendorRepEmail;
			
			
			String activationKeyEncodedString = Base64.getEncoder().encodeToString(activationKey.getBytes());

			// setting context of mail
			Context context = new Context();
			context.setVariable("name", vendor.getFirstName() + " " +vendor.getLastName());
			context.setVariable("link",
					"http://localhost:8080/vendor-application-status.html?activationToken=" + activationKeyEncodedString);
			
			context.setVariable("linkMsg", "Provide clarification by clicking this link");
			
			context.setVariable("emailType", "Clarification Required Email");

			String body = htmlTemplateEngine.process("email-template-htmlformat2", context);

			// Send the verification email
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper;
			try {
				helper = new MimeMessageHelper(message, true);
				helper.setTo(vendorRepEmail);
				helper.setSubject("Vendor Company Application - Clarification Notice");
				helper.setText(body, true);
			} catch (MessagingException e) {
				e.printStackTrace();//notify vms about the mail sending failure
			}

			javaMailSender.send(message);
			System.out.println("Vendor Company Application - Clarification Notice Mail Sent Successfully..............");

		} else {

		}

	}
	
	public void sendCheckVendorStatusLink(Vendor vendor) {
		

		if (vendor != null && vendor.getEmail().length() > 0 && vendor.getProcessInstanceId().length() > 0) {

			String vendorRepEmail = vendor.getEmail();
			String pId = vendor.getProcessInstanceId();
			String activationKey = pId + activationKeyDelimiter + vendorRepEmail;
			
			
			String activationKeyEncodedString = Base64.getEncoder().encodeToString(activationKey.getBytes());

			// setting context of mail
			Context context = new Context();
			context.setVariable("name", vendor.getFirstName() + " " +vendor.getLastName());
			context.setVariable("link",
					"http://localhost:8080/vendor-application-status.html?activationToken=" + activationKeyEncodedString);
			
			context.setVariable("linkMsg", "Click To Know Your Application Status");
			
			context.setVariable("emailType", "Check Application Status");

			String body = htmlTemplateEngine.process("email-template-htmlformat2", context);

			// Send the verification email
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper;
			try {
				helper = new MimeMessageHelper(message, true);
				helper.setTo(vendorRepEmail);
				helper.setSubject("Vendor Company Application - Check Status");
				helper.setText(body, true);
			} catch (MessagingException e) {
				e.printStackTrace();//notify vms about the mail sending failure
			}

			javaMailSender.send(message);
			System.out.println("Vendor Company Application - Check Status Mail Sent Successfully..............");

		} else {

		}

	}
}
