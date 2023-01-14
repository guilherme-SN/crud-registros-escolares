package br.com.guilherme.regescweb.repositories;

import br.com.guilherme.regescweb.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Repositório de ações para o Aluno
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
