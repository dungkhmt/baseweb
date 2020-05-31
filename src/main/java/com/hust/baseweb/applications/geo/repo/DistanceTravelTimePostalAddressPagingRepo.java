package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.embeddable.DistanceTravelTimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTravelTimePostalAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DistanceTravelTimePostalAddressPagingRepo
    extends PagingAndSortingRepository<DistanceTravelTimePostalAddress, DistanceTravelTimePostalAddressEmbeddableId> {

}
