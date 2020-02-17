package com.hust.baseweb.applications.geo.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import java.util.UUID;

public interface PostalAddressRepo extends PagingAndSortingRepository<PostalAddress, UUID> {

}
