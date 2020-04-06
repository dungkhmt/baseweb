package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.embeddable.DistanceTravelTimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTravelTimePostalAddress;
import org.springframework.data.repository.CrudRepository;

public interface DistanceTravelTimePostalAddressRepo extends CrudRepository<DistanceTravelTimePostalAddress, DistanceTravelTimePostalAddressEmbeddableId> {
    DistanceTravelTimePostalAddress findByDistanceTravelTimePostalAddressEmbeddableId(
            DistanceTravelTimePostalAddressEmbeddableId distanceTraveltimePostalAddressEmbeddableId);

}
