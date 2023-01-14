package br.com.guilherme.regescweb.repositories;

import br.com.guilherme.regescweb.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Repositório de ações para o Professor
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
