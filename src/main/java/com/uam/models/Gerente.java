package com.uam.models;

public class Gerente extends Usuario {
    
    public Gerente(String nomeCompleto, String cpf, String email, String cargo, String login, String senha) {
        super(nomeCompleto, cpf, email, cargo, login, senha);
    }

    @Override
    public String getTipoUsuario() {
        return "GERENTE";
    }
    
    @Override
    public Role getRole() {
        return Role.GERENTE;
    }
    
    @Override
    public boolean podeGerenciarProjetos() {
        return true;
    }
    
}