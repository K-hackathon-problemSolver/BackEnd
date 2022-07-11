package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.CakeDemand;
import pnu.problemsolver.myorder.domain.CakeStore;

public interface CakeDemandRepositroy extends JpaRepository<CakeDemand, Long> {

}
