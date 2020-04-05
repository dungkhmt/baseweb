package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostalAddressJpaRepo extends JpaRepository<PostalAddress, UUID> {
    public List<PostalAddress> findAll();
}
