package com.clinic.xadmin.repository.clinic;

import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import org.springframework.data.repository.CrudRepository;

public interface ClinicSatuSehatCredentialRepository extends CrudRepository<ClinicSatuSehatCredential, String>, ClinicSatuSehatCredentialCustomRepository {

}
