package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
