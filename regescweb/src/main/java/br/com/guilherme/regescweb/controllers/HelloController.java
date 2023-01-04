package br.com.guilherme.regescweb.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller     // Indica que a classe é um controller
public class HelloController {

    @GetMapping("/hello")       // Mapea a action para a URL "/hello" ==> quando entramos em localhost:8080/hello ele aciona a action
    public String hello() {
        return "hello";     // Procura pelo arquivo hello.html em ../templates/ e o renderiza
    }

    // Formas de mandar um request:

    // Usando o HttpServletRequest
    @GetMapping("/hello-servlet")
    public String hello(HttpServletRequest request) {
        request.setAttribute("nome", "Guilherme");      // Envia um atributo "nome" com o valor "Guilherme"
        return "hello";
    }

    // Usando o Model (do Spring)
    @GetMapping("/hello-model")
    public String hello(Model model) {
        model.addAttribute("nome", "Gui");
        return "hello";
    }

    // Usando o ModelAndView (do Spring)
    @GetMapping("/hello-mv")
    public ModelAndView helloMv() {
        ModelAndView mv = new ModelAndView("hello");    // Nome do arquivo a ser renderizado
        mv.addObject("nome", "Gu");
        return mv;
    }
}
