package com.uam.models;

import com.uam.models.Role;
public class User {
    private static long SEQ = 1;
    private final long id;
    private final String nomeCompleto;
    private final String cpf;
    private final String email;
    private final String cargo;
    private final String login;
    private final String senha;
    private final Role perfil;

    public User(String nomeCompleto, String cpf, String email, String cargo, String login, String senha, Role perfil) {
        this.id = SEQ++;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public long getId() { return id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public Role getPerfil() { return perfil; }

    @Override
    public String toString() { return nomeCompleto + " (" + perfil + ")"; }
}
