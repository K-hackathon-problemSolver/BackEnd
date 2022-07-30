package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pnu.problemsolver.myorder.domain.constant.SNSType;
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
    @Query(nativeQuery = true, value = "select * from store order by (latitude-:lati) + (longitude-:longi) limit :limit offset :offset")
    List<Store> findByLocation(@Param("lati") double lati, @Param("longi")double longi, @Param("limit")int limit, @Param("offset")int offset);
    
    
    
    //쿼리메소드에서는 Id가 PK의 id가 아니라 필드변수명을 뜻한다.
    
    @Query("select s from Store s where s.uuid in :list")
    List<Store> findAllInUUIDList(@Param("list") List<UUID> list);//개쉽네.. 그냥 하면 되네...

    List<Store> findByName(String name);

    public List<Store> findBySnsTypeAndSnsIdentifyKey(SNSType type, String key);



}
