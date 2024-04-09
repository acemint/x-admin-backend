package com.clinic.xadmin.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;

@Configuration
@EnableVaultRepositories(value = "com.clinic.xadmin.repository")
public class VaultConfiguration {

}
