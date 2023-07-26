package br.com.guilherme.regescweb.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // Serve para tratar exceções em todos os controllers da aplicação
public class GlobalController {

    @ModelAttribute("servletPath")      // Registra esse atributo para todos os controllers da aplicação
    public String getRequestServletPath(HttpServletRequest request) {

        return request.getServletPath();    // Alternativa para o request.getRequestURI()
    }
}
