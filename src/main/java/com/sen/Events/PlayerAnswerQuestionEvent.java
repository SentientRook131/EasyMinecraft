package com.sen.Events;

import com.sen.QuestionnaireCore.Question;
import com.sen.QuestionnaireCore.QuestionnaireInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerAnswerQuestionEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    protected final Question question;
    protected final QuestionnaireInstance questionnaireInstance;
    public PlayerAnswerQuestionEvent(@NotNull Player who, Question question, QuestionnaireInstance questionnaireInstance) {
        super(who);
        this.question = question;
        this.questionnaireInstance = questionnaireInstance;
    }

    public Question getQuestion() {
        return question;
    }

    public QuestionnaireInstance getQuestionnaireInstance() {
        return questionnaireInstance;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
