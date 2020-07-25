package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.UserLoginFacility;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface UserLoginFacilityRepository extends MongoRepository<UserLoginFacility, ObjectId> {
    List<UserLoginFacility> findByUserLoginIdAndUserLoginFacilityRelationTypeAndThruDate(String userLoginId,
                                                                                         String userLoginFacilityRelationType,
                                                                                         Date thruDate);
}
