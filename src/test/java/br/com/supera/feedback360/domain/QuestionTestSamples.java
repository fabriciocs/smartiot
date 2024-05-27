package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question().id(1L).questionText("questionText1").questionType("questionType1");
    }

    public static Question getQuestionSample2() {
        return new Question().id(2L).questionText("questionText2").questionType("questionType2");
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(longCount.incrementAndGet())
            .questionText(UUID.randomUUID().toString())
            .questionType(UUID.randomUUID().toString());
    }
}
