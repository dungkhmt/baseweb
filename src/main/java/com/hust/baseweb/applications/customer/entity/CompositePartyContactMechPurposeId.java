package com.hust.baseweb.applications.customer.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class CompositePartyContactMechPurposeId implements Serializable {
    private UUID partyId;
    private UUID contactMechId;
    private String contactMechPurposeTypeId;
    private Date fromDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompositePartyContactMechPurposeId Id1 = (CompositePartyContactMechPurposeId) o;
        if (partyId != Id1.partyId) {
            return false;
        }
        if (contactMechId != Id1.contactMechId) {
            return false;
        }
        if (!contactMechPurposeTypeId.equals(Id1.contactMechPurposeTypeId)) {
            return false;
        }
        return fromDate.equals(Id1.fromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, contactMechId, contactMechPurposeTypeId, fromDate);
    }
}
