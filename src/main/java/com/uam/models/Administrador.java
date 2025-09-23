package com.uam.models;

public class Administrador extends Usuario {
    
    public Administrador(String nomeCompleto, String cpf, String email, String cargo, String login, String senha) {
        super(nomeCompleto, cpf, email, cargo, login, senha);
    }

    @Override
    public String getTipoUsuario() {
        return "ADMINISTRADOR";
    }
    
    @Override
    public Role getRole() {
        return Role.ADMINISTRADOR;
    }
    
    @Override
    public boolean podeGerenciarProjetos() {
        return true;
    }
}