package com.hust.baseweb.applications.schedule.service;

import com.hust.baseweb.applications.schedule.entity.Schedule;
import com.hust.baseweb.applications.schedule.model.ScheduleInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService
{
    Schedule save(ScheduleInputModel input);
    List<Schedule> findAll();
}
