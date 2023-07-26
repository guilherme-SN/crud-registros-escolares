package br.com.guilherme.regescweb.controllers;


import br.com.guilherme.regescweb.dto.RequisicaoFormProfessor;
import br.com.guilherme.regescweb.models.Professor;
import br.com.guilherme.regescweb.models.StatusProfessor;
import br.com.guilherme.regescweb.repositories.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorRepository professorRepository;

    // Injeção de Dependência
    @Autowired
    public ProfessorController(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @GetMapping("")
    public ModelAndView index() {

        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", this.professorRepository.findAll());

        return mv;
    }


    @GetMapping("/new")
    public ModelAndView newProfessor(RequisicaoFormProfessor requisicao) {      // Esse parâmetro é necessário para explicitar que essa action está atrelada ao formulário

        ModelAndView mv = new ModelAndView("/professores/new");
        mv.addObject("statusProfessor", StatusProfessor.values());

        return mv;
    }


    // Quando submetemos o formulário, ele manda uma requisição pelo verbo POST acionando essa action
    @PostMapping("")
    // Spring automaticamente "seta" os valores dos campos do formulário para os atributos do objeto requisicao
    // @Valid indica que "requisicao" deve ser válido e, bindingResult será o resultado dessa validação
    public ModelAndView create(@Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {

        // Verificamos a validação
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/professores/new");
            mv.addObject("statusProfessor", StatusProfessor.values());

            return mv;
        }

        // Converte um objeto da classe DTO para a classe entidade
        Professor professor = requisicao.toProfessor();

        // Faz a persistência no banco de dados
        this.professorRepository.save(professor);

        return new ModelAndView("redirect:/professores/" + professor.getId());

    }


    @GetMapping("/{id}")    // Mapeamento dos professores associando o seu "id"
    public ModelAndView show(@PathVariable Long id) {       // @PathVariable indica que o parâmetro recebido deve vir da URL

        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);

        // Verifica se o professor com o id passado existe
        if (optionalProfessor.isPresent()) {
            // Recupera o professor
            Professor professor = optionalProfessor.get();

            ModelAndView mv = new ModelAndView("professores/show");

            // Passa um objeto da entidade Professor para a view professores/show.html
            mv.addObject("professor", professor);

            // Passa a lista de disciplinas que um professor está associado para o Thymeleaf
            mv.addObject("disciplinas", professor.getDisciplinas());

            return mv;
        }

        return this.retornaErroProfessor("SHOW ERROR: Professor #" + id + " não foi encontrado!");
    }


    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicao) {

        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);

        // Verifica se o professor com o id passado existe
        if (optionalProfessor.isPresent()) {
            Professor professor = optionalProfessor.get();

            // Converte a entidade Professor para a classe DTO
            requisicao.fromProfessor(professor);

            ModelAndView mv = new ModelAndView("professores/edit");

            // Passa os valores de status possíveis para o Thymeleaf
            mv.addObject("statusProfessor", StatusProfessor.values());
            mv.addObject("professorId", id);

            return mv;
        }

        return this.retornaErroProfessor("EDIT ERROR: Professor #" + id + " não foi encontrado!");
    }


    @PatchMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {

        // Verifica se há erros nos dados passados pelo formulário
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("professores/edit");

            mv.addObject("statusProfessor", StatusProfessor.values());
            mv.addObject("professorId", id);

            return mv;
        }

        Optional<Professor> optionalProfessor = this.professorRepository.findById(id);

        // Verifica se o professor com o id passado existe
        if (optionalProfessor.isPresent()) {
            ModelAndView mv = new ModelAndView("redirect:/professores/" + id);

            // Converte um objeto da classe DTO para a classe entidade a partir de um professor válido
            Professor professor = requisicao.toProfessor(optionalProfessor.get());

            // Faz a persistência no banco de dados
            this.professorRepository.save(professor);

            // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
            mv.addObject("message", "Professor #" + id + " atualizado com sucesso!");
            mv.addObject("error", false);

            return mv;
        }

        return this.retornaErroProfessor("UPDATE ERROR: Professor #" + id + " não foi encontrado!");
    }


    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {

        try {
            // Tenta deletar o professor com o id passado
            this.professorRepository.deleteById(id);

            ModelAndView mv = new ModelAndView("redirect:/professores");

            // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
            mv.addObject("message", "Professor #" + id + " deletado com sucesso!");
            mv.addObject("error", false);

            return mv;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return this.retornaErroProfessor("DELETE ERROR: Professor #" + id + " não encontrado!");
        }
    }


    // Função para setar os parâmetros de erro ao realizar uma operação

    private ModelAndView retornaErroProfessor(String msg) {
        ModelAndView mv = new ModelAndView("redirect:/professores");

        // Passa os objetos que serão montados na URL pelo Thymeleaf a partir do ${param.<nome>}
        mv.addObject("message", msg);
        mv.addObject("error", true);

        return mv;
    }
}
