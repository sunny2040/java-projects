package com.surge.vms.util;

import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncodeUtil {

	// Encode Base64 Util
	public String encode64(String token) {
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encoded = encoder.encode(token.getBytes());
		return new String(encoded);
	}

	// Decode Base64 Util
	public String decode64(String token) {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] decoded = decoder.decode(token);
		return new String(decoded);
	}

	// Encode Bcrypt Password (Spring Security)
	@Bean
	public PasswordEncoder getPasswordEncode() {
		return new BCryptPasswordEncoder(14);
	}
}
