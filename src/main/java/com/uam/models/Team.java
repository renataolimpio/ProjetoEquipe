package com.uam.models;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Team {
    private static long SEQ = 1;
    private final long id;
    private final String nome;
    private final String descricao;
    private final Set<User> membros = new LinkedHashSet<>();
    private final Set<Project> projetosAtuando = new LinkedHashSet<>();

    public Team(String nome, String descricao) {
        this.id = SEQ++;
        this.nome = nome;
        this.descricao = descricao;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Set<User> getMembros() { return Collections.unmodifiableSet(membros); }
    public Set<Project> getProjetosAtuando() { return Collections.unmodifiableSet(projetosAtuando); }

    public void adicionarMembro(User u) { if (u != null) membros.add(u); }
    public void removerMembro(User u) { membros.remove(u); }
    public void adicionarProjeto(Project p) { if (p != null) projetosAtuando.add(p); }

    @Override
    public String toString() { return nome + " (" + membros.size() + " membros, projetos: " + projetosAtuando.size() + ")"; }
}