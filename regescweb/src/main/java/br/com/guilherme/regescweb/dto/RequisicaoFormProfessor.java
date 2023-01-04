package br.com.guilherme.regescweb.dto;

import br.com.guilherme.regescweb.models.Professor;
import br.com.guilherme.regescweb.models.StatusProfessor;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// Classe DTO (Data Transfer Object) --> garante que não terá inserções "maliciosas"
public class RequisicaoFormProfessor {      // É quem precisa ser validada, pois é essa classe que está recebendo o formulário
    @NotBlank   // Não pode ser nula e o tamanho (sem os espaços do começo e fim) deve ser maior ou igual a 0
    @NotNull    // Pode ser vazia (empty), mas não pode ser nula
    private String nome;
    @NotNull
    @DecimalMin("0")
    private BigDecimal salario;
    private StatusProfessor statusProfessor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusProfessor getStatusProfessor() {
        return statusProfessor;
    }

    public void setStatusProfessor(StatusProfessor statusProfessor) {
        this.statusProfessor = statusProfessor;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    // Função para converter a classe DTO para Entidade
    public Professor toProfessor() {
        return new Professor(this.nome, this.salario, this.statusProfessor);
    }

    // Função para converter a Entidade Professor para a classe DTO
    public void fromProfessor(Professor professor) {
        this.setNome(professor.getNome());
        this.setSalario(professor.getSalario());
        this.setStatusProfessor(professor.getStatusProfessor());
    }

    @Override
    public String toString() {
        return "RequisicaoNovoProfessor{" +
                "nome='" + nome + '\'' +
                ", salario=" + salario +
                ", statusProfessor=" + statusProfessor +
                '}';
    }
}
