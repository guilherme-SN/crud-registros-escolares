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
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public DisciplinaController(DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
    }

    @GetMapping("")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("/disciplinas/index");
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

        if (result.hasErrors()) {
            return this.retornaErroDisciplina("CREATE ERROR: erro ao criar disciplina!");
        }

        Disciplina disciplina = requisicao.toDisciplina(this.professorRepository);

        this.disciplinaRepository.save(disciplina);

        return this.retornaSucessoDisciplina("Disciplina criada com sucesso!");
    }


    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {

        ModelAndView mv = new ModelAndView();
        Optional<Disciplina> optionalDisciplina = this.disciplinaRepository.findById(id);

        if (optionalDisciplina.isPresent()) {
            Disciplina disciplina = optionalDisciplina.get();

            mv.setViewName("/disciplinas/show");
            mv.addObject("disciplinaId", id);
            mv.addObject("disciplina", disciplina);
        } else {
            mv.setViewName("redirect:/disciplinas");
        }

        return mv;
    }


    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormDisciplina requisicao) {

        ModelAndView mv = new ModelAndView();
        Optional<Disciplina> optionalDisciplina = this.disciplinaRepository.findById(id);

        if (optionalDisciplina.isPresent()) {
            Disciplina disciplina = optionalDisciplina.get();

            requisicao.fromDisciplina(disciplina);

            mv.setViewName("/disciplinas/edit");
            mv.addObject("disciplinaId", id);
            mv.addObject("professores", this.professorRepository.findAll());
        } else {
            mv.setViewName("redirect:/disciplinas");
        }

        return mv;
    }


    @PatchMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormDisciplina requisicao, BindingResult result) {

        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("/disciplinas/edit");

            mv.addObject("disciplinaId", id);
            mv.addObject("professores", this.professorRepository.findAll());

            return mv;
        }

        Optional<Disciplina> optionalDisciplina = this.disciplinaRepository.findById(id);

        if (optionalDisciplina.isPresent()) {
            Disciplina disciplina = requisicao.toDisciplina(optionalDisciplina.get(), this.professorRepository);

            this.disciplinaRepository.save(disciplina);

            return this.retornaSucessoDisciplina("Disciplina #" + id + " atualizada com sucesso!", id);
        }

        return this.retornaErroDisciplina(id);
    }


    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {

        ModelAndView mv = new ModelAndView();

        try {
            this.disciplinaRepository.deleteById(id);

            mv = this.retornaSucessoDisciplina("Disciplina #" + id + " deletada com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());

            mv = this.retornaErroDisciplina(id);
        }

        return mv;
    }


    private ModelAndView retornaErroDisciplina(Long id) {

        ModelAndView mv = new ModelAndView("redirect:/disciplinas");

        mv.addObject("message", "Disciplina #" + id + " não encontrada!");
        mv.addObject("error", true);

        return mv;
    }


    private ModelAndView retornaErroDisciplina(String msg) {

        ModelAndView mv = new ModelAndView("redirect:/disciplinas");

        mv.addObject("message", msg);
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
