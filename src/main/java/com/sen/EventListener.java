package com.sen;

import com.alibaba.fastjson2.JSONObject;
import com.sen.Events.PlayerAnswerQuestionCorrectlyEvent;
import com.sen.Events.PlayerAnswerQuestionEvent;
import com.sen.Events.PlayerAnswerQuestionWronglyEvent;
import com.sen.Events.PlayerQuitQuestionnaireAbnormallyEvent;
import com.sen.Log.LogEntry;
import com.sen.QuestionnaireCore.Question;
import com.sen.QuestionnaireCore.QuestionType;
import com.sen.QuestionnaireCore.QuestionnaireInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.sen.Toolkit.*;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerChatE1(AsyncPlayerChatEvent e) {
        if (!config.getBoolean("location-display.enable")) return;
        if (config.contains("location-display.players-settings." + e.getPlayer().getUniqueId() + ".location-buffer")) {
            String showMode = config.getString("location-display.players-settings." + e.getPlayer().getUniqueId() + ".show-mode");
            JSONObject json = getLocationByUUID(e.getPlayer().getUniqueId());
            if (showMode != null && showMode.equalsIgnoreCase("province")) {
                e.setFormat(ChatColor.GRAY + "[" + json.get("country") + " " + json.get("regionName") + "]" + ChatColor.WHITE + "<%1$s> %2$s");
            } else if (showMode != null && showMode.equalsIgnoreCase("country")) {
                e.setFormat(ChatColor.GRAY + "[" + json.get("country") + "]" + ChatColor.WHITE + "<%1$s> %2$s");
            } else if (showMode != null && showMode.equalsIgnoreCase("city")) {
                e.setFormat(ChatColor.GRAY + "[" + json.get("country") + " " + json.get("regionName") + " " + json.get("city") + "]" + ChatColor.WHITE + "<%1$s> %2$s");
            }
            return;
        } else {
            e.getPlayer().sendMessage(prefix + "检测到您是第一次发言，为了获取您的位置信息，请稍等...");
            e.getPlayer().sendMessage(prefix + "我们将会将您的位置信息缓存，以加快您的发言速度。");
            e.getPlayer().sendMessage(prefix + "如果您想重新获取您的位置信息，请使用 /em location-display reload 命令。");
            e.getPlayer().sendMessage(prefix + "我们将会默认您的位置信息详细到 国家 。 如果您想关闭此功能，请使用 /em location-display off 命令。");
        }
        if (!config.contains("location-display.players-settings." + e.getPlayer().getUniqueId() + ".show-mode")) {
            config.set("location-display.players-settings." + e.getPlayer().getUniqueId() + ".show-mode", "country");
        }
        if (config.contains("location-display.players-settings." + e.getPlayer().getUniqueId() + ".show-mode")) {
            String showMode = config.getString("location-display.players-settings." + e.getPlayer().getUniqueId() + ".show-mode");
            JSONObject json = getLocationByUUID(e.getPlayer().getUniqueId());
            if (showMode != null && showMode.equalsIgnoreCase("province")) {
                e.setFormat(ChatColor.GRAY + "[" + json.get("country") + " " + json.get("regionName") + "]" + ChatColor.WHITE + "<%1$s> %2$s");
            } else if (showMode != null && showMode.equalsIgnoreCase("country")) {
                e.setFormat(ChatColor.GRAY + "[" + json.get("country") + "]" + ChatColor.WHITE + "<%1$s> %2$s");
            } else if (showMode != null && showMode.equalsIgnoreCase("city")) {
                e.setFormat(ChatColor.GRAY + "[" + json.get("country") + " " + json.get("regionName") + " " + json.get("city") + "]" + ChatColor.WHITE + "<%1$s> %2$s");
            }
            config.set("location-display.players-settings." + e.getPlayer().getUniqueId() + ".location-buffer", json.toJSONString());
        }
    }

    @EventHandler
    public void onPlayerChatE2(AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("@all")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(prefix + ChatColor.RED + e.getPlayer().getDisplayName() + ChatColor.GREEN + "在发言中提到了所有人！");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains("@" + player.getDisplayName())) {
                player.sendMessage(prefix + ChatColor.RED + e.getPlayer().getDisplayName() + ChatColor.GREEN + "在发言中提到了你！");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }
        }
    }

    @EventHandler
    public void onPlayerChatE3(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        List<String> public_variables = config.getStringList("variables.public.$defined$");
        List<String> private_variables = config.getStringList("variables.private." + e.getPlayer().getUniqueId() + ".$defined$");
        for (String variable : private_variables) message = message.replaceAll("%" + variable + "%", Objects.requireNonNull(config.getString("variables.private." + e.getPlayer().getUniqueId() + "." + variable)));
        for (String variable : public_variables) message = message.replaceAll("%" + variable + "%", Objects.requireNonNull(config.getString("variables.public." + variable)));
        e.setMessage(message);
    }

    @EventHandler
    public void onPlayerChatE4(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        if (message.startsWith("!")) {
            String tmp = message.substring(1);
            e.setMessage(tmp);
            //long group = config.getLong("qq.group");
            //Objects.requireNonNull(em.bot.getGroup(config.getLong("qq.group"))).sendMessage(tmp);
        }
    }

    @EventHandler
    public void onPlayerChatE5(AsyncPlayerChatEvent e) {
        if (whoAreDoingQuestionnaire.stream().anyMatch(p -> p.first.equals(e.getPlayer().getUniqueId()))) {
            QuestionnaireInstance qi = whoAreDoingQuestionnaire.stream().filter(p -> p.first.equals(e.getPlayer().getUniqueId())).findFirst().get().second;
            if (qi.getCurrentQuestion().type.equals(QuestionType.COMPLETION)) {
                if (e.getMessage().equals(qi.getCurrentQuestion().answer)) {
                    PlayerAnswerQuestionCorrectlyEvent playerAnswerQuestionCorrectlyEvent = new PlayerAnswerQuestionCorrectlyEvent(e.getPlayer(), qi.getCurrentQuestion(), qi);
                    Bukkit.getServer().getPluginManager().callEvent(playerAnswerQuestionCorrectlyEvent);
                    qi.nextQuestion(qi.getCurrentQuestion().score);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                } else {
                    PlayerAnswerQuestionWronglyEvent playerAnswerQuestionWronglyEvent = new PlayerAnswerQuestionWronglyEvent(e.getPlayer(), qi.getCurrentQuestion(), qi);
                    Bukkit.getServer().getPluginManager().callEvent(playerAnswerQuestionWronglyEvent);
                    qi.nextQuestion(0);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
                PlayerAnswerQuestionEvent playerAnswerQuestionEvent = new PlayerAnswerQuestionEvent(e.getPlayer(), qi.getCurrentQuestion(), qi);
                Bukkit.getServer().getPluginManager().callEvent(playerAnswerQuestionEvent);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerTabComplete(TabCompleteEvent e) {
        if (e.getBuffer().endsWith("@")) {
            List<String> suggestions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getDisplayName());
            }
            e.setCompletions(suggestions);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        allowCommands.put(e.getPlayer().getUniqueId(), new ArrayList<>());
        JSONObject json = JSONObject.parse(getLocationInfo(e.getPlayer().getAddress()));
        if (config.contains("location-display.players-settings." + e.getPlayer().getUniqueId() + ".location-buffer")) {
            if (!json.toJSONString().equals(config.getString("location-display.players-settings." + e.getPlayer().getUniqueId() + ".location-buffer"))) {
                e.getPlayer().sendMessage(prefix + "检测到您的位置信息变动，建议使用 /em location-display reload 指令重试");
            }
        } else {
            e.getPlayer().sendMessage(prefix + "已自动获取您的位置信息，如果想关闭位置信息显示，请输入 /em location-display off 命令。");
            config.set("location-display.players-settings." + e.getPlayer().getUniqueId() + ".location-buffer", json.toJSONString());
            config.set("location-display.players-settings." + e.getPlayer().getUniqueId() + ".show-mode", "country");
        }
    }

    @EventHandler
    public void onPlayerInteractMenu(InventoryClickEvent e) {
         if (whoAreDoingQuestionnaire.stream().anyMatch(p -> p.first.equals(e.getWhoClicked().getUniqueId()))) {
             if (e.getInventory().getSize() <= 20) {
                 QuestionnaireInstance doing = whoAreDoingQuestionnaire.stream().filter(p -> p.first.equals(e.getWhoClicked().getUniqueId())).findFirst().get().second;
                 Question question = doing.getCurrentQuestion();
                 Player player = (Player) e.getWhoClicked();
                 if (question.type.equals(QuestionType.CHOICE)) {
                     e.getWhoClicked().closeInventory();
                     if (question.answer.equals(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName()))  {
                         PlayerAnswerQuestionCorrectlyEvent playerAnswerQuestionCorrectlyEvent = new PlayerAnswerQuestionCorrectlyEvent(player, question, doing);
                         Bukkit.getServer().getPluginManager().callEvent(playerAnswerQuestionCorrectlyEvent);
                         doing.nextQuestion(question.score);
                         player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                     } else {
                         PlayerAnswerQuestionWronglyEvent playerAnswerQuestionWronglyEvent = new PlayerAnswerQuestionWronglyEvent(player, question, doing);
                         Bukkit.getServer().getPluginManager().callEvent(playerAnswerQuestionWronglyEvent);
                         doing.nextQuestion(0);
                         player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                     }
                     PlayerAnswerQuestionEvent playerAnswerQuestionEvent = new PlayerAnswerQuestionEvent(player, question, doing);
                     Bukkit.getServer().getPluginManager().callEvent(playerAnswerQuestionEvent);
                 } else if (question.type.equals(QuestionType.COMPLETION)) {
                     if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.WRITABLE_BOOK)) {
                         e.getWhoClicked().closeInventory();
                         e.getWhoClicked().sendMessage(prefix + "请输入答案：");
                     }
                 }
                 e.setCancelled(true);
             }
         }
    }

    @EventHandler
    public void onPlayerCloseMenu(InventoryCloseEvent e) {
        if (whoAreDoingQuestionnaire.stream().anyMatch(p -> p.first.equals(e.getPlayer().getUniqueId()))) {
            QuestionnaireInstance questionnaireInstance = whoAreDoingQuestionnaire.stream().filter(p -> p.first.equals(e.getPlayer().getUniqueId())).findFirst().get().second;
            PlayerQuitQuestionnaireAbnormallyEvent playerQuitQuestionnaireAbnormallyEvent = new PlayerQuitQuestionnaireAbnormallyEvent((Player) e.getPlayer(), questionnaireInstance);
            Bukkit.getServer().getPluginManager().callEvent(playerQuitQuestionnaireAbnormallyEvent);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (whoAreDoingQuestionnaire.stream().anyMatch(p -> p.first.equals(e.getPlayer().getUniqueId()))) {
            QuestionnaireInstance questionnaireInstance = whoAreDoingQuestionnaire.stream().filter(p -> p.first.equals(e.getPlayer().getUniqueId())).findFirst().get().second;
            PlayerQuitQuestionnaireAbnormallyEvent playerQuitQuestionnaireAbnormallyEvent = new PlayerQuitQuestionnaireAbnormallyEvent(e.getPlayer(), questionnaireInstance);
            Bukkit.getServer().getPluginManager().callEvent(playerQuitQuestionnaireAbnormallyEvent);
        }
    }

    @EventHandler
    public void onPlayerJoinS(PlayerJoinEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Join", e.getJoinMessage()));
    }

    @EventHandler
    public void onPlayerQuitS(PlayerQuitEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Quit", e.getQuitMessage()));
    }

    @EventHandler
    public void onPlayerInteractS(PlayerInteractEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Interact", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerEnterCommandS(PlayerCommandPreprocessEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "CommandPreprocess", e.getMessage()));
    }

    @EventHandler
    public void onPlayerCommandSendS(PlayerCommandSendEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "CommandSend", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerDeathS(PlayerDeathEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getEntity().getDisplayName(), "Death", e.getDeathMessage()));
    }

    @EventHandler
    public void onPlayerRespawnS(PlayerRespawnEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Respawn", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerBedEnterS(PlayerBedEnterEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "BedEnter", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerBedLeaveS(PlayerBedLeaveEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "BedLeave", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerChangedMainHandS(PlayerChangedMainHandEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "ChangedMainHand", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerChangedMainHandS(PlayerChangedWorldEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "ChangedWorld", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerDropItemS(PlayerDropItemEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "DropItem", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerChatS(AsyncPlayerChatEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Chat", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerBreakBlockS(BlockBreakEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Chat", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent e) {
        log.append(LogEntry.generate(e.getEventName(), e.getPlayer().getDisplayName(), "Chat", objectToJSON(e, 1)));
    }

    @EventHandler
    public void onPlayerDamageOthers(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player) log.append(LogEntry.generate(e.getEventName(), player.getDisplayName(), "DamageOhers", objectToJSON(e, 1)));
    }
}
