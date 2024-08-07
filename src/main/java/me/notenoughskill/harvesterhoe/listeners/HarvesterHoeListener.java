package me.notenoughskill.harvesterhoe.listeners;

import me.notenoughskill.harvesterhoe.utils.HarvesterHoeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HarvesterHoeListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.DIAMOND_HOE && item.getItemMeta() != null && item.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "" + ChatColor.BOLD + "Harvester Hoe")) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Inventory gui = Bukkit.createInventory(null, 27, "Harvester Hoe");

                ItemStack infoItem = new ItemStack(Material.BOOK);
                ItemMeta infoMeta = infoItem.getItemMeta();
                if (infoMeta != null) {
                    infoMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Hoe Info");
                    List<String> lore = new ArrayList<>();
                    Map<String, String> hoeStats = HarvesterHoeCropListener.getHoeStats(item);

                    lore.add(ChatColor.GRAY + "Stats:");
                    lore.add(ChatColor.GRAY + "Farmed: " + ChatColor.WHITE + hoeStats.getOrDefault("farmedAmount", "0"));
                    lore.add(ChatColor.GRAY + "Level: " + ChatColor.WHITE + hoeStats.getOrDefault("level", "1"));
                    lore.add(ChatColor.GRAY + "Experience: " + ChatColor.GREEN + hoeStats.getOrDefault("currentExp", "0") + ChatColor.GRAY + " / " + ChatColor.RED + hoeStats.getOrDefault("maxExp", "100"));

                    lore.add(ChatColor.GRAY + "Farmed Blocks:");
                    Map<Material, Integer> cropCounts = HarvesterHoeCropListener.getPlayerCropCounts(player.getName());
                    for (Map.Entry<Material, Integer> entry : cropCounts.entrySet()) {
                        lore.add(ChatColor.GRAY + " " + entry.getKey().name() + ": " + ChatColor.WHITE + entry.getValue());
                    }

                    infoMeta.setLore(lore);
                    infoItem.setItemMeta(infoMeta);
                }

                ItemStack enchantsItem = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta enchantsMeta = enchantsItem.getItemMeta();
                if (enchantsMeta != null) {
                    enchantsMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Apply Enchants");
                    enchantsItem.setItemMeta(enchantsMeta);
                }

                gui.setItem(12, infoItem);
                gui.setItem(14, enchantsItem);

                player.openInventory(gui);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Harvester Hoe")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                if (clickedItem.getType() == Material.ENCHANTED_BOOK && clickedItem.getItemMeta() != null &&
                        clickedItem.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "Apply Enchants")) {
                    openEnchantmentsGUI(player);
                }
            }
        }
    }

    private void openEnchantmentsGUI(Player player) {
        Inventory enchantmentsGUI = Bukkit.createInventory(null, 27, "Enchantments");

        ItemStack exampleEnchantmentItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta exampleMeta = exampleEnchantmentItem.getItemMeta();
        if (exampleMeta != null) {
            exampleMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Example Enchantment");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Apply this enchantment to your hoe.");
            exampleMeta.setLore(lore);
            exampleEnchantmentItem.setItemMeta(exampleMeta);
        }

        enchantmentsGUI.setItem(13, exampleEnchantmentItem);

        player.openInventory(enchantmentsGUI);
    }
}
