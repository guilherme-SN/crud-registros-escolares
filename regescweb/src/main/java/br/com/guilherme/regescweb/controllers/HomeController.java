package br.com.guilherme.regescweb.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {

        return "home.html";      // renderiza o arquivo templates/home.html
    }
}
