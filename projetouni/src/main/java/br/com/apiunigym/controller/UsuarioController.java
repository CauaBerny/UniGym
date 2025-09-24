package br.com.apiunigym.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apiunigym.DAO.IAluno;
import br.com.apiunigym.model.Aluno;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/alunos")
public class UsuarioController {

    @Autowired
    private IAluno dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Aluno> listaAlunos () {
        return (List<Aluno>) dao.findAll();
    }

    @GetMapping("/{ra}")
    public ResponseEntity<Aluno> getAlunoPorRa(@PathVariable long ra) {
    // Procura o aluno no banco de dados pelo RA fornecido na URL
    Optional<Aluno> alunoOptional = dao.findById(ra);

    // Verifica se o aluno foi encontrado
    if (alunoOptional.isPresent()) {
        // Se encontrou, retorna o aluno com status 200 OK
        return ResponseEntity.ok(alunoOptional.get());
    } else {
        // Se não encontrou, retorna status 404 Not Found
        return ResponseEntity.notFound().build();
    }
}

    @PostMapping
    public ResponseEntity<Aluno> cadastrarAluno (@RequestBody Aluno aluno) {
        String senhaCriptografada = passwordEncoder.encode(aluno.getSenha());
        aluno.setSenha(senhaCriptografada);
        aluno.setAtivo(false);
        Aluno alunoNovo = dao.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoNovo);
    }

    @PutMapping("/{ra}" )
    public ResponseEntity<Aluno> editarAluno (@RequestBody Aluno aluno) {
        Optional<Aluno> alunoOptional = dao.findById(aluno.getRa());
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Aluno alunoExistente = alunoOptional.get();
        // Atualiza apenas os campos que podem ser alterados
        alunoExistente.setNome(aluno.getNome());
        alunoExistente.setSexo(aluno.getSexo());
        alunoExistente.setTelefone(aluno.getTelefone());
        alunoExistente.setEmail(aluno.getEmail());
        alunoExistente.setDatanascimento(aluno.getDatanascimento());
        alunoExistente.setRaProf(aluno.getRaProf());
        alunoExistente.setAtivo(aluno.isAtivo());

        if (aluno.getSenha() != null && !aluno.getSenha().isEmpty()) {
            // Apenas criptografa se uma nova senha for fornecida
            alunoExistente.setSenha(passwordEncoder.encode(aluno.getSenha()));
        }

        Aluno alunoNovo = dao.save(alunoExistente);
        return ResponseEntity.ok(alunoNovo);
    }

    @DeleteMapping("/{ra}")
    public ResponseEntity<Void> deletarAluno (@PathVariable long ra) {
        if (!dao.existsById(ra)) { // Verifica se o aluno existe antes de tentar deletar
            return ResponseEntity.notFound().build(); // Retorna 404 se não existir
        }
        dao.deleteById(ra);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso sem retorno de corpo
    }
}


