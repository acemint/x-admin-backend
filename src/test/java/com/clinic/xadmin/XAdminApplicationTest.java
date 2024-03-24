package com.clinic.xadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestConfiguration(proxyBeanMethods = false)
public class XAdminApplicationTest {

	public static void main(String[] args) {
		SpringApplication.from(XAdminApplication::main).with(XAdminApplicationTest.class).run(args);
	}

}
