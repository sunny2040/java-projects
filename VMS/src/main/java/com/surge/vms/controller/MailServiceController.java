package com.surge.vms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.surge.vms.business.MailService;
import com.surge.vms.model.MailRequest;
import com.surge.vms.model.MailResponse;
import com.surge.vms.util.FileUtil;
import com.surge.vms.util.MailUtil;

/**
 * @author ${user}
 * @desc This is Mail Controller. Provide all the mapping url for mail service
 */
@RestController
public class MailServiceController {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private MailUtil mailUtil;

	@PostMapping("/sendApprovedEmail")
	public MailResponse sendApprovedMail(@RequestBody MailRequest request) {
		Map<String, Object> model = new HashMap<>();
		model.put("name", request.getName());
		model.put("location", "Houston,TX");
		System.out.println("========= Approved Email sent =========");
		return mailService.sendApprovedMail(request, model);
	}

	@PostMapping("/sendRejectedEmail")
	public MailResponse sendRejectedMail(@RequestBody MailRequest request) {
		Map<String, Object> model = new HashMap<>();
		model.put("name", request.getName());
		model.put("location", "Houston,TX");
		System.out.println("========= Rejected Email sent =========");
		return mailService.sendRejectedMail(request, model);
	}

	@PostMapping("/sendEmail")
	void sendEmailTBD() {
		/*try {
			mailUtil.sendHtmlMail("muthumur@gmail.com");
		} catch (MessagingException e) {

			e.printStackTrace();
		}*/
	}

}
