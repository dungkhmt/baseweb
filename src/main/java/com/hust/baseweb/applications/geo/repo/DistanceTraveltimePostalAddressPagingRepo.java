package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.embeddable.DistanceTraveltimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTraveltimePostalAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DistanceTraveltimePostalAddressPagingRepo extends PagingAndSortingRepository<DistanceTraveltimePostalAddress, DistanceTraveltimePostalAddressEmbeddableId> {

}
