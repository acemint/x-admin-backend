package com.clinic.xadmin.service.practitioner;

import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.practitioner.PractitionerSearchResourceResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.practitioner.SatuSehatSearchPractitionerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Objects;

@Service
public class PractitionerServiceImpl implements PractitionerService {

  private final SatuSehatAPICallWrapper apiCallWrapper;
  private final ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository;

  @Autowired
  private PractitionerServiceImpl(SatuSehatAPICallWrapper apiCallWrapper, ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository) {
    this.apiCallWrapper = apiCallWrapper;
    this.clinicSatuSehatCredentialRepository = clinicSatuSehatCredentialRepository;
  }

  @Override
  public String getPractitionerFromSatuSehat(Member member) {
    if (StringUtils.hasText(member.getSatuSehatPractitionerReferenceId())) {
      return member.getSatuSehatPractitionerReferenceId();
    }

    String ihsCode = this.callGETPractitionerByAllEndpoints(member);
    if (StringUtils.hasText(ihsCode)) {
      return ihsCode;
    }
    throw new XAdminBadRequestException("Unable to get or create data");
  }

  private String callGETPractitionerByAllEndpoints(Member member) {
    // Try to obtain IHS Code by all provided endpoints
    if (Objects.nonNull(member.getNik())) {
      String ihsCode = callGETPractitionerEndpoint(SatuSehatSearchPractitionerEndpoint.builder()
          .nik(member.getNik())
          .build());
      if (Objects.nonNull(ihsCode)) {
        return ihsCode;
      }
    }

    return null;
  }

  private String callGETPractitionerEndpoint(SatuSehatEndpoint<StandardizedResourceResponse<PractitionerSearchResourceResponse>> endpoint) throws HttpStatusCodeException {
    ResponseEntity<StandardizedResourceResponse<PractitionerSearchResourceResponse>>
        response = this.apiCallWrapper.call(endpoint, clinicSatuSehatCredentialRepository.searchMainClinicApp());
    if (response.getBody().getEntries().isEmpty() || response.getBody().getTotal() > 1) {
      return null;
    }
    return response.getBody().getEntries().getFirst().getResource().getId();
  }

}
