package com.sen.QuestionnaireCore;


import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    public final boolean isAnswerable;
    public final QuestionType type;
    public final String name;
    public final long id;
    public final String description;
    public final String answer;
    public final List<Choice> choices;
    public final int score;

    public Question(boolean isAnswerable, QuestionType type, String name, long id, String description, String answer, List<Choice> choices, int score) {
        this.isAnswerable = isAnswerable;
        this.type = type;
        this.name = name;
        this.id = id;
        this.description = description;
        this.answer = answer;
        this.choices = choices;
        this.score = score;
    }
}
