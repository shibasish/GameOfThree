package com.takeaway.got.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeaway.got.model.Player;

public interface PlayerRepo extends JpaRepository<Player, String> {}