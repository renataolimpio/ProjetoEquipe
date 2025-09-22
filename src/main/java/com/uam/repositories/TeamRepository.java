package com.uam.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.uam.models.Team;
public class TeamRepository {
    private final List<Team> teams = new ArrayList<>();

    public void save(Team t) { teams.add(t); }
    public List<Team> findAll() { return Collections.unmodifiableList(teams); }
    public Optional<Team> findById(long id) { return teams.stream().filter(t -> t.getId() == id).findFirst(); }
}
