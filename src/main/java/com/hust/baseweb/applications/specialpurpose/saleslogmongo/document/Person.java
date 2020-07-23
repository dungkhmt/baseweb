package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("people")
public class Person {

    @Id
    private ObjectId id;

    private List<String> names; // [firstName, midName, lastName]

    private String email;

    private String phone;

    private String address;
}
