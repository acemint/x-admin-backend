package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeeResponseMapper {

  EmployeeResponseMapper INSTANCE = Mappers.getMapper( EmployeeResponseMapper.class );

  EmployeeResponse createFrom(Employee employee);

  List<EmployeeResponse> createFrom(List<Employee> employees);

}
