package com.hust.baseweb.applications.salesroutes.entity;

import com.hust.baseweb.entity.Party;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class SalesmanCheckinHistory {
    @Id
    @Column(name = "salesman_checkin_history_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID salesmanCheckinHistoryId;

    //@JoinColumn(name="user_login_id", referencedColumnName="user_login_id")
    //@ManyToOne(fetch = FetchType.EAGER)
    //private UserLogin userLogin;

    @Column(name = "user_login_id")
    private String userLoginId;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Party party;

    @Column(name = "check_in_action")
    private String checkinAction;// Y (check-in) or N (check-out)

    @Column(name = "location")
    private String location;

    @Column(name = "time_point")
    private Date timePoint;

}
