package com.sen.QuestionnaireCore;

import com.sen.Events.PlayerConductQuestionnaireEvent;
import com.sen.Events.PlayerFinishQuestionnaireEvent;
import com.sen.InventoryCore.InventoryAPI;
import com.sen.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;

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
    public Question getCurrentQuestion() {
        return originalQuestionnaire.questions.get(currentQuestionIndex);
    }
    public void start() {
        PlayerConductQuestionnaireEvent e = new PlayerConductQuestionnaireEvent(whoAreDoing, this);
        Bukkit.getServer().getPluginManager().callEvent(e);
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
        Inventory i = InventoryAPI.showQuestion(this, question, whoAreDoing);
        questionInventories.put(whoAreDoing.getUniqueId(), i);
        whoAreDoing.openInventory(i);
    }
    public void finish() {
        PlayerFinishQuestionnaireEvent playerFinishQuestionnaireEvent = new PlayerFinishQuestionnaireEvent(whoAreDoing, this, score);
        Bukkit.getServer().getPluginManager().callEvent(playerFinishQuestionnaireEvent);
        whoAreDoing.sendMessage(prefix + "恭喜您成功完成问卷！");
        whoAreDoing.sendMessage(prefix + "您的分值：" + score);
        whoAreDoing.playSound(whoAreDoing.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
        whoAreDoingQuestionnaire.removeIf(q -> q.first.equals(whoAreDoing.getUniqueId()));
        questionInventories.remove(whoAreDoing.getUniqueId());
    }
}
