package com.hust.baseweb.applications.logistics.entity;

import com.hust.baseweb.entity.RoleType;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FacilityRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID facilityRoleId;

    @JoinColumn(referencedColumnName = "user_login_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserLogin userLogin;

    @JoinColumn(referencedColumnName = "facility_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Facility facility;

    @JoinColumn(referencedColumnName = "role_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private RoleType roleType;

    private Date fromDate;
    private Date thruDate;

    public ApiOutputModel toOutputApiModel(String userLoginName) {
        return new ApiOutputModel(
            facilityRoleId.toString(),
            Optional.ofNullable(userLogin).map(UserLogin::getUserLoginId).orElse(null),
            userLoginName,
            Optional.ofNullable(facility).map(Facility::getFacilityId).orElse(null),
            Optional.ofNullable(roleType).map(RoleType::getRoleTypeId).orElse(null),
            Constant.DATE_FORMAT.format(fromDate),
            Constant.DATE_FORMAT.format(thruDate)
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ApiInputModel {

        private String userLoginId;
        private String facilityId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ApiOutputModel {

        private String facilityRoleId;
        private String userLoginId;
        private String userLoginName;
        private String facilityId;
        private String roleTypeId;
        private String fromDate;
        private String thruDate;
    }
}
