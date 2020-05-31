package com.hust.baseweb.applications.geo.embeddable;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class DistanceTravelTimePostalAddressEmbeddableId implements Serializable {

    @NotNull
    private UUID fromContactMechId;

    @NotNull
    private UUID toContactMechId;

}
