package com.sen;

import com.sen.QuestionnaireCore.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

import static com.sen.Toolkit.*;

public final class em extends JavaPlugin {

    public static List<com.sen.Command> registerCommands = new ArrayList<>();

    public static em getInstance() {
        return JavaPlugin.getPlugin(em.class);
    }

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
        Function<Pair<String[], CommandSender>, Boolean> f = a -> {
            a.second.sendMessage(prefix + "本指令为测试指令，您输入的参数为" + Arrays.toString(a.first));
            return true;
        };
        registerCommand("test", f);
        Toolkit.registerQuestionnaire(new TestQuestionnaire());
        List<String> questionnaireNames = config.getStringList("questionnaires.$registry$");
        for (String questionnaireName : questionnaireNames) {
            String name = config.getString("questionnaires." + questionnaireName + ".name");
            String description = config.getString("questionnaires." + questionnaireName + ".description");
            String title = config.getString("questionnaires." + questionnaireName + ".title");
            Questionnaire questionnaire = new Questionnaire(name, description, title);
            List<String> questionNames = config.getStringList("questionnaires." + questionnaireName + ".$registry$");
            for (String questionName : questionNames) {
                String qName = config.getString("questionnaires." + questionnaire.name + ".questions." + questionName + ".name");
                String qDescription = config.getString("questionnaires." + questionnaire.name + ".questions." + questionName + ".description");
                String[] qChoices = config.getStringList("questionnaires." + questionnaire.name + ".questions." + questionName + ".choices").toArray(new String[0]);
                boolean qIsAnswerable = config.getBoolean("questionnaires." + questionnaire.name + ".questions." + questionName + ".isAnswerable");
                int qScore = config.getInt("questionnaires." + questionnaire.name + ".questions." + questionName + ".score");
                QuestionType qType = (QuestionType) config.get("questionnaires." + questionnaire.name + ".questions." + questionName + ".type");
                String qAnswer = config.getString("questionnaires." + questionnaire.name + ".questions." + questionName + ".answer");
                Question question = new Question(qIsAnswerable, qType, qName, qDescription, qAnswer, qChoices, qScore);
                questionnaire.addQuestion(question);
            }
            registerQuestionnaire(questionnaire);
        }
        if (questionnaires == null) questionnaires = new ArrayList<>();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        List<String> questionnaireNames = new ArrayList<>();
        for (Questionnaire questionnaire : questionnaires) {
            questionnaireNames.add(questionnaire.name);
            config.set("questionnaires." + questionnaire.name + ".name", questionnaire.name);
            config.set("questionnaires." + questionnaire.name + ".description", questionnaire.description);
            config.set("questionnaires." + questionnaire.name + ".title", questionnaire.title);
            List<String> questionNames = new ArrayList<>();
            for (Question question : questionnaire.questions) {
                questionNames.add(question.name);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".name", question.name);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".description", question.description);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".type", question.type);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".choices", question.choices);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".isAnswerable", question.isAnswerable);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".score", question.score);
                config.set("questionnaires." + questionnaire.name + ".questions." + question.name + ".answer", question.answer);
            }
            config.set("questionnaires." + questionnaire.name + ".$registry$", questionNames);
        }
        config.set("questionnaires.$registry$", questionnaireNames);
        saveConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
                } else if (args[0].equalsIgnoreCase("toolkit")) {
                    if (args[1].equalsIgnoreCase("ping")) {
                        if (args.length == 3) player.sendMessage(prefix + "您的延迟：" + player.getPing() + "ms");
                        else player.sendMessage(prefix + args[3] + "的延迟：" + Objects.requireNonNull(Bukkit.getServer().getPlayer(args[3])).getPing() + "ms");
                    }
                } else if (args[0].equalsIgnoreCase("questionnaire")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        String title = args[2], name = args[3], description = args[4];
                        registerQuestionnaire(new Questionnaire(title, name, description));
                    } else if (args[1].equalsIgnoreCase("add_question")) {
                        String name = args[2];
                        Questionnaire q = nameMatchesQuestionnaire(name);
                        String title = args[3], description = args[4], typetmp = args[5], answer = args[6];
                        int score = Integer.parseInt(args[7]);
                        QuestionType type = (typetmp.equalsIgnoreCase("choice") ? QuestionType.CHOICE : typetmp.equalsIgnoreCase("completion") ? QuestionType.COMPLETION : QuestionType.SHORT_ANSWER);
                        q.addQuestion(new Question(true, type, title, description, answer, ((!type.equals(QuestionType.CHOICE)) ? new String[0] : Arrays.copyOfRange(args, 8, args.length)), score));
                    } else if (args[1].equalsIgnoreCase("conduct")) {
                        QuestionnaireInstance qi = nameMatchesQuestionnaire(args[2]).conduct(Bukkit.getPlayer(args[3]), true, true, true, true);
                        qi.start();
                    }
                } else {
                    Optional<com.sen.Command> optionalCommand = registerCommands.stream()
                            .filter(command -> command.rootCmd.equalsIgnoreCase(args[0]))
                            .findFirst();

                    if (optionalCommand.isPresent()) {
                        com.sen.Command command = optionalCommand.get();
                        try {
                            return command.run(Arrays.copyOfRange(args, 1, args.length), sender);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return true;
                        }
                    }
                    player.sendMessage(prefix + "未找到指令：" + args[0]);
                    return true;
                }
            }
        } catch (Exception ex) {
            player.sendMessage(prefix + "输入的参数有问题！");
            throw new RuntimeException(ex);
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
                result.add("toolkit");
                result.add("questionnaire");
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
                } else if (args[0].equalsIgnoreCase("toolkit")) {
                    result.add("ping");
                } else if (args[0].equalsIgnoreCase("questionnaire")) {
                    result.add("create");
                    result.add("add_question");
                    result.add("conduct");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("var")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        result.add("public");
                        result.add("private");
                    }
                } else if (args[0].equalsIgnoreCase("toolkit")) {
                    if (args[1].equalsIgnoreCase("ping")) {

                    }
                } else if (args[0].equalsIgnoreCase("questionnaire")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        result.add("<title>");
                    } else if (args[1].equalsIgnoreCase("add_question")) {
                        for (Questionnaire q : questionnaires) result.add(q.name);
                    } else if (args[1].equalsIgnoreCase("conduct")) {
                        for (Questionnaire q : questionnaires) result.add(q.name);
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("questionnaire")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        result.add("<name>");
                    } else if (args[1].equalsIgnoreCase("add_question")) {
                        result.add("<title>");
                    } else {
                        result.add(ChatColor.LIGHT_PURPLE + "Tab补全不可用！");
                    }
                }
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("questionnaire")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        result.add("<description>");
                    } else if (args[1].equalsIgnoreCase("add_question")) {
                        result.add("<description>");
                    } else {
                        result.add(ChatColor.LIGHT_PURPLE + "Tab补全不可用！");
                    }
                }
            } else if (args.length == 6) {
                if (args[1].equalsIgnoreCase("add_question")) {
                    result.add("CHOICE");
                    result.add("COMPLETION");
                    result.add("SHORT_ANSWER");
                }
            } else if (args.length == 7) {
                if (args[1].equalsIgnoreCase("add_question")) {
                    result.add("<answer>");
                }
            } else if (args.length == 8) {
                if (args[1].equalsIgnoreCase("add_question")) {
                    result.add("<score>");
                }
            }
            else {
                if (args[1].equalsIgnoreCase("add_question")) {
                    result.add("<choices>");
                } else result.add(ChatColor.LIGHT_PURPLE + "Tab补全不可用！");
            }
        }
        return result.isEmpty() ? super.onTabComplete(sender, cmd, alias, args) : result;
    }

    public boolean registerCommand(String rootCmd, Function<Pair<String[], CommandSender>, Boolean> runnable) {
        return registerCommands.add(new com.sen.Command(rootCmd, runnable));
    }
}
