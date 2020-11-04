package com.hust.baseweb.applications.schedule.repo;

import com.hust.baseweb.applications.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleRepo extends JpaRepository<Schedule,UUID>
{
    Schedule save(Schedule schedule);
}
