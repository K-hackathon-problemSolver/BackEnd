package pnu.problemsolver.myorder.repository;

import org.hibernate.validator.internal.engine.resolver.JPATraversableResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Cake;

public interface CakeRepositroy extends JpaRepository<Cake, Long> {

}
