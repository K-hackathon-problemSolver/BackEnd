package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Demand;

public interface DemandRepository extends JpaRepository<Demand, Long> {

}
