package com.sen.InventoryCore;

import com.sen.QuestionnaireCore.Question;
import com.sen.QuestionnaireCore.QuestionType;
import com.sen.QuestionnaireCore.QuestionnaireInstance;
import com.sen.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryAPI {
    public static Inventory showQuestion(QuestionnaireInstance q, Question question, Player player) {
        Inventory inventory = Bukkit.createInventory(null, 18, q.originalQuestionnaire.title);
        ItemStack titleStack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = titleStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(question.id + ". " + question.name);
            List<String> loreList = new ArrayList<>();
            loreList.add(question.description);
            meta.setLore(loreList);
            titleStack.setItemMeta(meta);
        }
        inventory.setItem(4, titleStack);
        if (question.type.equals(QuestionType.COMPLETION)) {
            ItemStack editStack = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta editMeta = editStack.getItemMeta();
            editMeta.setDisplayName("请键入答案");
            editStack.setItemMeta(editMeta);
            inventory.setItem(13, editStack);
        } else {
            for (int i = 0;i < question.choices.size();i++) {
                ItemStack choiceStack = new ItemStack(Material.PAPER);
                ItemMeta choiceMeta = choiceStack.getItemMeta();
                if (choiceMeta != null) {
                    choiceMeta.setDisplayName(question.choices.get(i).essential);
                    List<String> loreList = new ArrayList<>();
                    loreList.add(question.choices.get(i).view);
                    choiceMeta.setLore(loreList);
                    choiceStack.setItemMeta(choiceMeta);
                }
                inventory.setItem(i + 9, choiceStack);
            }
            Toolkit.questionInventories.put(player.getUniqueId(), inventory);
        }
        return inventory;
    }
}
