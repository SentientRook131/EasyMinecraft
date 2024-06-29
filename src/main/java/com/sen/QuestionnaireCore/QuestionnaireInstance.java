package com.sen.QuestionnaireCore;

import com.sen.Pair;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

import static com.sen.Toolkit.*;
public class QuestionnaireInstance implements Serializable {
    public final boolean ableToMove;
    public final boolean ableToSpeak;
    public final boolean ableToSeeMessage;
    public final boolean ableToLoadChunk;
    public final Questionnaire originalQuestionnaire;
    public final Player whoAreDoing;
    public int currentQuestionIndex;
    public int score;
    public QuestionnaireInstance(Questionnaire questionnaire, Player whoAreDoing, boolean ableToMove, boolean ableToSpeak, boolean ableToSeeMessage, boolean ableToLoadChunk) {
        this.ableToMove = ableToMove;
        this.ableToSpeak = ableToSpeak;
        this.ableToSeeMessage = ableToSeeMessage;
        this.ableToLoadChunk = ableToLoadChunk;
        this.whoAreDoing = whoAreDoing;
        this.originalQuestionnaire = questionnaire;
    }
    public void start() {
        this.currentQuestionIndex = -1;
        score = 0;
        nextQuestion(0);
        whoAreDoingQuestionnaire.add(new Pair<>(whoAreDoing.getUniqueId(), this));
    }
    public void nextQuestion(int getScore) {
        this.score += getScore;
        this.currentQuestionIndex++;

        if (currentQuestionIndex >= originalQuestionnaire.questions.size()) {
            finish();
            return;
        }

        Question question = originalQuestionnaire.questions.get(currentQuestionIndex);
        whoAreDoing.sendMessage(prefix + (currentQuestionIndex + 1) + ". " + question.name + "\n" + prefix + question.description);
        if (question.type == QuestionType.CHOICE) {
            for (String choice : question.choices) whoAreDoing.sendMessage(prefix + choice);
        }
    }
    public void finish() {
        whoAreDoing.sendMessage(prefix + "恭喜您成功完成问卷！");
        whoAreDoing.sendMessage(prefix + "您的分值：" + score);
        whoAreDoingQuestionnaire.removeIf(q -> q.first.equals(whoAreDoing.getUniqueId()));
    }
}
