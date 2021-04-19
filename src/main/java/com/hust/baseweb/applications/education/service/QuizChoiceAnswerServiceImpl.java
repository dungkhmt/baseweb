package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChoiceAnswerCreateInputModel;
import com.hust.baseweb.applications.education.repo.QuizChoiceAnswerRepo;
import com.hust.baseweb.applications.education.repo.QuizQuestionRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizChoiceAnswerServiceImpl implements QuizChoiceAnswerService {
    private QuizChoiceAnswerRepo quizChoiceAnswerRepo;
    private QuizQuestionRepo quizQuestionRepo;
    @Override
    public List<QuizChoiceAnswer> findAll() {
        return quizChoiceAnswerRepo.findAll();
    }

    @Override
    public QuizChoiceAnswer save(QuizChoiceAnswerCreateInputModel input) {
        QuizChoiceAnswer quizChoiceAnswer = new QuizChoiceAnswer();
        quizChoiceAnswer.setChoiceAnswerContent(input.getChoiceAnswerContent());
        quizChoiceAnswer.setIsCorrectAnswer(input.getIsCorrectAnswer());
        QuizQuestion quizQuestion = quizQuestionRepo.findById(input.getQuizQuestionId()).orElse(null);
        quizChoiceAnswer.setQuizQuestion(quizQuestion);

        quizChoiceAnswer  = quizChoiceAnswerRepo.save(quizChoiceAnswer);

        return quizChoiceAnswer;
    }

    @Override
    public List<QuizChoiceAnswer> findAllByQuizQuestionId(UUID quizQuestionId) {
        log.info("findAllByQuizQuestionId, quizQuestionId = " + quizQuestionId);

        QuizQuestion quizQuestion = quizQuestionRepo.findById(quizQuestionId).orElse(null);
        List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerRepo.findAllByQuizQuestion(quizQuestion);
        return quizChoiceAnswers;
    }
}
