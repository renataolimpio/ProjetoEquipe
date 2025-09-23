package com.uam.services;
import java.util.List;

import com.uam.models.Administrador;
import com.uam.models.Colaborador;
import com.uam.models.Gerente;
import com.uam.models.Role;
import com.uam.models.Usuario;
import com.uam.repositories.UsuarioRepository;

public class UserService {
    private final UsuarioRepository repo;

    public UserService(UsuarioRepository repo) { this.repo = repo; }

    public Usuario criarUsuario(String nome, String cpf, String email, String cargo, String login, String senha, Role perfil) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obrigatório");
        if (cpf == null || cpf.isBlank()) throw new IllegalArgumentException("CPF obrigatório");
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Login obrigatório");
        if (repo.findByLogin(login).isPresent()) throw new IllegalArgumentException("Login já existe");
        
        Usuario u = switch (perfil) {
            case ADMINISTRADOR -> new Administrador(nome, cpf, email, cargo, login, senha);
            case GERENTE -> new Gerente(nome, cpf, email, cargo, login, senha);
            case COLABORADOR -> new Colaborador(nome, cpf, email, cargo, login, senha);
        };
        
        repo.save(u);
        return u;
    }

    public List<Usuario> listarUsuarios() { return repo.findAll(); }
}

