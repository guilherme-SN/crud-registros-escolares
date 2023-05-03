package br.com.guilherme.regescweb.models;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;


@Entity
public class Disciplina {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Integer semestre;
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
    @ManyToMany(mappedBy = "disciplinas", fetch = FetchType.EAGER)
    private Set<Aluno> alunos;


    // Métodos especiais

    @Deprecated
    public Disciplina() {}


    public Disciplina(String nome, Integer semestre, Professor professor) {
        this.nome = nome;
        this.semestre = semestre;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

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

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Set<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        this.alunos = alunos;
    }


    // Função de pré-remoção para "desmatricular" os alunos ao remover uma disciplina
    // Esta função é necessária, pois o mapeamento do relacionamento entre Aluno-Disciplina está sendo feito na entidade Aluno, isto é, Disciplina é o non-owning side,
    // logo o Hibernate não permite deletar uma disciplina diretamente
    @PreRemove
    public void removeAlunosOnDelete() {

        for (Aluno aluno : this.getAlunos()) {
            Set<Disciplina> alunoDisciplinas = aluno.getDisciplinas();

            alunoDisciplinas.removeIf(disciplina -> Objects.equals(disciplina.getId(), this.getId()));
        }
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", semestre=" + semestre +
                ", professor=" + professor +
                '}';
    }
}
