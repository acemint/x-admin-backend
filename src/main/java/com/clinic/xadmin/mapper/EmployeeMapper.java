package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeeMapper {

  EmployeeMapper INSTANCE = Mappers.getMapper( EmployeeMapper.class );

  EmployeeResponse createFrom(Employee employee);
  Employee createFrom(RegisterEmployeeRequest registerEmployeeRequest);

  List<EmployeeResponse> createFrom(List<Employee> employees);

}
