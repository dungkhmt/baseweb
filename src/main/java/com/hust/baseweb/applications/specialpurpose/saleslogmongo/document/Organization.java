package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Tổ chức giao dịch hàng hóa: nhà cung cấp/khách hàng")
public class Organization {

    @Id
    private String organizationId;
    private String organizationName;
    private String address;
}
