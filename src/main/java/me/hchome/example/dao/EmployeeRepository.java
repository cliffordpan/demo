package me.hchome.example.dao;

import me.hchome.example.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Cliff Pan
 */
@Repository
public interface EmployeeRepository extends AccountRepository<Employee> {

	@EntityGraph("Employee.withRoles")
	@Override
	Optional<Employee> findOneByEmail(String email);
}
