package com.sen;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Charsets;
import com.sen.Log.Log;
import com.sen.QuestionnaireCore.Questionnaire;
import com.sen.QuestionnaireCore.QuestionnaireInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Toolkit {
    public static final List<Pair<UUID, QuestionnaireInstance>> whoAreDoingQuestionnaire = new ArrayList<>();
    public static final Map<UUID, Inventory> questionInventories = new HashMap<>();
    public static final JavaPlugin plugin = JavaPlugin.getPlugin(em.class);
    public static final Configuration config = plugin.getConfig();
    public static final String prefix = ChatColor.GOLD + "[EasyMinecraft]" + ChatColor.WHITE;
    public static final String apiUrl = "http://ip-api.com/json/";
    public static final HashMap<UUID, List<String[]>> allowCommands = new HashMap<>();
    public static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2048);
    public static final Log log = new Log();
    public static List<Questionnaire> questionnaires = new ArrayList<>();
    public static Questionnaire idMatchesQuestionnaire(long id) {
        Optional<Questionnaire> optional = questionnaires.stream().filter(q -> q.id == id).findFirst();
        return optional.orElse(null);
    }
    public static Questionnaire titleMatchesQuestionnaire(String title) {
        Optional<Questionnaire> optional = questionnaires.stream().filter(q -> Objects.equals(q.title, title)).findFirst();
        return optional.orElse(null);
    }
    @SafeVarargs
    public static<T> boolean isIn(T original, T... list) {
        return Arrays.asList(list).contains(original);
    }
    public static void registerQuestionnaire(Questionnaire q) {
        questionnaires.add(q);
    }
    @SafeVarargs
    public static<T> List<T> createList(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }
    public static boolean isPrimitiveOrString(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class);
    }
    public static<T> JSONObject objectToJSON(T obj, int floor) {
        JSONObject object = new JSONObject();
        if (obj == null) return new JSONObject();
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                if (method.getName().equals("getHandlers") || method.getName().equals("getHandlerList")) continue;
                if (isPrimitiveOrString(method.getReturnType())) {
                    try {
                        Object o = method.invoke(obj);
                        object.put(method.getName(), o);
                    } catch (IllegalAccessException | InvocationTargetException e) {

                    }
                } else if (floor < 5) {
                    if (object.toJSONString().getBytes().length >= (1024 * 16)) continue;
                    if ((method.getReturnType().getPackageName().startsWith("org.bukkit") && !method.getReturnType().getPackageName().startsWith("org.bukkit.craftbukkit")) || method.getReturnType().isAssignableFrom(Serializable.class)) {
                        if (method.getReturnType().isAssignableFrom(Player.class)) {
                            JSONObject o2 = new JSONObject();
                            try {
                                Player player = (Player) method.invoke(obj);
                                if (player == null) continue;
                                o2.put("getDisplayName", player.getDisplayName());
                                o2.put("getName", player.getName());
                                o2.put("getExp", player.getExp());
                                o2.put("getPing", player.getPing());
                                o2.put("getHealth", player.getHealth());
                                object.put(method.getName(), o2);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        } else if (method.getReturnType().isAssignableFrom(LivingEntity.class)) {
                            JSONObject o2 = new JSONObject();
                            try {
                                Entity entity = (Entity) method.invoke(obj);
                                if (entity == null) continue;
                                o2.put("getName", entity.getName());
                                object.put(method.getName(), o2);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        } else if (method.getReturnType().isAssignableFrom(Block.class)) {
                            JSONObject o2 = new JSONObject();
                            try {
                                Block block = (Block) method.invoke(obj);
                                if (block == null) continue;
                                if (block.getState().getType().equals(Material.AIR)) continue;
                                o2.put("getName", block.getState().getType().name());
                                JSONObject loc = new JSONObject();
                                loc.put("getBlockX", block.getLocation().getBlockX());
                                loc.put("getBlockY", block.getLocation().getBlockY());
                                loc.put("getBlockZ", block.getLocation().getBlockZ());
                                o2.put("getLocation", loc);
                                object.put(method.getName(), o2);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        } else if (method.getReturnType().isAssignableFrom(Location.class)) {
                            JSONObject o2 = new JSONObject();
                            try {
                                Location loc = (Location) method.invoke(obj);
                                if (loc == null) continue;
                                o2.put("getBlockX", loc.getBlockX());
                                o2.put("getBlockY", loc.getBlockY());
                                o2.put("getBlockZ", loc.getBlockZ());
                                object.put(method.getName(), o2);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }
                        try {
                            Object o = method.invoke(obj);
                            if (o == null) continue;
                            object.put(method.getName(), objectToJSON(o, floor + 1));
                        } catch (IllegalAccessException | InvocationTargetException e) {

                        }
                    }
                }
            }
        }
        return object;
    }
    public static List<String> splitOutsideQuotes(String str) {
        List<String> result = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        Stack<Character> quoteStack = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"') {
                if (!quoteStack.isEmpty() && quoteStack.peek() == '"') {
                    quoteStack.pop();
                    if (quoteStack.isEmpty()) {
                        continue;
                    }
                } else {
                    quoteStack.push('"');
                }
            }

            if (quoteStack.isEmpty() && c == ' ') {
                if (!currentToken.isEmpty()) {
                    result.add(currentToken.toString());
                    currentToken.setLength(0); // 重置token
                }
            } else {
                currentToken.append(c);
            }
            if (i == str.length() - 1 && !currentToken.isEmpty()) {
                result.add(currentToken.toString());
            }
        }

        return result;
    }
    public static QuestionnaireInstance matchQuestionnaire(UUID player) {
        Optional<Pair<UUID, QuestionnaireInstance>> optional = whoAreDoingQuestionnaire.stream().filter(pair -> pair.first.equals(player)).findFirst();
        return optional.map(uuidQuestionnaireInstancePair -> uuidQuestionnaireInstancePair.second).orElse(null);
    }
    public static class RSA {
        public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        }

        public static String encrypt(String content, PublicKey publicKey) throws Exception {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        }

        public static String decrypt(String encryptedContent, PrivateKey privateKey) throws Exception {
            byte[] bytes = Base64.getDecoder().decode(encryptedContent);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedText = cipher.doFinal(bytes);
            return new String(decryptedText, StandardCharsets.UTF_8);
        }
        public static String keyToString(Key key) {
            return Base64.getEncoder().encodeToString(key.getEncoded());
        }
    }
    public static String localization(String keyword, UUID player) {
        List<String> list = config.getStringList("localization.registry-words");
        if (list.contains(keyword)) {
            String lang = config.getString("localization.players-settings." + player.toString() + ".lang");
            Configuration langConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(Objects.requireNonNull(plugin.getResource(lang + ".yml")), Charsets.UTF_8));
            return langConfig.getString(keyword);
        }
        return "Unknown";
    }
    public static JSONObject getLocationByUUID(UUID uuid) {
        try {
            return JSONObject.parse(config.getString("location-display.players-settings." + uuid + ".location-buffer"));
        } catch (Exception ignore) {
            return JSONObject.parse(getLocationInfo(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getAddress()));
        }

    }
    public static String getLocationInfo(InetSocketAddress address) {
        System.out.println("Player address: " + address);
        try {
            URL url = new URL(apiUrl + address.getHostName() + "?lang=zh-CN");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                return content.toString();
            } else {
                System.out.println("HTTP GET request failed with response code " + responseCode);
            }

            connection.disconnect();

        } catch (Exception e) {
            return "Unknown";
        }
        return "Unknown";
    }
}
