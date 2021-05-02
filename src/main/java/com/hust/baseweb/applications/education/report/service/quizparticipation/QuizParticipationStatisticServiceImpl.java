package com.hust.baseweb.applications.education.report.service.quizparticipation;

import com.hust.baseweb.applications.education.entity.LogUserLoginQuizQuestion;
import com.hust.baseweb.applications.education.repo.LogUserLoginQuizQuestionRepo;
import com.hust.baseweb.applications.education.report.model.quizparticipation.QuizParticipationStatisticOutputModel;
import com.hust.baseweb.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizParticipationStatisticServiceImpl implements QuizParticipationStatisticService {
    private LogUserLoginQuizQuestionRepo logUserLoginQuizQuestionRepo;
    @Override
    public List<QuizParticipationStatisticOutputModel> getQuizParticipationStatistic() {
        List<LogUserLoginQuizQuestion> logUserLoginQuizQuestions = logUserLoginQuizQuestionRepo.findAll();
        List<QuizParticipationStatisticOutputModel> quizParticipationStatisticOutputModels = new ArrayList();
        HashMap<String, Integer> mDate2Count = new HashMap<>();

        for(LogUserLoginQuizQuestion i: logUserLoginQuizQuestions){
            Date date = i.getCreateStamp();
            if(date == null){
                continue;
            }
            String s_date = DateTimeUtils.date2YYYYMMDD(date);
            if(mDate2Count.get(s_date) == null){
                mDate2Count.put(s_date,1);
            }else{
                mDate2Count.put(s_date,mDate2Count.get(s_date)+1);
            }

        }
        for(String sd: mDate2Count.keySet()){
            quizParticipationStatisticOutputModels.add(new QuizParticipationStatisticOutputModel(sd,mDate2Count.get(sd)));
            log.info("getQuizParticipationStatistic, map date " + sd + " -> " + mDate2Count.get(sd));
        }
        return quizParticipationStatisticOutputModels;
    }
}
