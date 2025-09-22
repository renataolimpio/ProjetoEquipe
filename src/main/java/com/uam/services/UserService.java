package com.uam.services;
import com.uam.repositories.UserRepository;
import com.uam.repositories.TeamRepository;
import com.uam.repositories.ProjectRepository;
import com.uam.models.User;
import com.uam.models.Team;
import com.uam.models.Role;
import com.uam.models.Project;

import java.util.List;

public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    public User criarUsuario(String nome, String cpf, String email, String cargo, String login, String senha, Role perfil) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obrigatório");
        if (cpf == null || cpf.isBlank()) throw new IllegalArgumentException("CPF obrigatório");
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Login obrigatório");
        if (repo.findByLogin(login).isPresent()) throw new IllegalArgumentException("Login já existe");
        User u = new User(nome, cpf, email, cargo, login, senha, perfil);
        repo.save(u);
        return u;
    }

    public List<User> listarUsuarios() { return repo.findAll(); }
}

