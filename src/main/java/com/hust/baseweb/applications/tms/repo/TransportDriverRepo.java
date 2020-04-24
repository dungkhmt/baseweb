package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.document.aggregation.TransportDriver;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportDriverRepo extends MongoRepository<TransportDriver, TransportDriver.Id> {
    List<TransportDriver> findAllByIdIn(List<TransportDriver.Id> ids);

    List<TransportDriver> findAllById_DriverIdAndId_DateBetween(UUID driverId, LocalDate from, LocalDate to);
}
