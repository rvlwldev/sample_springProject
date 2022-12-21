package com.shopping.study.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @GetMapping("/hello")
    public ModelAndView mainPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/hello.html");
        return view;
    }
}
