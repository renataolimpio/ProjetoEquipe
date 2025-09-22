package com.uam.services;

import java.util.List;
import com.uam.repositories.UserRepository;
import com.uam.repositories.TeamRepository;
import com.uam.repositories.ProjectRepository;
import com.uam.models.User;
import com.uam.models.Team;
import com.uam.models.Project;

public class TeamService {
    private final TeamRepository repo;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;

    public TeamService(TeamRepository repo, UserRepository userRepo, ProjectRepository projectRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    public Team criarEquipe(String nome, String descricao) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da equipe obrigatório");
        Team t = new Team(nome, descricao);
        repo.save(t);
        return t;
    }

    public void adicionarMembro(Team t, User u) {
        if (t == null || u == null) throw new IllegalArgumentException("Equipe e usuário obrigatórios");

        // Validar se o usuário existe no sistema
        if (userRepo.findById(u.getId()).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado no sistema");
        }

        t.adicionarMembro(u);
    }

    public void associarProjeto(Team t, Project p) {
        if (t == null || p == null) throw new IllegalArgumentException("Equipe e projeto obrigatórios");

        // Validar se o projeto existe no sistema
        if (projectRepo.findById(p.getId()).isEmpty()) {
            throw new IllegalArgumentException("Projeto não encontrado no sistema");
        }

        t.adicionarProjeto(p);
    }
    public List<Team> listarEquipes() { return repo.findAll(); }
}
