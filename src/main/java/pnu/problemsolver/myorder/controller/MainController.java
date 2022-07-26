package pnu.problemsolver.myorder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class MainController {

    @GetMapping("/")
    public void setting() {
        //TODO : 더미데이터 넣기!
    }
}
