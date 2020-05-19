package com.hust.baseweb.repo;

import java.util.UUID;

import com.hust.baseweb.entity.Content;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepo extends JpaRepository<Content,UUID>{
    Content findByContentId(UUID id);
}