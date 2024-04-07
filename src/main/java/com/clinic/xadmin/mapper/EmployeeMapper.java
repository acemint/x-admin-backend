package com.clinic.xadmin.mapper;

import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.RegisterEmployeeData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeeMapper {

  EmployeeMapper INSTANCE = Mappers.getMapper( EmployeeMapper.class );

  EmployeeResponse createFrom(Employee employee);
  Employee createFrom(RegisterEmployeeData registerEmployeeData);

  List<EmployeeResponse> createFrom(List<Employee> employees);

  RegisterEmployeeData convertFromDtoToModel(RegisterEmployeeRequest registerEmployeeRequest);

}
