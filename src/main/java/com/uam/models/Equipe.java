package com.uam.models;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Equipe {
    private static long SEQ = 1;
    private long id;
    private String nome;
    private String descricao;
    private final Set<Usuario> membros = new LinkedHashSet<>();
    private final Set<Projeto> projetosAtuando = new LinkedHashSet<>();

    public Equipe() {
        this.id = SEQ++;
    }

    public Equipe(String nome, String descricao) {
        this();
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters
    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Set<Usuario> getMembros() { return Collections.unmodifiableSet(membros); }
    public Set<Projeto> getProjetosAtuando() { return Collections.unmodifiableSet(projetosAtuando); }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setMembros(Set<Usuario> membros) { 
        this.membros.clear();
        if (membros != null) {
            this.membros.addAll(membros);
        }
    }
    public void setProjetosAtuando(Set<Projeto> projetosAtuando) { 
        this.projetosAtuando.clear();
        if (projetosAtuando != null) {
            this.projetosAtuando.addAll(projetosAtuando);
        }
    }

    // Métodos de manipulação
    public void adicionarMembro(Usuario u) { if (u != null) membros.add(u); }
    public void removerMembro(Usuario u) { membros.remove(u); }
    public void adicionarProjeto(Projeto p) { if (p != null) projetosAtuando.add(p); }
    public void removerProjeto(Projeto p) { projetosAtuando.remove(p); }

    @Override
    public String toString() { return nome + " (" + membros.size() + " membros, projetos: " + projetosAtuando.size() + ")"; }
}