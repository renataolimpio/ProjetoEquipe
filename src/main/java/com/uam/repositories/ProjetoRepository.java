package com.uam.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.uam.models.Projeto;

public class ProjetoRepository {
    private final List<Projeto> projetos = new ArrayList<>();

    public void save(Projeto p) { projetos.add(p); }
    public List<Projeto> findAll() { return Collections.unmodifiableList(projetos); }
    public Optional<Projeto> findById(long id) { return projetos.stream().filter(p -> p.getId() == id).findFirst(); }
}