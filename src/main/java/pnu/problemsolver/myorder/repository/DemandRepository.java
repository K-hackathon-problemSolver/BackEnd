package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Demand;

import java.util.UUID;

public interface DemandRepository extends JpaRepository<Demand, UUID> {

}
