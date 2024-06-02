package com.clinic.xadmin.service.practitioner;

import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;

public interface PractitionerService {

  String getPractitionerFromSatuSehat(Member member);

}
