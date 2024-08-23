package io.event.thinking.sample.faculty.model;

import io.event.thinking.eventstore.api.Criteria;
import io.event.thinking.micro.es.CommandModel;
import io.event.thinking.micro.es.Event;
import io.event.thinking.sample.faculty.api.command.UnsubscribeStudent;
import io.event.thinking.sample.faculty.api.event.StudentSubscribed;
import io.event.thinking.sample.faculty.api.event.StudentUnsubscribed;

import java.util.List;

import static io.event.thinking.eventstore.api.Criterion.criterion;
import static io.event.thinking.eventstore.api.Tag.tag;
import static io.event.thinking.micro.es.Event.event;
import static io.event.thinking.micro.es.Tags.type;
import static io.event.thinking.sample.faculty.model.Constants.COURSE_ID;
import static io.event.thinking.sample.faculty.model.Constants.STUDENT_ID;

public class UnsubscribeStudentCommandModel implements CommandModel<UnsubscribeStudent> {

    private boolean subscribed = false;

    @Override
    public Criteria criteria(UnsubscribeStudent cmd) {
        return Criteria.criteria(criterion(type(StudentSubscribed.NAME),
                                           tag(STUDENT_ID, cmd.studentId()),
                                           tag(COURSE_ID, cmd.courseId())),
                                 criterion(type(StudentUnsubscribed.NAME),
                                           tag(STUDENT_ID, cmd.studentId()),
                                           tag(COURSE_ID, cmd.courseId())));
    }

    @Override
    public List<Event> handle(UnsubscribeStudent cmd) {
        if (subscribed) {
            return List.of(tagEvent(new StudentUnsubscribed(cmd.studentId(), cmd.courseId())));
        }
        throw new RuntimeException("Student is not subscribed to course");
    }

    void on(StudentSubscribed evt) {
        subscribed = true;
    }

    void on(StudentUnsubscribed evt) {
        subscribed = false;
    }

    private static Event tagEvent(StudentUnsubscribed event) {
        return event(event,
                     type(StudentUnsubscribed.NAME),
                     tag(STUDENT_ID, event.studentId()),
                     tag(COURSE_ID, event.courseId()));
    }

    // This would be done by the framework for you
    @Override
    public void onEvent(Event event) {
        switch (event.payload()) {
            case StudentSubscribed e -> on(e);
            case StudentUnsubscribed e -> on(e);
            default -> throw new RuntimeException("No handler for this event");
        }
    }
}