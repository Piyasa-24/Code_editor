package com.editor.code.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.editor.code.Model.User;

public interface Userrepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}
