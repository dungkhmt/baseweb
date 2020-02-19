package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PostalAddressRepo extends PagingAndSortingRepository<PostalAddress, UUID> {

}
