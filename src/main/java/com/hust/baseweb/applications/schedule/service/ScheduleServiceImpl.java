package com.hust.baseweb.applications.schedule.service;

import com.hust.baseweb.applications.schedule.entity.Schedule;
import com.hust.baseweb.applications.schedule.model.ScheduleInputModel;
import com.hust.baseweb.applications.schedule.repo.ScheduleRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ScheduleServiceImpl implements ScheduleService
{

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Override
    public Schedule save(ScheduleInputModel input)
    {
        Schedule schedule = new Schedule();

        schedule.setCid(input.getCid());
        schedule.setAcid(input.getAcid());
        schedule.setCourse_code(input.getCourse_code());
        schedule.setCourse_name(input.getCourse_name());
        schedule.setCapacity(input.getCapacity());
        schedule.setNote(input.getNote());
        schedule.setCourse_week(input.getCourse_week());
        schedule.setDay_of_week(input.getDay_of_week());
        schedule.setCourse_time(input.getCourse_time());
        schedule.setStart_section(input.getStart_section());
        schedule.setFinish_section(input.getFinish_section());
        schedule.setShift(input.getShift());
        schedule.setRoom(input.getRoom());
        schedule.setType_of_class(input.getType_of_class());
        schedule.setTotal(input.getTotal());
        schedule.setCourse_session(input.getCourse_session());
        schedule.setState(input.getState());
        schedule.setExperiment(input.getExperiment());
        schedule.setMid(input.getMid());
        schedule.setCollege(input.getCollege());
        schedule.setCourse_period(input.getCourse_period());

        scheduleRepo.save(schedule);

        return null;
    }

    @Override
    public List<Schedule> findAll()
    {
        List<Schedule> schedulelist = scheduleRepo.findAll();
        return schedulelist;
    }
}
