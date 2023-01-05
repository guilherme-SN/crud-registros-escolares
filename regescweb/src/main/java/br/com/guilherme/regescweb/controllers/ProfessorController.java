package br.com.guilherme.regescweb.controllers;


import br.com.guilherme.regescweb.dto.RequisicaoFormProfessor;
import br.com.guilherme.regescweb.models.Professor;
import br.com.guilherme.regescweb.models.StatusProfessor;
import br.com.guilherme.regescweb.repositories.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired      // Identifica que é uma dependência e, automaticamente, vai injetá-la (sem precisar do construtor)
    private ProfessorRepository professorRepository;


    @GetMapping("")
    public ModelAndView index() {

        List<Professor> professores = this.professorRepository.findAll();

        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);

        return mv;
    }


    @GetMapping("/new")
    public ModelAndView newProfessor(RequisicaoFormProfessor requisicao) {      // Esse parâmetro é necessário para explicitar que essa action está atrelada ao forms

        ModelAndView mv = new ModelAndView("/professores/new");
        mv.addObject("statusProfessor", StatusProfessor.values());

        return mv;
    }


    // Quando submetemos o formulário, ele manda uma requisição pelo verbo POST acionando essa action
    @PostMapping("")
    // Spring automaticamente "seta" os valores dos campos do form para os atributos do objeto requisicao
    // @Valid indica que "requisicao" deve ser válido e, bindingResult será o resultado dessa validação
    public ModelAndView create(@Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {

        // Verificamos a validação
        if (bindingResult.hasErrors()) {
            System.out.println("\n************** TEM ERROS **************\n");

            ModelAndView mv = new ModelAndView("/professores/new");
            mv.addObject("statusProfessor", StatusProfessor.values());

            return mv;
        } else {
            Professor professor = requisicao.toProfessor();
            this.professorRepository.save(professor);

            return new ModelAndView("redirect:/professores/" + professor.getId());
        }
    }


    @GetMapping("/{id}")    // Mapeamento dos professores associando o seu "id"
    public ModelAndView show(@PathVariable Long id) {       // @PathVariable indica que o parâmetro recebido deve vir da URL

        // Verifica se o professor com o id passado existe
        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);

        if (optionalProfessor.isPresent()) {
            Professor professor = optionalProfessor.get();

            ModelAndView mv = new ModelAndView("professores/show");
            mv.addObject("professor", professor);       // Passamos um objeto da entidade Professor para a view professores/show.html

            return mv;
        } else {
            return this.retornaErroProfessor("SHOW ERROR: Professor #" + id + " não foi encontrado!");
        }
    }


    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicao) {

        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);

        if (optionalProfessor.isPresent()) {
            Professor professor = optionalProfessor.get();
            requisicao.fromProfessor(professor);        // Converte a entidade Professor para a classe DTO

            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("statusProfessor", StatusProfessor.values());      // Passa os valores possíveis para o campo de status
            mv.addObject("professorId", id);

            return mv;
        } else {
            return this.retornaErroProfessor("EDIT ERROR: Professor #" + id + " não foi encontrado!");
        }
    }


    @PatchMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("professorId", id);
            mv.addObject("statusProfessor", StatusProfessor.values());

            return mv;
        } else {
            Optional<Professor> optionalProfessor = this.professorRepository.findById(id);

            if (optionalProfessor.isPresent()) {
                ModelAndView mv = new ModelAndView("redirect:/professores/" + id);
                Professor professor = requisicao.toProfessor(optionalProfessor.get());

                this.professorRepository.save(professor);

                // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
                mv.addObject("message", "Professor #" + id + " atualizado com sucesso!");
                mv.addObject("error", false);

                return mv;
            } else {
                return this.retornaErroProfessor("UPDATE ERROR: Professor #" + id + " não foi encontrado!");
            }
        }
    }


    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {

        ModelAndView mv = new ModelAndView("redirect:/professores");

        try {
            this.professorRepository.deleteById(id);

            // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
            mv.addObject("message", "Professor #" + id + " deletado com sucesso!");
            mv.addObject("error", false);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            mv = this.retornaErroProfessor("DELETE ERROR: Professor #" + id + " não encontrado!");
        }

        return mv;
    }


    private ModelAndView retornaErroProfessor(String msg) {
        ModelAndView mv = new ModelAndView("redirect:/professores");

        // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
        mv.addObject("message", msg);
        mv.addObject("error", true);

        return mv;
    }


    private ModelAndView retornaErroProfessor(String msg, Long id) {
        ModelAndView mv = new ModelAndView("redirect:/professores/" + id);

        // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
        mv.addObject("message", msg);
        mv.addObject("error", true);

        return mv;
    }
}
