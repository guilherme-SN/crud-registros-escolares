package br.com.guilherme.regescweb.repositories;

import br.com.guilherme.regescweb.models.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}
