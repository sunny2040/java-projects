package com.surge.vms.business;

import java.util.Map;

import com.surge.vms.model.MailRequest;
import com.surge.vms.model.MailResponse;

public interface MailService {
	MailResponse sendApprovedMail(MailRequest request,
			Map<String, Object> model);

	MailResponse sendRejectedMail(MailRequest request,
			Map<String, Object> model);
}
