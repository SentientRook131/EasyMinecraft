package com.sen.QuestionnaireCore;


import java.io.Serializable;

public class Question implements Serializable {
    public final boolean isAnswerable;
    public final QuestionType type;
    public final String name;
    public final String description;
    public final String answer;
    public final String[] choices;
    public final int score;

    public Question(boolean isAnswerable, QuestionType type, String name, String description, String answer, String[] choices, int score) {
        this.isAnswerable = isAnswerable;
        this.type = type;
        this.name = name;
        this.description = description;
        this.answer = answer;
        this.choices = choices;
        this.score = score;
    }
}
