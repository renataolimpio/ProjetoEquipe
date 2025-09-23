package com.uam.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.uam.models.Usuario;

public class UsuarioRepository {
    private final List<Usuario> usuarios = new ArrayList<>();

    public void save(Usuario u) { usuarios.add(u); }
    public List<Usuario> findAll() { return Collections.unmodifiableList(usuarios); }
    public Optional<Usuario> findByLogin(String login) { return usuarios.stream().filter(u -> u.getLogin().equals(login)).findFirst(); }
    public Optional<Usuario> findById(long id) { return usuarios.stream().filter(u -> u.getId() == id).findFirst(); }
}