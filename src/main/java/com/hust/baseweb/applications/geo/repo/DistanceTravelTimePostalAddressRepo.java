package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.embeddable.DistanceTravelTimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTravelTimePostalAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DistanceTravelTimePostalAddressRepo extends CrudRepository<DistanceTravelTimePostalAddress, DistanceTravelTimePostalAddressEmbeddableId> {
    DistanceTravelTimePostalAddress findByDistanceTravelTimePostalAddressEmbeddableId(
        DistanceTravelTimePostalAddressEmbeddableId distanceTravelTimePostalAddressEmbeddableId);

    List<DistanceTravelTimePostalAddress> findAllByDistanceTravelTimePostalAddressEmbeddableId_FromContactMechIdInAndDistanceTravelTimePostalAddressEmbeddableId_ToContactMechIdIn(
        List<UUID> froms,
        List<UUID> tos);
}
