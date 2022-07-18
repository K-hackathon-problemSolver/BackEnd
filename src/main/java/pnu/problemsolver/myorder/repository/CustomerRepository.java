package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Customer;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
