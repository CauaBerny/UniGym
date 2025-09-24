package br.com.apiunigym.model; // Use br.com.apiunigym.dto se criar o pacote

public class LoginRequest {
    private String username; // Este campo receberá o RA do frontend (como String)
    private String password; // Este campo receberá a senha do frontend

    // Getters e Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}