package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NonNull
    private String firstName;

    @NonNull
    private String middleName;

    @NonNull
    private String lastName;

    @NonNull
    private short genderId;

    @NonNull
    private LocalDate birthDate;

    private Date createdAt;
    private Date updatedAt;
}
