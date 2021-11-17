package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.CommentOnQuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.CommentOnQuizQuestionDetailOM;
import com.hust.baseweb.applications.education.repo.CommentOnQuizQuestionRepo;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CommentOnQuizQuestionServiceImpl implements CommentOnQuizQuestionService{

    @Autowired
    private CommentOnQuizQuestionRepo commentOnQuizQuestionRepo;

    @Autowired
    private UserService userService;
    @Override
    public CommentOnQuizQuestion createComment(UUID questionId, String comment, UserLogin u) {
        CommentOnQuizQuestion commentOnQuizQuestion = new CommentOnQuizQuestion();
        commentOnQuizQuestion.setCommentText(comment);
        commentOnQuizQuestion.setQuestionId(questionId);
        commentOnQuizQuestion.setCreatedByUserLoginId(u.getUserLoginId());
        commentOnQuizQuestion.setCreatedStamp(new Date());
        commentOnQuizQuestion.setStatusId(CommentOnQuizQuestion.STATUS_CREATED);
        commentOnQuizQuestion = commentOnQuizQuestionRepo.save(commentOnQuizQuestion);
        return commentOnQuizQuestion;
    }

    @Override
    public List<CommentOnQuizQuestionDetailOM> findByQuestionId(UUID questionId) {
        List<CommentOnQuizQuestion> lst = commentOnQuizQuestionRepo.findAllByQuestionId(questionId);
        List<CommentOnQuizQuestionDetailOM> list = new ArrayList();
        for(CommentOnQuizQuestion c: lst){
            CommentOnQuizQuestionDetailOM cd = new CommentOnQuizQuestionDetailOM();
            cd.setCommentId(c.getCommentId());
            cd.setCommentText(c.getCommentText());
            cd.setCreatedByUserLoginId(c.getCreatedByUserLoginId());
            cd.setCreatedStamp(c.getCreatedStamp());

            PersonModel person = userService.findPersonByUserLoginId(c.getCreatedByUserLoginId());
            if(person != null){
                cd.setFullNameOfCreator(person.getLastName() + " " + person.getMiddleName()
                                        + " " + person.getFirstName());
            }
            list.add(cd);
        }
        return list;
    }
}
