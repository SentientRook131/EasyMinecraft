package com.sen.Events;

import com.sen.QuestionnaireCore.QuestionnaireInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerConductQuestionnaireEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    protected final QuestionnaireInstance questionnaireInstance;
    public PlayerConductQuestionnaireEvent(@NotNull Player who, QuestionnaireInstance questionnaireInstance) {
        super(who);
        this.cancelled = false;
        this.questionnaireInstance = questionnaireInstance;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public QuestionnaireInstance getQuestionnaireInstance() {
        return questionnaireInstance;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
