package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostalAddressJpaRepo extends JpaRepository<PostalAddress, UUID> {
    public List<PostalAddress> findAll();


    //@Query("select u from PostalAddress u where u.address like:a")
    public List<PostalAddress> findTop5ByAddressContaining(String add);

    public List<PostalAddress> findByAddress(String add);

    public PostalAddress findByContactMechId(UUID id);
}
