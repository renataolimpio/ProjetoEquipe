package com.uam.services;
import com.uam.repositories.UserRepository;
import com.uam.repositories.TeamRepository;
import com.uam.repositories.ProjectRepository;
import com.uam.models.User;
import com.uam.models.Team;
import com.uam.models.Project;
import com.uam.models.ProjectStatus;
import com.uam.models.Role;
import java.time.LocalDate;
import java.util.List;

public class ProjectService {
    private final ProjectRepository repo;
    private final UserRepository userRepo;

    public ProjectService(ProjectRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public Project criarProjeto(String nome, String descricao, LocalDate dataInicio, LocalDate dataTerminoPrevisto, ProjectStatus status, User gerente) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome do projeto obrigatório");
        if (gerente == null) throw new IllegalArgumentException("Gerente responsável obrigatório");

        // Validar se o gerente existe no sistema
        if (userRepo.findById(gerente.getId()).isEmpty()) {
            throw new IllegalArgumentException("Gerente responsável não encontrado no sistema");
        }

        // Validar se o gerente tem perfil adequado
        if (gerente.getPerfil() != Role.GERENTE && gerente.getPerfil() != Role.ADMINISTRADOR) {
            throw new IllegalArgumentException("Usuário deve ter perfil GERENTE ou ADMINISTRADOR para ser responsável por projeto");
        }

        if (dataInicio != null && dataTerminoPrevisto != null && dataTerminoPrevisto.isBefore(dataInicio))
            throw new IllegalArgumentException("Data término prevista não pode ser antes da data de início");
        Project p = new Project(nome, descricao, dataInicio, dataTerminoPrevisto, status, gerente);
        repo.save(p);
        return p;
    }

    public List<Project> listarProjetos() { return repo.findAll(); }
}
