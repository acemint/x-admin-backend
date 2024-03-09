package com.clinic.xadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestXAdminApplication {

	public static void main(String[] args) {
		SpringApplication.from(XAdminApplication::main).with(TestXAdminApplication.class).run(args);
	}

}
