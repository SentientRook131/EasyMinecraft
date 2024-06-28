package com.sen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import static com.sen.Toolkit.*;

public final class em extends JavaPlugin {

    //public static Bot bot = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println(" -- Thanks for using this plugin. -- ");
        System.out.println(" -- This plugin is free at all. -- ");
        System.out.println(" -- Author: SentientRook131 -- ");
        System.out.println(" -- QQ: 3460596497 -- ");
        System.out.println(" -- Please make sure that this server can get the Real Address of players -- ");
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        try {
            if (cmd.getName().equalsIgnoreCase("em")) {
                if (args[0].equalsIgnoreCase("location-display")) {
                    if (args[1].equalsIgnoreCase("off")) {
                        config.set("location-display.players-settings." + player.getUniqueId() + ".show-mode", "off");
                    } else if (args[1].equalsIgnoreCase("province")) {
                        config.set("location-display.players-settings." + player.getUniqueId() + ".show-mode", "province");
                    } else if (args[1].equalsIgnoreCase("city")) {
                        config.set("location-display.players-settings." + player.getUniqueId() + ".show-mode", "city");
                    } else if (args[1].equalsIgnoreCase("country")) {
                        config.set("location-display.players-settings." + player.getUniqueId() + ".show-mode", "country");
                    } else if (args[1].equalsIgnoreCase("reload")) {
                        player.sendMessage(prefix + "正在重新获取位置信息。");
                        config.set("location-display.players-settings." + player.getUniqueId() + ".location-buffer", getLocationInfo(player.getAddress()));
                        player.sendMessage(prefix + "获取成功。");
                    } else {
                        player.sendMessage(prefix + "输入的参数有问题！如需关闭请输入参数：off。");
                    }
                } else if (args[0].equalsIgnoreCase("var")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        if (args[2].equalsIgnoreCase("public")) {
                            if (config.getStringList("variables.public.$defined$").contains(args[3])) {
                                player.sendMessage(prefix + "变量已存在！");
                            } else {
                                config.set("variables.public." + args[3], (args.length == 4) ? "" : args[4]);
                                List<String> list = config.getStringList("variables.public.$defined$");
                                list.add(args[3]);
                                config.set("variables.public.$defined$", list);
                                player.sendMessage(prefix + "变量创建成功！");
                            }
                        } else if (args[2].equalsIgnoreCase("private")) {
                            if (config.getStringList("variables.private." + player.getUniqueId() + ".$defined$").contains(args[3])) {
                                player.sendMessage(prefix + "变量已存在！");
                            } else {
                                config.set("variables.private." + player.getUniqueId() + "." + args[3], (args.length == 4) ? "" : args[4]);
                                List<String> list = config.getStringList("variables.private." + player.getUniqueId() + ".$defined$");
                                list.add(args[3]);
                                config.set("variables.private." + player.getUniqueId() + ".$defined$", list);
                                player.sendMessage(prefix + "变量创建成功！");
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("set")) {
                        List<String> public_variables = config.getStringList("variables.public.$defined$");
                        List<String> private_variables = config.getStringList("variables.private." + player.getUniqueId() + ".$defined$");
                        if (private_variables.contains(args[2])) config.set("variables.private." + player.getUniqueId() + "." + args[2], args[3]);
                        else if (public_variables.contains(args[2])) config.set("variables.public." + args[2], args[3]);
                        else {
                            player.sendMessage(prefix + "变量不存在！");
                            return true;
                        }
                        player.sendMessage(prefix + "变量设置成功！");
                    }
                } else if (args[1].equalsIgnoreCase("toolkit")) {
                    if (args[2].equalsIgnoreCase("ping")) {
                        if (args.length == 3) player.sendMessage(prefix + "您的延迟：" + player.getPing() + "ms");
                        else player.sendMessage(prefix + args[3] + "的延迟：" + Objects.requireNonNull(Bukkit.getServer().getPlayer(args[3])).getPing() + "ms");
                    }
                }
            }
        } catch (Exception ignore) {
            player.sendMessage(prefix + "输入的参数有问题！如需关闭请输入参数：off。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("em")) {
            if (args.length == 1) {
                result.add("location-display");
                result.add("var");
                result.add("qq");
                result.add("toolkit");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("location-display")) {
                    result.add("off");
                    result.add("reload");
                    result.add("city");
                    result.add("province");
                    result.add("country");
                } else if (args[0].equalsIgnoreCase("var")) {
                    result.add("create");
                    result.add("set");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("var")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        result.add("public");
                        result.add("private");
                    }
                } else if (args[0].equalsIgnoreCase("toolkit")) {
                    if (args[1].equalsIgnoreCase("ping")) {
                        result = super.onTabComplete(sender, cmd, alias, args);
                    }
                }
            }
        }
        return result.isEmpty() ? super.onTabComplete(sender, cmd, alias, args) : result;
    }
}
