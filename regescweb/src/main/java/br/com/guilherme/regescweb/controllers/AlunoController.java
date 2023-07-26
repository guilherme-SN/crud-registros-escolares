package br.com.guilherme.regescweb.controllers;


import br.com.guilherme.regescweb.dto.RequisicaoFormAluno;
import br.com.guilherme.regescweb.models.Aluno;
import br.com.guilherme.regescweb.repositories.AlunoRepository;
import br.com.guilherme.regescweb.repositories.DisciplinaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping(value = "/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;


    // Injeção de Dependência
    @Autowired
    public AlunoController(AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository) {
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }


    @GetMapping("")
    public ModelAndView index() {

        ModelAndView mv = new ModelAndView("alunos/index");
        mv.addObject("alunos", this.alunoRepository.findAll());

        return mv;
    }


    @GetMapping("/new")
    public ModelAndView newAluno(RequisicaoFormAluno requisicao) {

        ModelAndView mv = new ModelAndView("alunos/new");
        mv.addObject("disciplinas", this.disciplinaRepository.findAll());

        return mv;
    }


    @PostMapping("")
    public ModelAndView create(@Valid RequisicaoFormAluno requisicao, BindingResult result) {

        // Verifica se há erros nos valores enviados pelo formulário
        if (result.hasErrors()) {
            return new ModelAndView("/alunos/new");
        }

        // Converte um objeto da classe DTO para a classe entidade
        Aluno aluno = requisicao.toAluno(this.disciplinaRepository);

        // Faz a persistência no banco de dados
        this.alunoRepository.save(aluno);

        return new ModelAndView("redirect:/alunos/" + aluno.getId());
    }


    @GetMapping("/{id}")
    public ModelAndView showAlunos(@PathVariable Long id) {

        Optional<Aluno> optionalAluno = this.alunoRepository.findById(id);

        // Verifica se o aluno com o id passado existe
        if (optionalAluno.isPresent()) {
            ModelAndView mv = new ModelAndView("alunos/show");

            // Manda o objeto aluno para o Thymeleaf
            mv.addObject("aluno", optionalAluno.get());

            return mv;
        }

        return new ModelAndView("redirect:/alunos");
    }


    @GetMapping("{id}/disciplinas")
    public ModelAndView showDisciplinas(@PathVariable Long id) {

        Optional<Aluno> optionalAluno = this.alunoRepository.findById(id);

        // Verifica se o aluno com o id passado existe
        if (optionalAluno.isPresent()) {
            Aluno aluno = optionalAluno.get();

            ModelAndView mv = new ModelAndView("alunos/disciplinas");

            // Manda o id do aluno e suas disciplinas para o Thymeleaf
            mv.addObject("alunoId", id);
            mv.addObject("disciplinas", aluno.getDisciplinas());

            return mv;
        }

        return new ModelAndView("redirect:/alunos");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormAluno requisicao) {

        Optional<Aluno> optionalAluno = this.alunoRepository.findById(id);

        // Verifica se o aluno com o id passado existe
        if (optionalAluno.isPresent()) {
            // Converte um objeto da entidade Aluno para a classe DTO
            requisicao.fromAluno(optionalAluno.get(), this.disciplinaRepository);

            ModelAndView mv = new ModelAndView("alunos/edit");

            // Manda o id do aluno e as disciplinas disponíveis
            mv.addObject("alunoId", id);
            mv.addObject("disciplinas", this.disciplinaRepository.findAll());

            return mv;
        }

        return new ModelAndView("redirect:/alunos");
    }


    @PatchMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, RequisicaoFormAluno requisicao, BindingResult result) {

        // Verifica se há erros nos valores enviados pelo formulário
        if (result.hasErrors()) {
            return new ModelAndView("/alunos/" + id + "/edit");
        }

        Optional<Aluno> optionalAluno = this.alunoRepository.findById(id);

        // Verifica se o aluno com o id passado existe
        if (optionalAluno.isPresent()) {
            // Atualiza os dados do aluno a partir dos dados da classe DTO recebida pelo formulário
            Aluno aluno = requisicao.toAluno(optionalAluno.get(), this.disciplinaRepository);

            // Faz a persistência no banco de dados
            this.alunoRepository.save(aluno);

            ModelAndView mv =  new ModelAndView("alunos/show");
            mv.addObject("aluno", aluno);

            return mv;
        }

        return new ModelAndView("redirect:/alunos");
    }


    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {

        try {
            // Tenta deletar o objeto aluno com o id passado
            this.alunoRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ModelAndView("redirect:/alunos");
    }
}
