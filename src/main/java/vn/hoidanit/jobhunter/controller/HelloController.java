package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    // // Thêm CORS để không xung đột port, default config
    // @CrossOrigin
    public String getHelloWorld() {
        return "Update Hello World (Hỏi Dân IT & Eric)";
    }
}
