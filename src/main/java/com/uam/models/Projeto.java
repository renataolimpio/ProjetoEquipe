package com.uam.models;

import java.time.LocalDate;

public class Projeto {
    private static long SEQ = 1;
    private long id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataTerminoPrevisto;
    private ProjectStatus status;
    private Usuario gerenteResponsavel;

    public Projeto() {
        this.id = SEQ++;
    }

    public Projeto(String nome, String descricao, LocalDate dataInicio, LocalDate dataTerminoPrevisto, ProjectStatus status, Usuario gerenteResponsavel) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevisto = dataTerminoPrevisto;
        this.status = status;
        this.gerenteResponsavel = gerenteResponsavel;
    }

    // Getters
    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataTerminoPrevisto() { return dataTerminoPrevisto; }
    public ProjectStatus getStatus() { return status; }
    public Usuario getGerenteResponsavel() { return gerenteResponsavel; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public void setDataTerminoPrevisto(LocalDate dataTerminoPrevisto) { this.dataTerminoPrevisto = dataTerminoPrevisto; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public void setGerenteResponsavel(Usuario gerenteResponsavel) { this.gerenteResponsavel = gerenteResponsavel; }

    @Override
    public String toString() {
        return nome + " [" + status + "] - Gerente: " + (gerenteResponsavel != null ? gerenteResponsavel.getNomeCompleto() : "-");
    }
}