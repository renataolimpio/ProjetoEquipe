package com.uam.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.uam.models.Project;

public class ProjectRepository {
    private final List<Project> projects = new ArrayList<>();

    public void save(Project p) { projects.add(p); }
    public List<Project> findAll() { return Collections.unmodifiableList(projects); }
    public Optional<Project> findById(long id) { return projects.stream().filter(p -> p.getId() == id).findFirst(); }
}
