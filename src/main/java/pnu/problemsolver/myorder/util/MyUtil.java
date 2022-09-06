package pnu.problemsolver.myorder.util;

import lombok.extern.slf4j.Slf4j;
import pnu.problemsolver.myorder.dto.CakeEditDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class MyUtil {
	
	//이게 캐싱이다.
	public static Base64.Decoder decoder = Base64.getDecoder();
	public static Base64.Encoder encoder = Base64.getEncoder();
	
	
	public static Path saveFile(Path dirName, String fileName, String fileContent) {//이게 만능임.
//		byte[] bytes = Base64.getDecoder().decode(d.getFile());//디코딩 안하고 그대로 저장한다.
		File dir = (dirName.toFile());
		if (!dir.exists()) {//존재하지 않으면 생성.
			dir.mkdirs();
		}
		Path imgPath = Paths.get(dir.toPath() + File.separator + fileName);
		try {
			Files.write(imgPath, fileContent.getBytes());
		} catch (IOException e) {
			log.error("img write error!");
			e.printStackTrace();
		}
		return imgPath;
	}

	
	public static List<Path> saveCakeImg(Path storeDirPath, List<CakeEditDTO> cakeDTOList) throws IOException {
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
	
	public static String makeCakePath(String storeDirPath, CakeEditDTO cakeEditDTO) {
		return storeDirPath + File.separator + cakeEditDTO.getName() + "." + cakeEditDTO.getExtension();
	}
	
	public static boolean isFileExtensionOk(String extension) {
	//pdf는 빼기로 했다.
			return Arrays.asList("jpg", "jpeg", "png", "bmp", "jfif").contains(extension);
		}
}
