package com.sen.GameCore;

import org.bukkit.World;

public class Game {
    public final String name;
    public Game(String name) {
        this.name = name;
    }
    public GameControl getInstance(World world, Runnable gameTickEvent, Runnable gameStartEvent, Runnable gameEndEvent) {
        return new GameControl(this, world) {
            @Override
            public void GameTickEvent() {
                gameTickEvent.run();
            }

            @Override
            public void GameStartEvent() {
                gameStartEvent.run();
            }

            @Override
            public void GameEndEvent() {
                gameEndEvent.run();
            }
        };
    }
}
