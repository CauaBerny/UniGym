package br.com.apiunigym.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apiunigym.DAO.IAluno;
import br.com.apiunigym.model.Aluno;
import br.com.apiunigym.model.LoginRequest;

@RestController
@CrossOrigin(allowedHeaders = "*", originPatterns = "*")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IAluno alunoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/alunos")
    public ResponseEntity<String> loginAluno(@RequestBody LoginRequest loginRequest) {
        Optional<Aluno> alunoOptional;

        // Tenta converter o 'username' (RA) para long
        try {
            long ra = Long.parseLong(loginRequest.getUsername());
            alunoOptional = alunoRepository.findByRa(ra); // Busca o aluno pelo RA
        } catch (NumberFormatException e) {
            // Se o RA não for um número válido, retorna BAD_REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de RA inválido.");
        }

        // Verifica se o aluno foi encontrado
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas: RA ou senha incorretos.");
        }

        Aluno aluno = alunoOptional.get();

        // 1. Verificar se a conta está ativa
        if (!aluno.isEnabled()) { // O método isEnabled() vem de UserDetails e usa o campo 'ativo'
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sua conta está inativa. Aguarde a ativação.");
        }

        // 2. Comparar a senha usando o PasswordEncoder
        if (passwordEncoder.matches(loginRequest.getPassword(), aluno.getPassword())) {
            // Senha correta
            return ResponseEntity.ok("Login bem-sucedido! Bem-vindo, " + aluno.getNome() + "!");
        } else {
            // Senha incorreta
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas: RA ou senha incorretos.");
        }
    }
}