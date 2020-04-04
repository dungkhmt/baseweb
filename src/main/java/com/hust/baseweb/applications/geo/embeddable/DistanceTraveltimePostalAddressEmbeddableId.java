package com.hust.baseweb.applications.geo.embeddable;

import lombok.*;

import javax.validation.constraints.NotNull;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class DistanceTraveltimePostalAddressEmbeddable {
    @NotNull
    private UUID fromContactMechId;

    @NotNull
    private UUID toContactMechId;

}
