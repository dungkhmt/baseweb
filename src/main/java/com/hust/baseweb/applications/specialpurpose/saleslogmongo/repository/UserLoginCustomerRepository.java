package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.UserLoginCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface UserLoginCustomerRepository extends MongoRepository<UserLoginCustomer, String> {
    List<UserLoginCustomer> findAllByUserLoginIdAndUserLoginOrganizationRelationTypeAndThruDate(String userLoginId,
                                                                                                String userLoginOrganizationRelationType, Date thruDate);
}
