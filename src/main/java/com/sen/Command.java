package com.sen;

import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class Command {
    public final String rootCmd;
    private final Function<Pair<String[], CommandSender>, Boolean> runnable;
    public Command(String rootCmd, Function<Pair<String[], CommandSender>, Boolean> runnable) {
        this.rootCmd = rootCmd;
        this.runnable = runnable;
    }
    public boolean run(String[] args, CommandSender sender) throws InterruptedException {
        return runnable.apply(new Pair<>(args, sender));
    }
}
