package com.hust.baseweb.applications.education.suggesttimetable.repo;


import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.model.GroupClassesOM;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
public class EduClassRepo implements IClassRepo {

    @Delegate
    private final IClassMongoRepo classRepo;

    private final MongoTemplate mongoTemplate;

    @Override
    public List<EduClass> saveAll(List<EduClass> eduClasses) {
        return classRepo.saveAll(eduClasses);
    }

    /**
     * Drop collection and insert classes in batch.
     *
     * @param classes
     */
    @Override
    public void insertClassesInBatch(List<EduClass> classes) {
        String collectionName = "class";
        mongoTemplate.dropCollection(collectionName);
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, collectionName);
        bulkOperations.insert(classes);
        bulkOperations.execute();
    }

    @Override
    public List<GroupClassesOM> getAllClassesOfCourses(Set<String> courseIds) {
        MatchOperation match = Aggregation.match(new Criteria("courseId").in(courseIds));
        GroupOperation group = Aggregation.group("courseId", "classType").push(Aggregation.ROOT).as("classes");
        Aggregation aggregation = Aggregation.newAggregation(match, group);

        return mongoTemplate.aggregate(aggregation, "class", GroupClassesOM.class).getMappedResults();
    }
}
