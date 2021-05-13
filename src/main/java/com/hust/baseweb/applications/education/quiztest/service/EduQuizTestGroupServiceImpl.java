package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestGroupRepo;
import com.hust.baseweb.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EduQuizTestGroupServiceImpl implements EduQuizTestGroupService{
    private EduQuizTestGroupRepo eduQuizTestGroupRepo;


    @Override
    public List<EduTestQuizGroup> generateQuizTestGroups(GenerateQuizTestGroupInputModel input) {
        List<EduTestQuizGroup> eduTestQuizGroups = eduQuizTestGroupRepo.findByTestId(input.getQuizTestId());
        List<EduTestQuizGroup> eduTestQuizGroupList = new ArrayList<EduTestQuizGroup>();
        String[] codes  = new String[eduTestQuizGroups.size()];
        for(int i = 0; i < eduTestQuizGroups.size(); i++){
            codes[i] = eduTestQuizGroups.get(i).getGroupCode();
        }
        String[] newCodes = CommonUtils.generateNextSeqId(codes, input.getNumberOfQuizTestGroups());

        for(int i = 0; i < newCodes.length; i++){
            log.info("generateQuizTestGroups, gen newCode " + newCodes[i]);
            EduTestQuizGroup eduTestQuizGroup = new EduTestQuizGroup();
            eduTestQuizGroup.setTestId(input.getQuizTestId());
            eduTestQuizGroup.setGroupCode(newCodes[i]);

            eduTestQuizGroup = eduQuizTestGroupRepo.save(eduTestQuizGroup);

            eduTestQuizGroupList.add(eduTestQuizGroup);
        }
        return eduTestQuizGroupList;
    }
}
