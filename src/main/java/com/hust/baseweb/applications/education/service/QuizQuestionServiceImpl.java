package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.classmanagement.service.storage.exception.StorageException;
import com.hust.baseweb.applications.education.entity.LogUserLoginQuizQuestion;
import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChooseAnswerInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionUpdateInputModel;
import com.hust.baseweb.applications.education.repo.LogUserLoginQuizQuestionRepo;
import com.hust.baseweb.applications.education.repo.QuizChoiceAnswerRepo;
import com.hust.baseweb.applications.education.repo.QuizCourseTopicRepo;
import com.hust.baseweb.applications.education.repo.QuizQuestionRepo;
import com.hust.baseweb.config.FileSystemStorageProperties;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private QuizQuestionRepo quizQuestionRepo;

    private QuizCourseTopicRepo quizCourseTopicRepo;

    private QuizCourseTopicService quizCourseTopicService;

    private QuizChoiceAnswerRepo quizChoiceAnswerRepo;

    private LogUserLoginQuizQuestionRepo logUserLoginQuizQuestionRepo;

    private FileSystemStorageProperties properties;

    @Override
    public QuizQuestion save(QuizQuestionCreateInputModel input) {
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setLevelId(input.getLevelId());
        quizQuestion.setQuestionContent(input.getQuestionContent());
        quizQuestion.setStatusId(QuizQuestion.STATUS_PRIVATE);

        QuizCourseTopic quizCourseTopic = quizCourseTopicRepo.findById(input.getQuizCourseTopicId()).orElse(null);

        quizQuestion.setQuizCourseTopic(quizCourseTopic);

        quizQuestion = quizQuestionRepo.save(quizQuestion);

        return quizQuestion;
    }

    @Override
    public QuizQuestion save(UserLogin u, QuizQuestionCreateInputModel input, MultipartFile[] files) {

        //Do save file
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefixFileName = formatter.format(now);
        ArrayList<String> attachmentPaths = new ArrayList<>();
        Arrays.asList(files).forEach(file -> {
            try {
                Path path = Paths.get(properties.getFilesystemRoot()+"\\" + properties.getClassManagementDataPath());
                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

                if (file.isEmpty()) {
                    throw new StorageException("Failed to store empty file " + originalFileName);
                }

                if (originalFileName.contains("..")) {
                    // This is a security check
                    throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                        + originalFileName);
                }

                // Can throw IOExeption, e.g NoSuchFileException.
                Files.copy(
                    file.getInputStream(),
                    path.resolve(prefixFileName + "-" + originalFileName),
                    StandardCopyOption.REPLACE_EXISTING);
                attachmentPaths.add(prefixFileName + "-" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //taskInput.setAttachmentPaths(attachmentPaths.toArray(new String[0]));
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setLevelId(input.getLevelId());
        quizQuestion.setQuestionContent(input.getQuestionContent());
        //quizQuestion.setCreatedByUserLogin(u);
        quizQuestion.setCreatedByUserLoginId(u.getUserLoginId());
        QuizCourseTopic quizCourseTopic = quizCourseTopicRepo.findById(input.getQuizCourseTopicId()).orElse(null);

        quizQuestion.setQuizCourseTopic(quizCourseTopic);
        quizQuestion.setStatusId(QuizQuestion.STATUS_PRIVATE);
        quizQuestion.setAttachment(String.join(";", attachmentPaths.toArray(new String[0])));
        quizQuestion.setCreatedStamp(new Date());
        quizQuestion = quizQuestionRepo.save(quizQuestion);

        return quizQuestion;
    }

    @Override
    public List<QuizQuestion> findAll() {
        return quizQuestionRepo.findAll();
    }

    @Override
    public List<QuizQuestion> findQuizOfCourse(String courseId) {
        List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findAllByEduCourse(courseId);
        //List<String> quizCourseTopicIds = quizCourseTopics.stream()
        //                                                  .map(quizCourseTopic -> quizCourseTopic.getQuizCourseTopicId()).collect(
        //    Collectors.toList());
        for (QuizCourseTopic q : quizCourseTopics) {
            log.info("findQuizOfCourse, courseId = " +
                     courseId +
                     ", topic = " +
                     q.getQuizCourseTopicId() +
                     ", " +
                     q.getQuizCourseTopicName() +
                     ", courseId = " +
                     q.getEduCourse().getId());

        }
        List<QuizQuestion> quizQuestions = quizQuestionRepo.findAllByQuizCourseTopicIn(quizCourseTopics);
        log.info("findQuizOfCourse, courseId = " + courseId + ", quizCourseTopics.sz = " + quizCourseTopics.size()
                 + " return quizQuestions.sz = " + quizQuestions.size());
        return quizQuestions;
    }

    @Override
    public QuizQuestionDetailModel findQuizDetail(UUID questionId) {
        QuizQuestion quizQuestion = quizQuestionRepo.findById(questionId).orElse(null);
        QuizQuestionDetailModel quizQuestionDetailModel = new QuizQuestionDetailModel();
        quizQuestionDetailModel.setLevelId(quizQuestion.getLevelId());
        quizQuestionDetailModel.setStatement(quizQuestion.getQuestionContent());
        quizQuestionDetailModel.setQuizCourseTopic(quizQuestion.getQuizCourseTopic());
        quizQuestionDetailModel.setQuestionId(quizQuestion.getQuestionId());
        quizQuestionDetailModel.setStatusId(quizQuestion.getStatusId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String tempDate = formatter.format(quizQuestion.getCreatedStamp());
            quizQuestionDetailModel.setCreatedStamp(tempDate);
        }
        catch(Exception e) {
            //  Block of code to handle errors
        }
        List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerRepo.findAllByQuizQuestion(quizQuestion);
        //log.info("findQuizDetail, questionId = " +
        //         questionId +
        //         ", GOT quizChoideAnswers.sz = " +
        //         quizChoiceAnswers.size());

        quizQuestionDetailModel.setQuizChoiceAnswerList(quizChoiceAnswers);

        return quizQuestionDetailModel;
    }

    @Override
    public QuizQuestion changeOpenCloseStatus(UUID questionId) {
        QuizQuestion quizQuestion = quizQuestionRepo.findById(questionId).orElse(null);
        if (quizQuestion.getStatusId().equals(QuizQuestion.STATUS_PRIVATE)) {
            quizQuestion.setStatusId(QuizQuestion.STATUS_PUBLIC);
        } else {
            quizQuestion.setStatusId(QuizQuestion.STATUS_PRIVATE);
        }
        quizQuestion = quizQuestionRepo.save(quizQuestion);
        return quizQuestion;
    }

    @Override
    public boolean checkAnswer(UserLogin userLogin, QuizChooseAnswerInputModel quizChooseAnswerInputModel) {
        QuizQuestionDetailModel quizQuestionDetail = findQuizDetail(quizChooseAnswerInputModel.getQuestionId());

        LogUserLoginQuizQuestion logUserLoginQuizQuestion = new LogUserLoginQuizQuestion();
        logUserLoginQuizQuestion.setUserLoginId(userLogin.getUserLoginId());
        logUserLoginQuizQuestion.setQuestionId(quizChooseAnswerInputModel.getQuestionId());
        logUserLoginQuizQuestion.setCreateStamp(new Date());
        logUserLoginQuizQuestion = logUserLoginQuizQuestionRepo.save(logUserLoginQuizQuestion);

        boolean ans = true;

        List<UUID> correctAns = quizQuestionDetail
            .getQuizChoiceAnswerList()
            .stream()
            .filter(answer -> answer.getIsCorrectAnswer() == 'Y')
            .map(QuizChoiceAnswer::getChoiceAnswerId)
            .collect(Collectors.toList());

        if (!correctAns.containsAll(quizChooseAnswerInputModel.getChooseAnsIds())) {
            ans = false;
        }

        return ans;
    }

    @Override
    public QuizQuestion findById(UUID questionId) {
        return quizQuestionRepo.findById(questionId).orElse(null);
    }

    @Override
    public QuizQuestion update(UUID questionId, QuizQuestionUpdateInputModel input, MultipartFile[] files){
//        Date now = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String prefixFileName = formatter.format(now);
//        ArrayList<String> attachmentPaths = new ArrayList<>();
//        Arrays.asList(files).forEach(file -> {
//            try {
//                Path path = Paths.get(properties.getFilesystemRoot() +"\\"+ properties.getClassManagementDataPath());
//                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//                if (file.isEmpty()) {
//                    throw new StorageException("Failed to store empty file " + originalFileName);
//                }
//
//                if (originalFileName.contains("..")) {
//                    // This is a security check
//                    throw new StorageException(
//                        "Cannot store file with relative path outside current directory "
//                        + originalFileName);
//                }
//
//                // Can throw IOExeption, e.g NoSuchFileException.
//                Files.copy(file.getInputStream(), path.resolve(prefixFileName + "-" + originalFileName), StandardCopyOption.REPLACE_EXISTING);
//                attachmentPaths.add(prefixFileName + "-" + file.getOriginalFilename());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
        //taskInput.setAttachmentPaths(attachmentPaths.toArray(new String[0]));
        QuizQuestion quizQuestionTemp = quizQuestionRepo.findById(questionId).orElse(null);
        if (quizQuestionTemp == null)
            return null;
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuestionId(questionId);
        quizQuestion.setLevelId(input.getLevelId());
        quizQuestion.setQuestionContent(input.getQuestionContent());
        QuizCourseTopic quizCourseTopic = quizCourseTopicRepo.findById(input.getQuizCourseTopicId()).orElse(null);
        if (quizCourseTopic == null)
            return null;
        quizQuestion.setQuizCourseTopic(quizCourseTopic);
//        quizQuestion.setAttachment(String.join(";", attachmentPaths.toArray(new String[0])));
        quizQuestion.setLastUpdatedStamp(new Date());
        quizQuestion.setCreatedStamp(quizQuestionTemp.getCreatedStamp());
        quizQuestion.setStatusId(quizQuestionTemp.getStatusId());
        quizQuestion = quizQuestionRepo.save(quizQuestion);

        return quizQuestion;
    }
}
