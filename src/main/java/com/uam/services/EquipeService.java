package com.uam.services;

import java.util.List;

import com.uam.models.Equipe;
import com.uam.models.Projeto;
import com.uam.models.Usuario;
import com.uam.repositories.EquipeRepository;
import com.uam.repositories.ProjetoRepository;
import com.uam.repositories.UsuarioRepository;

public class EquipeService {
    private final EquipeRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final ProjetoRepository projetoRepo;

    public EquipeService(EquipeRepository repo, UsuarioRepository usuarioRepo, ProjetoRepository projetoRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
        this.projetoRepo = projetoRepo;
    }

    public Equipe criarEquipe(String nome, String descricao) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da equipe obrigatório");
        Equipe t = new Equipe(nome, descricao);
        repo.save(t);
        return t;
    }

    public void adicionarMembro(Equipe t, Usuario u) {
        if (t == null || u == null) throw new IllegalArgumentException("Equipe e usuário obrigatórios");

        // Validar se o usuário existe no sistema
        if (usuarioRepo.findById(u.getId()).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado no sistema");
        }

        t.adicionarMembro(u);
    }

    public void associarProjeto(Equipe t, Projeto p) {
        if (t == null || p == null) throw new IllegalArgumentException("Equipe e projeto obrigatórios");

        // Validar se o projeto existe no sistema
        if (projetoRepo.findById(p.getId()).isEmpty()) {
            throw new IllegalArgumentException("Projeto não encontrado no sistema");
        }

        t.adicionarProjeto(p);
    }
    public List<Equipe> listarEquipes() { return repo.findAll(); }
}