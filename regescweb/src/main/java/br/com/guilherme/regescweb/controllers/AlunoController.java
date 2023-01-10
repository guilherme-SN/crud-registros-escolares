package br.com.guilherme.regescweb.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/alunos")
public class AlunoController {

    @GetMapping("")
    public ModelAndView index() {

        return new ModelAndView("/alunos/index");
    }
}
