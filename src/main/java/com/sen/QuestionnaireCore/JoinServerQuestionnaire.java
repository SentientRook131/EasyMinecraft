package com.sen.QuestionnaireCore;

import com.sen.Toolkit;

import java.util.Collections;

public class JoinServerQuestionnaire extends Questionnaire {

    public JoinServerQuestionnaire() {
        super("服务器检测玩家水平测试问卷", 0, "该问卷目的是选拔合格的玩家，筛选素质不差的玩家");
        addQuestion(new Question(true, QuestionType.CHOICE, "sin 30° 等于", 1, "", "A", Toolkit.createList(new Choice("A", "A. 1/2"), new Choice("B", "B. 钝角")), 10));
        addQuestion(new Question(true, QuestionType.CHOICE, "sin 60° 等于", 2, "", "B", Toolkit.createList(new Choice("A", "A. sqrt(3)/3"), new Choice("B", "B. sqrt(3)/2")), 30));
        addQuestion(new Question(true, QuestionType.COMPLETION, "tan 45° 等于", 3, "", "1", Collections.emptyList(), 60));
    }
}
