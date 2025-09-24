package br.com.apiunigym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.apiunigym.DAO.IAluno;

@Service
public class AlunoDetailsService implements UserDetailsService {

    @Autowired
    private IAluno alunoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ALTERADO: Agora busca por RA.
        try {
            // Tenta converter o 'username' (que é o RA) para long
            long ra = Long.parseLong(username);

            // Busca o aluno pelo RA usando o repositório
            return alunoRepository.findByRa(ra)
                    .orElseThrow(() -> new UsernameNotFoundException("Aluno não encontrado com o RA: " + username));
        } catch (NumberFormatException e) {
            // Se o RA não for um número válido, lança uma exceção
            throw new UsernameNotFoundException("Formato de RA inválido para login: " + username);
        }
    }
}