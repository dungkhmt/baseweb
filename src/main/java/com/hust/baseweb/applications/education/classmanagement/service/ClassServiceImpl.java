package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.entity.Semester;
import com.hust.baseweb.applications.education.model.getclasslist.ClassOM;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import com.hust.baseweb.applications.education.repo.SemesterRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassServiceImpl implements ClassService {

    private ClassRepo classRepo;

    private SemesterRepo semesterRepo;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getClassListOfCurrentSemester(int page, int size) {
        Semester semester = semesterRepo.findByActiveTrue();
        Page<ClassOM> classes = classRepo.findBySemesterId(semester.getId(), PageRequest.of(page, size));

        return ResponseEntity.ok().body(new GetClassListOM(semester.getId(), classes));
    }
}
