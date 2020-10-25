package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostCustomerRepo extends JpaRepository<PostCustomer, UUID> {

    List<PostCustomer> findAll();

    PostCustomer save(PostCustomer PostCustomer);

    PostCustomer findByPartyId(UUID partyId);

    PostCustomer findByPostCustomerId(UUID id);
}
