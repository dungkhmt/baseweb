package com.hust.baseweb.applications.adminmaintenance.service.educlasscourse;

import com.hust.baseweb.applications.education.entity.*;
import com.hust.baseweb.applications.education.repo.*;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassCourseMaintenanceServiceImpl implements ClassCourseMaintenanceService {
    private LogUserLoginCourseChapterMaterialRepo logUserLoginCourseChapterMaterialRepo;
    private EduCourseRepo eduCourseRepo;
    private EduCourseChapterRepo eduCourseChapterRepo;
    private EduCourseChapterMaterialRepo eduCourseChapterMaterialRepo;
    private ClassRepo classRepo;

    @Override
    public int updateClassIdLogCourse() {
        List<LogUserLoginCourseChapterMaterial> logUserLoginCourseChapterMaterialList =
           logUserLoginCourseChapterMaterialRepo.findAll();
        List<EduCourseChapter> eduCourseChapters  = eduCourseChapterRepo.findAll();
        List<EduCourseChapterMaterial> eduCourseChapterMaterials = eduCourseChapterMaterialRepo.findAll();
        List<EduClass> eduClasses = classRepo.findAll();
        Map<UUID, EduCourseChapterMaterial> mapUUID2CourseChapterMaterial = new HashMap<>();
        for(EduCourseChapterMaterial e: eduCourseChapterMaterials){
            mapUUID2CourseChapterMaterial.put(e.getEduCourseMaterialId(),e);
        }
        Map<UUID, EduCourseChapter> mapUUID2CourseChapter  = new HashMap();
        for(EduCourseChapter e: eduCourseChapters){
            mapUUID2CourseChapter.put(e.getChapterId(),e);
            System.out.println("map chapter " + e.getChapterId());
        }
        Map<String, List<EduClass>> mapCourseID2ClassList = new HashMap();
        Map<String, EduClass> mapID2Class = new HashMap();
        for(EduClass e: eduClasses){
            String courseId = e.getEduCourse().getId();
            if(mapCourseID2ClassList.get(courseId) == null)
                mapCourseID2ClassList.put(courseId,new ArrayList());
            mapCourseID2ClassList.get(courseId).add(e);

            mapID2Class.put(e.getClassCode(),e);
        }

        int cnt = 0;
        for(LogUserLoginCourseChapterMaterial e: logUserLoginCourseChapterMaterialList){
            UUID materialId = e.getEduCourseMaterialId();
            UUID chapterId = mapUUID2CourseChapterMaterial.get(materialId).getEduCourseChapter().getChapterId();
            EduCourseChapter eduCourseChapter = mapUUID2CourseChapter.get(chapterId);
            if(eduCourseChapter == null){
                System.out.println("chapterId " + chapterId + " of material " + materialId + " not exists");
                continue;
            }
            EduCourse eduCourse = eduCourseChapter.getEduCourse();
            if(eduCourse == null){
                System.out.println("material " + materialId + " do not has course");
                continue;
            }
            String courseId = eduCourse.getId();
            List<EduClass> cls = mapCourseID2ClassList.get(courseId);
            String classId = null;
            if(cls != null)   for(EduClass c: cls){
                    if(c.getClassCode().equals("TTUD_20202")){
                        classId = c.getClassCode(); break;
                    }else if(c.getClassCode().equals("CTDL_20202")){
                        classId = c.getClassCode(); break;
                    }else if(c.getClassCode().equals("TOAN_RR_20202")){
                        classId = c.getClassCode(); break;
                    }else if(c.getClassCode().equals("DSA")){
                        classId = c.getClassCode(); break;
                    }
            }
            if(classId != null){
                EduClass c = mapID2Class.get(classId);
                e.setEduClass(c);
                logUserLoginCourseChapterMaterialRepo.save(e);
                cnt += 1;
                System.out.println("update " + cnt + " for class " + classId + " of course " + courseId);
            }
        }
        System.out.println("FINISHED updateClassIdLogCourse");
        return cnt;
    }
}
