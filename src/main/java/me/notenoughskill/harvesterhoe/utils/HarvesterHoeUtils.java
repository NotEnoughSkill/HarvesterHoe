package me.notenoughskill.harvesterhoe.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HarvesterHoeUtils {

    public static ItemStack createHarvesterHoe(int farmedAmount, int level, int currentExp, int maxExp) {
        ItemStack hoe = new ItemStack(Material.DIAMOND_HOE);
        updateHarvesterHoe(hoe, farmedAmount, level, currentExp, maxExp);
        return hoe;
    }

    public static void updateHarvesterHoe(ItemStack hoe, int farmedAmount, int level, int currentExp, int maxExp) {
        ItemMeta meta = hoe.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Harvester Hoe" + ChatColor.GRAY + " [" + farmedAmount + "]");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "");
            lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Stats:");
            lore.add(ChatColor.GRAY + "Level: " + ChatColor.WHITE + level);
            lore.add(ChatColor.GRAY + "Experience: " + ChatColor.GREEN + currentExp + ChatColor.GRAY + " / " + ChatColor.RED + maxExp);
            lore.add(getExpBar(currentExp, maxExp));
            lore.add(ChatColor.GRAY + "");
            lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Enchants: ");

            // DISPLAY ALL CUSTOM ENCHANTS

            meta.setLore(lore);
            hoe.setItemMeta(meta);
        }
    }

    private static String getExpBar(int currentExp, int maxExp) {
        int totalBars = 30;
        int filledBars = (int) ((double)currentExp / maxExp * totalBars);
        int emptyBars = totalBars - filledBars;

        return ChatColor.GREEN + "|".repeat(filledBars) + ChatColor.RED + "|".repeat(emptyBars);
    }
}
