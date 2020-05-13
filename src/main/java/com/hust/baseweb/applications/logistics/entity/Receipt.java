package com.hust.baseweb.applications.logistics.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Receipt {

    @Id
    private String receiptId;
    private Date receiptDate;

    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Facility facility;

    @NotNull
    public static String convertSequenceIdToReceiptId(Long id) {
        return "RCP" + String.format("%010d", id);
    }
}
