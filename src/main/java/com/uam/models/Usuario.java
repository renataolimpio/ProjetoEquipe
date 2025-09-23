package com.uam.models;

public abstract class Usuario {
    private static long SEQ = 1;
    private long id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String cargo;
    private String login;
    private String senha;

    public Usuario() {
        this.id = SEQ++;
    }

    public Usuario(String nomeCompleto, String cpf, String email, String cargo, String login, String senha) {
        this();
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
    }

    // Getters
    public long getId() { return id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    
    // Setters
    public void setId(long id) { this.id = id; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEmail(String email) { this.email = email; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public void setLogin(String login) { this.login = login; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public abstract String getTipoUsuario();
    public abstract Role getRole();
    public abstract boolean podeGerenciarProjetos();

    @Override
    public String toString() { return nomeCompleto + " (" + getTipoUsuario() + ")"; }
}