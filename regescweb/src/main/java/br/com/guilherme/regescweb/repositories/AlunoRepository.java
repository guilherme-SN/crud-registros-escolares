package br.com.guilherme.regescweb.repositories;

import br.com.guilherme.regescweb.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
