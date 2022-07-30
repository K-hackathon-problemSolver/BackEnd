package pnu.problemsolver.myorder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void setting() {
        //store, customer는 연관관계 없어서 먼저 넣을 수 있다
        List<StoreDTO> storeDTOList = insertStore();
        List<CustomerDTO> customerDTOList = insertCustomer();

        //cake에는 store가 필요함.
        List<CakeDTO> cakeDTOList = insertCake(storeDTOList);
        insertDemand(cakeDTOList, customerDTOList);

    }

    //demand는 customer, cake 2개가 필요하다.
    private void insertDemand(List<CakeDTO> cakeDTOList, List<CustomerDTO> customerDTOList) {
        IntStream.rangeClosed(0, 20).forEach((i) -> {
            int idx = (int) (Math.random() * cakeDTOList.size());
            DemandDTO demandDTO = DemandDTO.builder()
                    .cakeUUID(cakeDTOList.get(idx).getUuid())
                    .customerUUID(customerDTOList.get(idx).getUuid())
                    .status(DemandStatus.WAITING)
                    .build();
            demandService.save(demandDTO);
        });



    }

    private List<CustomerDTO> insertCustomer() {

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
                    .filePath("src/main/resources/static/" + i + ".jpg")
                    .build();
             cakeService.save(cakeDTO);
            li.add(cakeDTO);
        }
        return li;

    }

}
