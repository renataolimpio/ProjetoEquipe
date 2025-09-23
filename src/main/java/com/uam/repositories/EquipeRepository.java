package com.uam.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.uam.models.Equipe;

public class EquipeRepository {
    private final List<Equipe> equipes = new ArrayList<>();

    public void save(Equipe t) { equipes.add(t); }
    public List<Equipe> findAll() { return Collections.unmodifiableList(equipes); }
    public Optional<Equipe> findById(long id) { return equipes.stream().filter(t -> t.getId() == id).findFirst(); }
}