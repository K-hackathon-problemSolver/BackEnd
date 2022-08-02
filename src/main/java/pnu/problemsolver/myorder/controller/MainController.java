package pnu.problemsolver.myorder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.SNSType;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.CustomerDTO;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RestController
@RequiredArgsConstructor
public class MainController {

    private final StoreService storeService;
    private final CakeService cakeService;
    private final CustomerService customerService;

    private final DemandService demandService;


    @GetMapping("/")
    public String setting() {
        //store, customer는 연관관계 없어서 먼저 넣을 수 있다
        insertAll();
        return "success";
    }
    
    @Transactional
    public List<DemandDTO> insertAll() {
        List<StoreDTO> storeDTOList = insertStore();
        List<CustomerDTO> customerDTOList = insertCustomer();

        //cake에는 store가 필요함.
        List<CakeDTO> cakeDTOList = insertCake(storeDTOList);
        List<DemandDTO> demandDTOList = insertDemand(cakeDTOList, customerDTOList, storeDTOList);
        return demandDTOList;
    }
    
    //demand는 customer, cake 2개가 필요하다.
    public List<DemandDTO> insertDemand(List<CakeDTO> cakeDTOList, List<CustomerDTO> customerDTOList, List<StoreDTO> storeDTOList) {
        List<DemandDTO> demandDTOList = new ArrayList<>();
        Path p = Paths.get("src/main/resources/static/1.jpg");
        IntStream.rangeClosed(0, storeDTOList.size() - 1).forEach((i) -> {
//            int idx = (int) (Math.random() * storeDTOList.size());
            IntStream.rangeClosed(0, 20).forEach(idx->{
    
                DemandDTO demandDTO = DemandDTO.builder()
                        .filePath(p.toString())
                        .cakeUUID(cakeDTOList.get(i).getUuid())
                        .customerUUID(customerDTOList.get(i).getUuid())
                        .storeUUID(storeDTOList.get(i).getUuid())
                        .status(DemandStatus.WAITING)
                        .build();
//                System.out.println("실험 : " + storeDTOList.get(idx).getUuid());
                demandService.save(demandDTO);
                Demand byId = demandService.findById(param -> param, demandDTO.getUuid());
    
                //TODO : 동작안함..왜일까..영속성 때문인 것 같긴 하다.
//                byId.setCreated(byId.getCreated().minusHours(1));
                demandDTOList.add(demandDTO);
            });
        });
        return demandDTOList;
        
        
    }

    public List<CustomerDTO> insertCustomer() {

        List<CustomerDTO> li = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            CustomerDTO cus = CustomerDTO.builder()
                    .email("email" + i)
                    .name("custmer" + i)
                    .phone_num("010-1234-7890")
                    .birthYear(1999 + i)
                    .snsIdentifyKey("snsid~")
                    .snsType(SNSType.NAVER)
                    .gender(Gender.MAN)
                    .build();

            customerService.save(cus);
            li.add(cus);
        }

        for (int i = 0; i < 6; ++i) {
            CustomerDTO cus = CustomerDTO.builder()
                    .email("email" + i)
                    .name("custmer" + i)
                    .phone_num("010-1234-7890")
                    .birthYear(1999 + i)
                    .snsIdentifyKey("snsid~")
                    .snsType(SNSType.KAKAO)
                    .gender(Gender.MAN)
                    .build();

             customerService.save(cus);
            li.add(cus);
        }
        return li;
    }

    public List<StoreDTO> insertStore() {
        List<StoreDTO> li = new ArrayList<>();
        for (int i = 1; i <= 11; ++i) { //사진이 11개라서 11개만 해야함.
            StoreDTO st = StoreDTO.builder()
                    .snsIdentifyKey("snskey")
                    .birthYear(1999)
                    .name("store" + i)
                    .description("맛있는 가게!")
                    .location("부산시 금정구 부산대학로~")
                    .email("zhdhfhd33@zsdf")
                    .snsType(SNSType.KAKAO)
                    .latitude(i)
                    .longitude(i)
                    .owner_phone_num("010-3391-6486")
                    .filePath("src/main/resources/static/" + i + ".jpg")
                    .build();
            storeService.save(st);
            li.add(st);
        }
        return li;
    }


    public List<CakeDTO> insertCake(List<StoreDTO> list) {//cake는 연관관계가 있어서 store를 필요로 한다.
        List<CakeDTO> li = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {//lise.size() = 11
            CakeDTO cakeDTO = CakeDTO.builder()
                    .minPrice(3000 * i)
                    .name("케이크" + i)
                    .option("{\"color\" : [\"red\", \"blue\", \"green\"], \"size\" : [\"1\", \"2\", \"3\"]}")
                    .storeUUID(list.get(i).getUuid()) //연관관계
                    .description("생일 선물로 딱맞음!!")
                    .filePath("src/main/resources/static/" + (i+1) + ".jpg")
                    .build();
             cakeService.save(cakeDTO);
            li.add(cakeDTO);
        }
        return li;

    }

}
