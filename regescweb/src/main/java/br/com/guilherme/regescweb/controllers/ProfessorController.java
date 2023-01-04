package br.com.guilherme.regescweb.controllers;


import br.com.guilherme.regescweb.dto.RequisicaoNovoProfessor;
import br.com.guilherme.regescweb.models.Professor;
import br.com.guilherme.regescweb.models.StatusProfessor;
import br.com.guilherme.regescweb.repositories.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

@Controller
public class ProfessorController {

    @Autowired      // Identifica que é uma dependência e, automaticamente, vai injeta-la (sem precisar do construtor)
    private ProfessorRepository professorRepository;


    @GetMapping("/professores")
    public ModelAndView index() {

        List<Professor> professores = this.professorRepository.findAll();

        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);

        return mv;
    }


    @GetMapping("/professores/new")
    public ModelAndView newProfessor(RequisicaoNovoProfessor requisicao) {      // Esse parâmetro é necessário para explicitar que essa action está atrelada ao forms

        ModelAndView mv = new ModelAndView("/professores/new");
        mv.addObject("statusProfessor", StatusProfessor.values());

        return mv;
    }


    // Quando submetemos o formulário, ele manda uma requisição pelo verbo POST acionando essa action
    @PostMapping("/professores")
    // Spring automaticamente "seta" os valores dos campos do form para os atributos do objeto requisicao
    // @Valid indica que "requisicao" deve ser válido e, bindingResult será o resultado dessa validação
    public ModelAndView create(@Valid RequisicaoNovoProfessor requisicao, BindingResult bindingResult) {

        // Verificamos a validação
        if (bindingResult.hasErrors()) {
            System.out.println("\n************** TEM ERROS **************\n");

            ModelAndView mv = new ModelAndView("/professores/new");
            mv.addObject("statusProfessor", StatusProfessor.values());

            return mv;
        } else {
            Professor professor = requisicao.toProfessor();
            this.professorRepository.save(professor);

            return new ModelAndView("redirect:/professores");
        }
    }
}
