package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("organizations")
public class Organization {

    @Id
    private String organizationId;
    private String organizationName;
    private String address;
}
