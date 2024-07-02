package com.sen.Events;

import com.sen.InventoryCore.InventoryAPI;
import com.sen.QuestionnaireCore.QuestionnaireInstance;
import com.sen.Toolkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitQuestionnaireAbnormallyEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    protected QuestionnaireInstance questionnaireInstance;

    public PlayerQuitQuestionnaireAbnormallyEvent(@NotNull Player who, QuestionnaireInstance questionnaireInstance) {
        super(who);
        this.questionnaireInstance = questionnaireInstance;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public boolean forcePullBack() {
        if (!player.isOnline()) return false;
        player.sendMessage(Toolkit.prefix + "该问卷不允许中途退出，请继续作答。");
        InventoryAPI.showQuestion(questionnaireInstance, questionnaireInstance.getCurrentQuestion(), player);
        return true;
    }
}
