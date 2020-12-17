package com.mouensis.server.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhuyuan
 */
@SpringBootApplication(scanBasePackages = "com.mouensis")
public class MouensisIdentityServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MouensisIdentityServerApplication.class, args);
	}

}
