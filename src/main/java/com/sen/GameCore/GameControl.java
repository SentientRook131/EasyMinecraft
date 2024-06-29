package com.sen.GameCore;

import com.sen.em;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class GameControl implements IGameControl {
    public final Game game;
    public final World world;
    public final List<Player> playerList;
    public final List<Player> playingPlayerList;
    private boolean isStarted = false;
    private boolean isEnded = false;
    GameControl(Game game, World world) {
        this.game = game;
        this.world = world;
        playerList = new ArrayList<>();
        playingPlayerList = new ArrayList<>();
        Bukkit.getScheduler().runTaskTimerAsynchronously(JavaPlugin.getPlugin(em.class), this::GameTickEvent, 0, 1);
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start() {
        isStarted = true;
        GameStartEvent();
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void end() {
        isEnded = true;
        GameEndEvent();
    }
}
