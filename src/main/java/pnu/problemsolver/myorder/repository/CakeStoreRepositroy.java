package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.CakeStore;

import java.util.UUID;

public interface CakeStoreRepositroy extends JpaRepository<CakeStore, UUID> {

}
