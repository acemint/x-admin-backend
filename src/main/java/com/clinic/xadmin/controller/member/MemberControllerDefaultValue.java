package com.clinic.xadmin.controller.member;

import com.clinic.xadmin.repository.member.MemberCustomRepositoryImpl;

import java.util.Set;


public interface MemberControllerDefaultValue {
  String DEFAULT_SORT_ORDER = "ASC";
  String DEFAULT_PAGE_NUMBER = "0";
  String DEFAULT_PAGE_SIZE = "10";
  // TODO: Define where to put this sorted by, because this is somehow defined in controller but it is needed to be known in repository
  Set<String> AVAILABLE_SORTED_BY = MemberCustomRepositoryImpl.AVAILABLE_SORTED_BY;

}
