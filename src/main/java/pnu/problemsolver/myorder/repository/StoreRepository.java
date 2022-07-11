package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pnu.problemsolver.myorder.domain.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, String> {//extends JpaRepository하면 @Repository안해도 된다.
    //여기에 코드가 없어도 알아서 CRUD구현해줌.
    // save(entity)
    // findById(key), getOne(key) : 필요할 때 sql실행. 오버헤드있을 것.
    // save(enetity)
    // deleteById(key), delete(entitiy)


//페이징 필요

    //쿼리메소드
    List<Store> findByEmailBetweenOrderByEmailDesc(String from, String to);

    //쿼리메소드에서는 Id가 PK의 id가 아니라 필드변수명을 뜻한다.
    void deleteStoreByEmailLessThan(String id);

}
