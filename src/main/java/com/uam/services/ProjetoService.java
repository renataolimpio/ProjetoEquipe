package com.uam.services;
import java.time.LocalDate;
import java.util.List;

import com.uam.models.ProjectStatus;
import com.uam.models.Projeto;
import com.uam.models.Usuario;
import com.uam.repositories.ProjetoRepository;
import com.uam.repositories.UsuarioRepository;

public class ProjetoService {
    private final ProjetoRepository repo;
    private final UsuarioRepository usuarioRepo;

    public ProjetoService(ProjetoRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    public Projeto criarProjeto(String nome, String descricao, LocalDate dataInicio, LocalDate dataTerminoPrevisto, ProjectStatus status, Usuario gerente) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome do projeto obrigatório");
        if (gerente == null) throw new IllegalArgumentException("Gerente responsável obrigatório");

        // Validar se o gerente existe no sistema
        if (usuarioRepo.findById(gerente.getId()).isEmpty()) {
            throw new IllegalArgumentException("Gerente responsável não encontrado no sistema");
        }

        // Validar se o gerente tem perfil adequado
        if (!gerente.podeGerenciarProjetos()) {
            throw new IllegalArgumentException("Usuário deve ter perfil GERENTE ou ADMINISTRADOR para ser responsável por projeto");
        }

        if (dataInicio != null && dataTerminoPrevisto != null && dataTerminoPrevisto.isBefore(dataInicio))
            throw new IllegalArgumentException("Data término prevista não pode ser antes da data de início");
        Projeto p = new Projeto(nome, descricao, dataInicio, dataTerminoPrevisto, status, gerente);
        repo.save(p);
        return p;
    }

    public List<Projeto> listarProjetos() { return repo.findAll(); }
}