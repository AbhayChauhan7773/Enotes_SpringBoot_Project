package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Notes;
import com.becoder.entity.User;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    List<Notes> findByUser(User user);
}
