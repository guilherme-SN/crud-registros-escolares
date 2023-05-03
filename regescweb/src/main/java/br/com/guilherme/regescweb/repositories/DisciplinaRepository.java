package br.com.guilherme.regescweb.repositories;

import br.com.guilherme.regescweb.models.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Repositório de ações para a Disciplina
@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}
