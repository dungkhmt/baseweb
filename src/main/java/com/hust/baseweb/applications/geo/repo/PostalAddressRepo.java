package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PostalAddressRepo extends PagingAndSortingRepository<PostalAddress, UUID> {

    List<PostalAddress> findAllByLocationCodeIn(List<String> locationCodes);

    PostalAddress findByContactMechId(UUID contactMechId);

    List<PostalAddress> findAllByLocationCode(String locationCode);
}
