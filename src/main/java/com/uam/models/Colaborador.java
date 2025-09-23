package com.uam.models;

public class Colaborador extends Usuario {
    
    public Colaborador(String nomeCompleto, String cpf, String email, String cargo, String login, String senha) {
        super(nomeCompleto, cpf, email, cargo, login, senha);
    }

    @Override
    public String getTipoUsuario() {
        return "COLABORADOR";
    }
    
    @Override
    public Role getRole() {
        return Role.COLABORADOR;
    }
    
    @Override
    public boolean podeGerenciarProjetos() {
        return false;
    }
    
}