package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class CurrencyUom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private String id;
}
