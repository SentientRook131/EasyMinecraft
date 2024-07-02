package com.sen.Events;

import com.sen.QuestionnaireCore.Question;
import com.sen.QuestionnaireCore.QuestionnaireInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerAnswerQuestionWronglyEvent extends PlayerAnswerQuestionEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    public PlayerAnswerQuestionWronglyEvent(@NotNull Player who, Question question, QuestionnaireInstance questionnaireInstance) {
        super(who, question, questionnaireInstance);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
