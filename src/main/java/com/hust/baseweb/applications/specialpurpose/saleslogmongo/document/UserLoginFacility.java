package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin cho biet 1 salesman co the ban hang tu kho (facility) nao")
@Document("user_login_facility")
public class UserLoginFacility {

    @Id
    private ObjectId userLoginFacilityId;
    private String userLoginId;// user login of salesman
    private String facilityId;
    private String userLoginFacilityRelationType;
    private Date fromDate;
    private Date thruDate;// null means that the relationship is active
}
