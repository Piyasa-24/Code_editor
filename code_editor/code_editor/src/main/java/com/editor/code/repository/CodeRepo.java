package com.editor.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.editor.code.Model.CodeTemplate;

public interface CodeRepo extends JpaRepository<CodeTemplate,Long> {

}
