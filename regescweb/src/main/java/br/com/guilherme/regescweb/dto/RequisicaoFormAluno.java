package br.com.guilherme.regescweb.dto;

import br.com.guilherme.regescweb.models.Aluno;
import br.com.guilherme.regescweb.models.Disciplina;
import br.com.guilherme.regescweb.repositories.DisciplinaRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RequisicaoFormAluno {

    // Atributos
    @NotNull
    @NotBlank
    private String nome;
    @NotNull
    @Min(1)
    private Integer idade;
    private Set<Long> disciplinasId;


    // Métodos especiais

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Set<Long> getDisciplinasId() {
        return disciplinasId;
    }

    public void setDisciplinasId(Set<Long> disciplinasId) {
        this.disciplinasId = disciplinasId;
    }


    // Recupera as disciplinas a partir dos IDs
    public Set<Disciplina> recuperaDisciplinas(DisciplinaRepository disciplinaRepository) {

        if (this.disciplinasId == null) {
            return null;
        }

        Set<Disciplina> disciplinas = new HashSet<Disciplina>();

        for (Long disciplinaId : this.disciplinasId) {
            Optional<Disciplina> optionalDisciplina = disciplinaRepository.findById(disciplinaId);

            optionalDisciplina.ifPresent(disciplinas::add);
        }

        return disciplinas;
    }


    // Recupera os IDs das disciplinas a partir das disciplinas
    public Set<Long> recuperaIdDisciplinas(DisciplinaRepository disciplinaRepository, Set<Disciplina> disciplinas) {

        Set<Long> disciplinasId = new HashSet<Long>();

        for (Disciplina disciplina : disciplinas) {
            disciplinasId.add(disciplina.getId());
        }

        return disciplinasId;
    }


    // Converte um objeto da classe DTO para a entidade Aluno
    public Aluno toAluno(DisciplinaRepository disciplinaRepository) {
        return new Aluno(this.nome, this.idade, this.recuperaDisciplinas(disciplinaRepository));
    }


    // Converte um objeto da classe DTO para a entidade Aluno a partir de um aluno válido
    public Aluno toAluno(Aluno aluno, DisciplinaRepository disciplinaRepository) {

        aluno.setNome(this.nome);
        aluno.setIdade(this.idade);
        aluno.setDisciplinas(this.recuperaDisciplinas(disciplinaRepository));

        return aluno;
    }


    // Converte um objeto da entidade Aluno para a classe DTO
    public void fromAluno(Aluno aluno, DisciplinaRepository disciplinaRepository) {
        this.setNome(aluno.getNome());
        this.setIdade(aluno.getIdade());
        this.setDisciplinasId(this.recuperaIdDisciplinas(disciplinaRepository, aluno.getDisciplinas()));
    }


    @Override
    public String toString() {
        return "RequisicaoFormAluno{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", disciplinasId=" + disciplinasId +
                '}';
    }
}
