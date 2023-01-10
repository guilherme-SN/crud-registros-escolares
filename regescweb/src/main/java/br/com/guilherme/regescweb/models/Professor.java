package br.com.guilherme.regescweb.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private BigDecimal salario;
    @Enumerated(EnumType.STRING)    // Explicita que é um Enum e trata as constantes como String
    private StatusProfessor statusProfessor;

    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    private Set<Disciplina> disciplinas;

    @Deprecated
    public Professor() { }

    public Professor(String nome, BigDecimal salario, StatusProfessor statusProfessor) {
        this.nome = nome;
        this.salario = salario;
        this.statusProfessor = statusProfessor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public StatusProfessor getStatusProfessor() {
        return statusProfessor;
    }

    public void setStatusProfessor(StatusProfessor statusProfessor) {
        this.statusProfessor = statusProfessor;
    }


    @PreRemove
    public void atualizaDisciplinaOnRemove() {
        for (Disciplina disciplina : this.getDisciplinas()) {
            disciplina.setProfessor(null);
        }
    }

    public Set<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(Set<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
