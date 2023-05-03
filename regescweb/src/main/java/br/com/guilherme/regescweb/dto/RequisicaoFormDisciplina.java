package br.com.guilherme.regescweb.dto;

import br.com.guilherme.regescweb.models.Disciplina;
import br.com.guilherme.regescweb.models.Professor;
import br.com.guilherme.regescweb.repositories.ProfessorRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;


// Classe DTO
public class RequisicaoFormDisciplina {

    // Atributos
    @NotNull
    @NotBlank
    private String nome;
    @NotNull
    @Min(1)
    private Integer semestre;
    private Long professorId;


    // Métodos especiais

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }


    // Converte um objeto da classe DTO para a entidade Professor
    public Professor toProfessor(ProfessorRepository professorRepository) {
        Optional<Professor> optionalProfessor = professorRepository.findById(this.professorId);

        return optionalProfessor.orElse(null);
    }


    // Converte um objeto da classe DTO para a entidade Disciplina
    public Disciplina toDisciplina(ProfessorRepository professorRepository) {

        Optional<Professor> optionalProfessor = professorRepository.findById(this.professorId);

        if (optionalProfessor.isPresent()) {
            return new Disciplina(this.nome, this.semestre, this.toProfessor(professorRepository));
        }

        return null;
    }


    // Converte um objeto da classe DTO para a entidade Disciplina a partir de uma disciplina válida
    public Disciplina toDisciplina(Disciplina disciplina, ProfessorRepository professorRepository) {

        disciplina.setNome(this.nome);
        disciplina.setSemestre(this.semestre);
        disciplina.setProfessor(this.toProfessor(professorRepository));

        return disciplina;
    }


    // Converte um objeto da entidade Disciplina para a classe DTO
    public void fromDisciplina(Disciplina disciplina) {
        this.setNome(disciplina.getNome());
        this.setSemestre(disciplina.getSemestre());
    }


    @Override
    public String toString() {
        return "RequisicaoFormDisciplina{" +
                "nome='" + nome + '\'' +
                ", semestre=" + semestre +
                ", professor=" + professorId +
                '}';
    }
}
