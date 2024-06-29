package com.sen.QuestionnaireCore;

import com.sen.Toolkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Questionnaire implements Serializable {

    public final List<Question> questions = new ArrayList<>();
    public final String title;
    public final String name;
    public final String description;
    public Questionnaire(String title, String name, String description) {
        this.title = title;
        this.name = name;
        this.description = description;
    }
    public void selfRegister() {
        Toolkit.registerQuestionnaire(this);
    }
    public Questionnaire addQuestion(Question question) {
        questions.add(question);
        return this;
    }
    public Questionnaire addQuestion(Question... questions) {
        for (Question question : questions) {
            addQuestion(question);
        }
        return this;
    }
    public int getTotalScore() {
        int totalScore = 0;
        for (Question question : questions) {
            totalScore += question.score;
        }
        return totalScore;
    }
    public int getQuestionsCount() {
        return questions.size();
    }
    public QuestionnaireInstance conduct(Player player, boolean ableToMove, boolean ableToSpeak, boolean ableToSeeMessage, boolean ableToLoadChunk) {
        return new QuestionnaireInstance(this, player, ableToMove, ableToSpeak, ableToSeeMessage, ableToLoadChunk);
    }
}
