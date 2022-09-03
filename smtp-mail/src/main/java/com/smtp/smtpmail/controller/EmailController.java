package com.smtp.smtpmail.controller;

//Importing required classes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smtp.smtpmail.model.EmailDetails;
import com.smtp.smtpmail.service.EmailService;

@RestController

public class EmailController {

	@Autowired
	private EmailService emailService;

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/sendMail")
	public String sendMail(@RequestBody EmailDetails details) {
		String status = emailService.sendSimpleMail(details);

		return status;
	}

	// Sending email with attachment
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}
}
