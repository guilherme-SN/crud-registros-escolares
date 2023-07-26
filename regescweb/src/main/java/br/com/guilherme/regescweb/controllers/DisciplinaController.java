package br.com.guilherme.regescweb.controllers;

import br.com.guilherme.regescweb.dto.RequisicaoFormDisciplina;
import br.com.guilherme.regescweb.models.Disciplina;
import br.com.guilherme.regescweb.repositories.DisciplinaRepository;
import br.com.guilherme.regescweb.repositories.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping(value = "/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;


    // Injeção de Dependência
    @Autowired
    public DisciplinaController(DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
    }


    @GetMapping("")
    public ModelAndView index() {

        ModelAndView mv = new ModelAndView("disciplinas/index");
        mv.addObject("disciplinas", this.disciplinaRepository.findAll());

        return mv;
    }


    @GetMapping("/new")
    public ModelAndView newDisciplina(RequisicaoFormDisciplina requisicao) {

        ModelAndView mv = new ModelAndView("/disciplinas/new");
        mv.addObject("professores", this.professorRepository.findAll());

        return mv;
    }


    @PostMapping("")
    public ModelAndView create(@Valid RequisicaoFormDisciplina requisicao, BindingResult result) {

        // Verifica se há erros nos valores enviados pelo formulário
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("/disciplinas/new");
            mv.addObject("professores", this.professorRepository.findAll());

            return mv;
        }

        // Converte um objeto da classe DTO para a classe entidade
        Disciplina disciplina = requisicao.toDisciplina(this.professorRepository);

        // Faz a persistência no banco de dados
        this.disciplinaRepository.save(disciplina);

        return this.retornaSucessoDisciplina("Disciplina criada com sucesso!");
    }


    @GetMapping("/{id}")
    public ModelAndView showDisciplinas(@PathVariable Long id) {

        Optional<Disciplina> optionalDisciplina = this.disciplinaRepository.findById(id);

        // Verifica se a disciplina com o id passado existe
        if (optionalDisciplina.isPresent()) {
            ModelAndView mv = new ModelAndView("/disciplinas/show");

            // Recupera e manda a disciplina para o Thymeleaf
            mv.addObject("disciplina", optionalDisciplina.get());

            return mv;
        }

        return new ModelAndView("redirect:/disciplinas");
    }


    @GetMapping("/{id}/alunos")
    public ModelAndView showAlunos(@PathVariable Long id) {

        Optional<Disciplina> disciplinaOptional = this.disciplinaRepository.findById(id);

        // Verifica se a disciplina com o id passado existe
        if (disciplinaOptional.isPresent()) {
            ModelAndView mv = new ModelAndView("disciplinas/alunos");

            // Recupera e manda os alunos matriculados na disciplina
            mv.addObject("alunos", disciplinaOptional.get().getAlunos());
            mv.addObject("disciplinaId", id);

            return mv;
        }

        return new ModelAndView("redirect:/disciplinas");
    }


    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormDisciplina requisicao) {

        Optional<Disciplina> optionalDisciplina = this.disciplinaRepository.findById(id);

        // Verifica se a disciplina com o id passado existe
        if (optionalDisciplina.isPresent()) {
            ModelAndView mv = new ModelAndView("/disciplinas/edit");

            // Converte um objeto da entidade Disciplina para a classe DTO
            requisicao.fromDisciplina(optionalDisciplina.get());

            // Manda o id da disciplina e os professores disponíveis
            mv.addObject("disciplinaId", id);
            mv.addObject("professores", this.professorRepository.findAll());

            return mv;
        }

        return new ModelAndView("redirect:/disciplinas");
    }


    @PatchMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormDisciplina requisicao, BindingResult result) {

        // Verifica se há erros nos valores passados pelo formulário
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("/disciplinas/edit");

            // Manda o id da disciplina e os professores disponíveis
            mv.addObject("disciplinaId", id);
            mv.addObject("professores", this.professorRepository.findAll());

            return mv;
        }

        Optional<Disciplina> optionalDisciplina = this.disciplinaRepository.findById(id);

        // Verifica se a disciplina com o id passado existe
        if (optionalDisciplina.isPresent()) {
            // Converte um objeto da classe DTO para a entidade Disciplina a partir de uma disciplina válida
            Disciplina disciplina = requisicao.toDisciplina(optionalDisciplina.get(), this.professorRepository);

            // Faz a persistência no banco de dados
            this.disciplinaRepository.save(disciplina);

            return this.retornaSucessoDisciplina("Disciplina #" + id + " atualizada com sucesso!", id);
        }

        return this.retornaErroDisciplina(id);
    }


    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {

        try {
            // Tenta deletar a disciplina com o id passado
            this.disciplinaRepository.deleteById(id);

            return this.retornaSucessoDisciplina("Disciplina #" + id + " deletada com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return this.retornaErroDisciplina(id);
        }
    }


    // Funções para setar os parâmetros de erro ou sucesso ao realizar uma operação

    private ModelAndView retornaErroDisciplina(Long id) {

        ModelAndView mv = new ModelAndView("redirect:/disciplinas");

        mv.addObject("message", "Disciplina #" + id + " não encontrada!");
        mv.addObject("error", true);

        return mv;
    }


    private ModelAndView retornaSucessoDisciplina(String msg) {

        ModelAndView mv = new ModelAndView("redirect:/disciplinas");

        mv.addObject("message", msg);
        mv.addObject("error", false);

        return mv;
    }


    private ModelAndView retornaSucessoDisciplina(String msg, Long id) {

        ModelAndView mv = new ModelAndView("redirect:/disciplinas/" + id);

        mv.addObject("message", msg);
        mv.addObject("error", false);

        return mv;
    }
}
