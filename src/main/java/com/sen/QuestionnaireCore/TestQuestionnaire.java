package com.sen.QuestionnaireCore;

public class TestQuestionnaire extends Questionnaire {
    public TestQuestionnaire() {
        super("测试问卷", "test_questionnaire", "仅测试用");
        addQuestion(new Question(true, QuestionType.CHOICE, "测试题目1", "测试选项", "A", new String[]{"A.选我","B.选A"}, 40));
        addQuestion(new Question(true, QuestionType.CHOICE, "测试题目2", "测试选项", "B", new String[]{"A.选B","B.选我"}, 60));
    }
}
