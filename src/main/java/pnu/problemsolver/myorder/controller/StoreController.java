package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.CakeEditDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreEditDTO;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final CakeService cakeService;

    private final String uploadPath;

    public StoreController(Environment en, StoreService storeService, CakeService cakeService) {
        this.storeService = storeService;
        this.uploadPath = Objects.requireNonNull(en.getProperty("myorder.uploadPath"), "application.properties 로드 실패");
        this.cakeService = cakeService;
    }

    @PostMapping("/save")
    public StoreDTO saveStore(@RequestBody StoreDTO storeDTO)  {
//        log.info(Mapper.objectMapper.writeValueAsString(storeDTO));
        StoreDTO resDTO = storeService.save(storeDTO); //없으면 저장하고 있다면 null아닌 것만 자동으로 덮어써진다.!
        return resDTO;
    }

    @PostMapping("/editMenu")
    //자동으로 StoreSaveDTO로 맵핑된다!
    public String editStoreMenu(@RequestBody StoreEditDTO storeEditDTO) throws IOException {
        if (!isFileExtensionOk(storeEditDTO.getExtension())) {
            return "\"jpg\", \"jpeg\", \"png\", \"bmp\", \"pdf\", \"jfif\"확장자만 가능합니다.";
        }
        //경로 만들기.
        Path storeDirPath = Paths.get(uploadPath + File.separator + storeEditDTO.getUuid());
        System.out.println(storeDirPath.toString());
        System.out.println(storeDirPath.toAbsolutePath());

        File mainFile = storeDirPath.toFile();
        if (!mainFile.exists()) {
            log.warn("파일이 없습니다.");
            mainFile.mkdirs();//전부 파일로 만들어버림.
        }

        Path mainPath = Paths.get(storeDirPath + File.separator + "mainImg." + storeEditDTO.getExtension());
        //디코딩, 파일 생성.
        byte[] encodedBytes = Base64.getDecoder().decode(storeEditDTO.getMainImg());
        Files.write(mainPath, encodedBytes);
        //mainImg끝.


        List<CakeEditDTO> cakeDTOList = storeEditDTO.getCakeList();
        List<String> cakePathList = new ArrayList<>();//나중에 쓰기위함.
        //cake사진 저장.
        for (CakeEditDTO i : cakeDTOList) {
            String path = makeCakePath(storeDirPath.toString(), i);
            cakePathList.add(path);
            File cakeFile = new File(path);
            byte[] decoded = Base64.getDecoder().decode(i.getImg());
            Files.write(cakeFile.toPath(), decoded);
        }

        //store update
        StoreDTO storeDTO = StoreDTO.builder()
                .uuid(storeEditDTO.getUuid())
                .filePath(mainPath.toString())
                .description(storeEditDTO.getDescription())
//                .impossibleDate(storeEditDTO.getImpossibleDate())
                .build();
        storeService.save(storeDTO);

        //cake update.
        //TODO 시간 남으면 sql하나로 만들기. 아래처럼 하면 성능 bad.
        for (int i = 0; i < cakeDTOList.size(); ++i) {

            CakeDTO cakeDTO = CakeDTO.builder()
                    .filePath(cakePathList.get(i))
                    .option(cakeDTOList.get(i).getOption())
                    .min_price(cakeDTOList.get(i).getMinPrice())
                    .build();
            cakeService.save(cakeDTO);

        }
        return "success";
    }

    @PostMapping("/editImpossibleDate")
    public String editImpossibleDate(@RequestBody Map map) {
        StoreDTO storeDTO = StoreDTO.builder()
                .uuid(UUID.fromString((String) map.get("uuid")) )
                .impossibleDate((String) map.get("impossibleDate"))
                .build();
        storeService.save(storeDTO);
        return "success";

    }

    @GetMapping("/")
    public


    public boolean isFileExtensionOk(String extension) {

        return Arrays.asList("jpg", "jpeg", "png", "bmp", "pdf", "jfif").contains(extension);
    }

    public String makeCakePath(String storeDirPath, CakeEditDTO cakeEditDTO) {
        return storeDirPath + File.separator + cakeEditDTO.getName() + "." + cakeEditDTO.getExtension();

    }
}
