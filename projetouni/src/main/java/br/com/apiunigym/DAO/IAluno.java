package br.com.apiunigym.DAO;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.apiunigym.model.Aluno;

public interface IAluno extends CrudRepository<Aluno, Long> {
    Optional<Aluno> findByRa(long ra);
}
