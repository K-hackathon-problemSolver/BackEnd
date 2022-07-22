package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreUpdateDTO;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {

    private final StoreService storeService;

    private final String uploadPath;

    public StoreController(Environment en, StoreService storeService) {
        this.storeService = storeService;
        this.uploadPath = Objects.requireNonNull(en.getProperty("myorder.uploadPath"), "application.properties 로드 실패");
    }

    @PostMapping("/save")
    public StoreDTO storeSave(@RequestBody StoreDTO storeDTO) throws JsonProcessingException {
        log.info(Mapper.objectMapper.writeValueAsString(storeDTO));
        StoreDTO resDTO = storeService.save(storeDTO); //없으면 저장하고 있다면 null아닌 것만 자동으로 덮어써진다.!
        return resDTO;
    }

//    @PostMapping("/update")
//    //자동으로 StoreSaveDTO로 맵핑된다!
//    public void updateStore(@RequestBody StoreUpdateDTO storeUpdateDTO) throws IOException {
////TODO : save하고 나서 update하자.
//        //경로 만들기.
//        Path path = Paths.get(uploadPath+File.separator+ storeUpdateDTO.getUuid()+"mainImg");
//        File file = path.toFile();
//        if (!file.exists()) {
//            log.warn("파일이 없습니다.");
//            file.mkdirs();
//        }
//
//        //디코딩, 파일 생성.
//        byte[] encodedBytes = Base64.getDecoder().decode(storeUpdateDTO.getMainImg());
//        Files.write(path, encodedBytes);
//
//        //TODO : 여기서 store 덮어쓰기.
//        storeService.save()
//    }
}
