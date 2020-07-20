package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {

    @Id
    private ObjectId id;

    private String[] names; // [firstName, midName, lastName]

    private String email;

    private String phone;

    private String address;
}
