package com.uam.models;

import java.time.LocalDate;
import com.uam.models.ProjectStatus;

public class Project {
    private static long SEQ = 1;
    private final long id;
    private final String nome;
    private final String descricao;
    private final LocalDate dataInicio;
    private final LocalDate dataTerminoPrevisto;
    private final ProjectStatus status;
    private final User gerenteResponsavel;

    public Project(String nome, String descricao, LocalDate dataInicio, LocalDate dataTerminoPrevisto, ProjectStatus status, User gerenteResponsavel) {
        this.id = SEQ++;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevisto = dataTerminoPrevisto;
        this.status = status;
        this.gerenteResponsavel = gerenteResponsavel;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataTerminoPrevisto() { return dataTerminoPrevisto; }
    public ProjectStatus getStatus() { return status; }
    public User getGerenteResponsavel() { return gerenteResponsavel; }

    @Override
    public String toString() {
        return nome + " [" + status + "] - Gerente: " + (gerenteResponsavel != null ? gerenteResponsavel.getNomeCompleto() : "-");
    }
}