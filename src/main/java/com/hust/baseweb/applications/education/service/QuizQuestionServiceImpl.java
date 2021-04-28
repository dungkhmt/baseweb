package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.classmanagement.service.storage.exception.StorageException;
import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.repo.QuizChoiceAnswerRepo;
import com.hust.baseweb.applications.education.repo.QuizCourseTopicRepo;
import com.hust.baseweb.applications.education.repo.QuizQuestionRepo;
import com.hust.baseweb.config.FileSystemStorageProperties;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizQuestionServiceImpl implements QuizQuestionService {
    private QuizQuestionRepo quizQuestionRepo;
    private QuizCourseTopicRepo quizCourseTopicRepo;
    private QuizCourseTopicService quizCourseTopicService;
    private QuizChoiceAnswerRepo quizChoiceAnswerRepo;

    private FileSystemStorageProperties properties;

    @Override
    public QuizQuestion save(QuizQuestionCreateInputModel input) {
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setLevelId(input.getLevelId());
        quizQuestion.setQuestionContent(input.getQuestionContent());
        QuizCourseTopic quizCourseTopic = quizCourseTopicRepo.findById(input.getQuizCourseTopicId()).orElse(null);

        quizQuestion.setQuizCourseTopic(quizCourseTopic);

        quizQuestion = quizQuestionRepo.save(quizQuestion);

        return quizQuestion;
    }

    @Override
    public QuizQuestion save(QuizQuestionCreateInputModel input, MultipartFile[] files) {
        
        //Do save file
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefixFileName = formatter.format(now);
        ArrayList<String> attachmentPaths = new ArrayList<>();
        Arrays.asList(files).forEach(file -> {
            try {
                Path path = Paths.get(properties.getFilesystemRoot() + properties.getClassManagementDataPath());
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
                Files.copy(file.getInputStream(), path.resolve(prefixFileName + "-" + originalFileName), StandardCopyOption.REPLACE_EXISTING);
                attachmentPaths.add(prefixFileName + "-" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //taskInput.setAttachmentPaths(attachmentPaths.toArray(new String[0]));
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setLevelId(input.getLevelId());
        quizQuestion.setQuestionContent(input.getQuestionContent());
        QuizCourseTopic quizCourseTopic = quizCourseTopicRepo.findById(input.getQuizCourseTopicId()).orElse(null);

        quizQuestion.setQuizCourseTopic(quizCourseTopic);

        quizQuestion.setAttachment(String.join(";", attachmentPaths.toArray(new String[0])));

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
        List<QuizQuestion> quizQuestions = quizQuestionRepo.findAllByQuizCourseTopicIn(quizCourseTopics);
        log.info("findQuizOfCourse, courseId = " + courseId + ", quizCourseTopics.sz = " + quizCourseTopics.size()
                 + " return quizQuestions.sz = " + quizQuestions.size());
        return quizQuestions;
    }

    @Override
    public QuizQuestionDetailModel findQuizDetail(UUID questionId) {
        QuizQuestion quizQuestion  = quizQuestionRepo.findById(questionId).orElse(null);
        QuizQuestionDetailModel quizQuestionDetailModel = new QuizQuestionDetailModel();
        quizQuestionDetailModel.setLevelId(quizQuestion.getLevelId());
        quizQuestionDetailModel.setStatement(quizQuestion.getQuestionContent());
        quizQuestionDetailModel.setQuizCourseTopic(quizQuestion.getQuizCourseTopic());
        quizQuestionDetailModel.setQuestionId(quizQuestion.getQuestionId());

        List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerRepo.findAllByQuizQuestion(quizQuestion);
        log.info("findQuizDetail, questionId = " + questionId + ", GOT quizChoideAnswers.sz = " + quizChoiceAnswers.size());

        quizQuestionDetailModel.setQuizChoiceAnswerList(quizChoiceAnswers);

        return quizQuestionDetailModel;
    }
}
