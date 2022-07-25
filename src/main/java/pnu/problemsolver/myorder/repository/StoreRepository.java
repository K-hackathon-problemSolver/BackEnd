package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pnu.problemsolver.myorder.domain.Store;

import java.util.List;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {//extends JpaRepository하면 @Repository안해도 된다.
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

    @Query("select s from Store s where s.uuid in :list")
    List<Store> findAllInUUIDList(@Param("list") List<UUID> list);//개쉽네.. 그냥 하면 되네...

    List<Store> findByName(String name);

}
