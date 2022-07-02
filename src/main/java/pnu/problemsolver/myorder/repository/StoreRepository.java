package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, String> {
    //여기에 코드가 없어도 알아서 CRUD구현해줌.
    // save(entity)
    // findById(key), getOne(key) : 필요할 때 sql실행. 오버헤드있을 것.
    // save(enetity)
    // deleteById(key), delete(entitiy)


//페이징 필요

    //쿼리메소드
    List<Store> findByIdBetweenOrderByIdDesc(String from, String to);

    void deleteStoreByIdLessThan(String id);

}
