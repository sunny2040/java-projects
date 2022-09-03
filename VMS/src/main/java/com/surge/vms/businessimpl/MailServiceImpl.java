package com.surge.vms.businessimpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.surge.vms.business.MailService;
import com.surge.vms.model.MailRequest;
import com.surge.vms.model.MailResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender sender;

	// Free marker config
	@Autowired
	private Configuration config;

	public MailResponse sendApprovedMail(MailRequest request,
			Map<String, Object> model) {

		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			// add attachment
			//helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

			Template template = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils
					.processTemplateIntoString(template, model);

			helper.setTo(request.getTo());
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getFrom());
			sender.send(message);

			response.setMessage("Email send to : " + request.getTo());
			response.setStatus(Boolean.TRUE);
		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Email sending failure ..." + e.getMessage());
			response.setStatus(Boolean.FALSE);

		}
		return response;
	}

	public MailResponse sendRejectedMail(MailRequest request,
			Map<String, Object> model) {

		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			// add attachment
			//helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

			Template template = config.getTemplate("email-template-reject.ftl");
			String html = FreeMarkerTemplateUtils
					.processTemplateIntoString(template, model);

			helper.setTo(request.getTo());
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getFrom());
			sender.send(message);

			response.setMessage("Email send to : " + request.getTo());
			response.setStatus(Boolean.TRUE);
		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Email sending failure ..." + e.getMessage());
			response.setStatus(Boolean.FALSE);

		}
		return response;
	}
}
