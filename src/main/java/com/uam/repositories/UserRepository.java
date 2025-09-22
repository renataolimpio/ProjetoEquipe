package com.uam.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.uam.models.User;

public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public void save(User u) { users.add(u); }
    public List<User> findAll() { return Collections.unmodifiableList(users); }
    public Optional<User> findByLogin(String login) { return users.stream().filter(u -> u.getLogin().equals(login)).findFirst(); }
    public Optional<User> findById(long id) { return users.stream().filter(u -> u.getId() == id).findFirst(); }
}
