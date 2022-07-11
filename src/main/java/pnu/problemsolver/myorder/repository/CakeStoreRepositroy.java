package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.CakeStore;

public interface CakeStoreRepositroy extends JpaRepository<CakeStore, Long> {

}
