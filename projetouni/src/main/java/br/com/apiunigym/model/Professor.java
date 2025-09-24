package br.com.apiunigym.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
public class Professor implements UserDetails {

    @Id
    @Column(name = "ra", length = 15, nullable = false, unique = true)
    private long ra;

    @Column(name = "nome", length = 100, nullable = true)
    private String nome;

    @Column(name = "sexo", length = 20, nullable = true)
    private char sexo;

    @Column(name = "telefone", length = 20, nullable = true)
    private String telefone;

    @Column(name = "email", length = 100, nullable = true)
    private String email;

    @Column(name = "senha", columnDefinition = "TEXT", length = 50, nullable = true)
    private String senha;

    public long getRa() {
        return ra;
    }
    public void setRa(long ra) {
        this.ra = ra;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public char getSexo() {
        return sexo;
    }
    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    // --- Métodos da interface UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.ra); // ESTA É A ALTERAÇÃO PRINCIPAL
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
