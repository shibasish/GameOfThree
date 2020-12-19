package com.takeaway.got.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeaway.got.model.Game;

public interface GameRepo extends JpaRepository<Game, UUID> {
}
