package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = CustomUserDetailsServiceImpl.BEAN_NAME)
public class CustomUserDetailsServiceImpl implements UserDetailsService {

  public static final String BEAN_NAME = "CustomUserDetailsServiceImpl";

  private final EmployeeRepository employeeRepository;

  @Autowired
  public CustomUserDetailsServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Employee employee = this.employeeRepository.findEmployeeByUsername(username);
    if (Objects.isNull(employee)) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return CustomUserDetailsFactory.createFrom(employee);
  }
}
