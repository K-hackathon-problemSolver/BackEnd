package pnu.problemsolver.myorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.problemsolver.myorder.dto.StoreSaveDTO;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Value("${myorder.uploadPath}")
    private String uploadPath;

    @PostMapping("/store")
    //자동으로 StoreSaveDTO로 맵핑된다!
    public void uploadFiles(@RequestBody StoreSaveDTO storeSaveDTO) throws IOException {
//        File file = new File("C:\\Users\\minkun\\Desktop\\minkun\\BackEnd\\src\\main\\java\\pnu\\problemsolver\\myorder\\controller\\testPicture.jpg");
//        System.out.println(file.toPath());

        byte[] encodedBytes = storeSaveDTO.getMainImg();
        Path path = Paths.get(uploadPath+"/"+"mainImg");

        encodedBytes = Base64.getDecoder().decode(encodedBytes);
        Files.write(path, encodedBytes);
    }
}
