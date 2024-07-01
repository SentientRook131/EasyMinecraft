package com.sen.Events;

import com.sen.QuestionnaireCore.QuestionnaireInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerFinishQuestionnaireEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    protected final QuestionnaireInstance questionnaireInstance;
    protected final int score;
    public PlayerFinishQuestionnaireEvent(@NotNull Player who, QuestionnaireInstance questionnaireInstance, int score) {
        super(who);
        this.questionnaireInstance = questionnaireInstance;
        this.score = score;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public int getScore() {
        return score;
    }

    public QuestionnaireInstance getQuestionnaireInstance() {
        return questionnaireInstance;
    }
}
