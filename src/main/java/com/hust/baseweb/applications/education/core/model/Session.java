package com.hust.baseweb.applications.education.core.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Session {
    private int index;
    private int startTime;
    private int endTime;
    private ArrayList<Integer> weeks;
    private String location;

    public Session clone(){
    	ArrayList<Integer> W = new ArrayList<Integer>();
    	for(int i : weeks) W.add(i);
    	return new Session(index, startTime, endTime, W, location);
    }
    
	public Session() {
		
	}

    public Session(int index, int startTime, int endTime, ArrayList<Integer> weeks, String location) {
        super();
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weeks = weeks;
        this.setLocation(location);
    }

    public boolean isConflict(Session session) {
        boolean flag = false;

        for (int i : this.weeks) {
            for (int j : session.weeks) {
                if (i == j) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }

        if (flag) {
            if (this.getStartTime() == session.getStartTime()
                    && this.getEndTime() == session.getEndTime()) {
                return true;
            }

            if (isInRange(this.getStartTime(),
                    session.getStartTime(), session.getEndTime())) {
                return true;
            }
            if (isInRange(this.getEndTime(),
                    session.getStartTime(), session.getEndTime())) {
                return true;
            }
            if (isInRange(session.getStartTime(),
                    this.getStartTime(), this.getEndTime())) {
                return true;
            }
            if (isInRange(session.getEndTime(),
                    this.getStartTime(), this.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    public boolean isInRange(int p, int s, int e) {
        if (p > s && p < e) {
            return true;
        } else {
            return false;
        }
    }
}
