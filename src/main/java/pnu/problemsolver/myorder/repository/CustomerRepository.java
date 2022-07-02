package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
