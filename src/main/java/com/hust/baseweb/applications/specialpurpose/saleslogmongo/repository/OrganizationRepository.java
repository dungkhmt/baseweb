package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface OrganizationRepository extends MongoRepository<Organization, String> {

}
