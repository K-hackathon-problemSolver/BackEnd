package pnu.problemsolver.myorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.StoreService;

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

    /**
     *
     * @param storeEditDTO
     * @return 유효하지 않은 uuid를 가지고 있으면 null반환. 성공하면 success
     * @throws IOException
     */
    @PostMapping("/editMenu")
    //자동으로 StoreEditDTO로 맵핑된다!
    public String editStoreMenu(@RequestBody StoreEditDTO storeEditDTO) throws IOException {
        if (!isFileExtensionOk(storeEditDTO.getExtension())) {
            return "\"jpg\", \"jpeg\", \"png\", \"bmp\", \"jfif\"확장자만 가능합니다."; //pdf는 지움.
        }
        //store dir 만들기.
        Path storeDirPath = makeStorePath(storeEditDTO);
        Path mainImgPath = Paths.get(storeDirPath + File.separator + "mainImg." + storeEditDTO.getExtension());


        //디코딩, 파일 생성.
        byte[] encodedBytes = Base64.getDecoder().decode(storeEditDTO.getMainImg());
        //mainImg 저장
        Files.write(mainImgPath, encodedBytes);

        List<CakeEditDTO> cakeEditDTOList = storeEditDTO.getCakeList();

        //cake사진 저장.
        List<Path> cakePathList=saveCakeImg(storeDirPath, cakeEditDTOList);

        //store update
        boolean a = storeService.saveOnlyNotNUll(storeEditDTO, mainImgPath);
        if (!a) {
            return null;
        }
//        StoreDTO storeDTOById = storeService.findById(storeEditDTO.getUuid());
//        StoreDTO storeDTO = StoreDTO.toDTO(storeEditDTO, mainImgPath.toString());
//        storeService.save(storeDTO);

        //cake update.
        //TODO 시간 남으면 sql하나로 만들기. 아래처럼 하면 성능 bad.
        for (int i = 0; i < cakeEditDTOList.size(); ++i) {
//            CakeDTO cakeDTO = CakeDTO.toDTO(cakeDTOList.get(i), cakePathList.get(i).toString(), storeEditDTO.getUuid());
//            cakeService.save(cakeDTO);
            boolean b = cakeService.saveOnlyNotNUll(cakeEditDTOList.get(i), cakePathList.get(i));
            if (!b) {
                return null;
            }
        }
        return "success";
    }

    //반환하는게 경로라면 string이지만 Path객체를 사용하자. Path 객체를 사용하지 않더라도 그게 더 직관적이다.!
    private List<Path> saveCakeImg(Path storeDirPath, List<CakeEditDTO> cakeDTOList) throws IOException {
        List<Path> cakePathList = new ArrayList<>();
        for (CakeEditDTO i : cakeDTOList) {
            Path path = Paths.get(makeCakePath(storeDirPath.toString(), i));
            cakePathList.add(path);
            File cakeFile = path.toFile();
            byte[] decoded = Base64.getDecoder().decode(i.getImg());
            Files.write(cakeFile.toPath(), decoded);
        }
        return cakePathList;
    }

    private Path makeStorePath(StoreEditDTO storeEditDTO) {
        Path storeDirPath = Paths.get(uploadPath + File.separator + storeEditDTO.getUuid());
//        System.out.println(storeDirPath.toString());
//        System.out.println(storeDirPath.toAbsolutePath());
        File mainFile = storeDirPath.toFile();
        if (!mainFile.exists()) {
            log.warn("파일이 없습니다.");
            mainFile.mkdirs();//전부 파일로 만들어버림.
        }
        return storeDirPath;
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

//    @GetMapping("/list")//GET으로 오면 preflight만 준다.
//    public List<StoreDTOForListPreflight> listPreflights() {
//        List<StoreDTOForListPreflight> li = storeService.getAllPreflights();
//        return li;
//    }

    @GetMapping("/list")
    public List<StoreDTOForList> list() {
        List<StoreDTOForList> all = storeService.findAll(a->StoreDTOForList.toDTO(a)); //store->T로 변환 함수만 넣어주면 된다.!
        System.out.println(all);
        return all;
    }

    @PostMapping("/list")
    public List<StoreDTOForList> list(@RequestBody ArrayList<UUID> li) {

        List<StoreDTOForList> resList = storeService.findAllInUUIDList(li);
        //encoding
        for (StoreDTOForList i : resList) {
            byte[] encodedImg = Base64.getEncoder().encode(i.getMainImg());
            i.setMainImg(encodedImg);
        }
        return resList;

    }

    public boolean isFileExtensionOk(String extension) {
//pdf는 빼기로 했다.
        return Arrays.asList("jpg", "jpeg", "png", "bmp", "jfif").contains(extension);
    }

    public String makeCakePath(String storeDirPath, CakeEditDTO cakeEditDTO) {
        return storeDirPath + File.separator + cakeEditDTO.getName() + "." + cakeEditDTO.getExtension();
    }
}
