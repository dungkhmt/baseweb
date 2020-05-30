package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostalAddressJpaRepo extends JpaRepository<PostalAddress, UUID> {

    List<PostalAddress> findAll();


    //@Query("select u from PostalAddress u where u.address like:a")
    List<PostalAddress> findTop5ByAddressContaining(String add);

    List<PostalAddress> findByAddress(String add);

    PostalAddress findByContactMechId(UUID id);
}
