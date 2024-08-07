package me.notenoughskill.harvesterhoe.listeners;

import me.notenoughskill.harvesterhoe.HarvesterHoe;
import me.notenoughskill.harvesterhoe.utils.HarvesterHoeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class HarvesterHoeCropListener implements Listener {

    private static final Set<Material> FARMABLE_CROPS = new HashSet<>();
    private static final Map<String, Map<Material, Integer>> playerCropCounts = new HashMap<>();
    private static final Map<Player, HoeData> hoeDataMap = new HashMap<>();
    public static final Map<Player, Integer> playerTokens = new HashMap<>();

    static {
        FARMABLE_CROPS.add(Material.SUGAR_CANE);
        FARMABLE_CROPS.add(Material.COCOA_BEANS);
        FARMABLE_CROPS.add(Material.WHEAT);
        FARMABLE_CROPS.add(Material.POTATOES);
        FARMABLE_CROPS.add(Material.CARROTS);
        FARMABLE_CROPS.add(Material.NETHER_WART);
        FARMABLE_CROPS.add(Material.BEETROOTS);
        FARMABLE_CROPS.add(Material.MELON);
        FARMABLE_CROPS.add(Material.PUMPKIN);
        FARMABLE_CROPS.add(Material.GLOW_BERRIES);
        FARMABLE_CROPS.add(Material.BAMBOO);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item != null && item.getType() == Material.DIAMOND_HOE && item.getItemMeta() != null && item.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "" + ChatColor.BOLD + "Harvester Hoe")) {
            Material blockType = event.getBlock().getType();
            System.out.println("Block broken: " + blockType);

            if (FARMABLE_CROPS.contains(blockType)) {
                System.out.println("Farmable crop broken: " + blockType);
                ItemMeta meta = item.getItemMeta();
                if (meta == null || meta.getLore() == null) {
                    System.out.println("Meta or lore is null");
                    return;
                }

                List<String> lore = meta.getLore();
                System.out.println("Lore: " + lore);

                if (lore.size() < 5) {
                    System.out.println("Lore size is less than expected");
                    return;
                }

                try {
                    String displayName = item.getItemMeta().getDisplayName();
                    String farmedAmountStr = extractBetween(displayName, "[", "]");
                    if (farmedAmountStr == null) {
                        System.out.println("Could not extract farmed amount from display name");
                        return;
                    }
                    farmedAmountStr = stripColorCodes(farmedAmountStr);
                    System.out.println("Farmed amount: " + farmedAmountStr);

                    String levelStr = stripColorCodes(lore.get(2).split(": ")[1]);
                    String[] expParts = stripColorCodes(lore.get(3).split(": ")[1]).split(" / ");
                    System.out.println("Exp parts: " + Arrays.toString(expParts));

                    if (expParts.length < 2) {
                        System.out.println("Exp parts length is less than 2");
                        return;
                    }
                    String currentExpStr = expParts[0];
                    String maxExpStr = expParts[1];
                    System.out.println("Current EXP: " + currentExpStr + ", Max EXP: " + maxExpStr);

                    int farmedAmount = Integer.parseInt(farmedAmountStr);
                    int level = Integer.parseInt(levelStr);
                    int currentExp = Integer.parseInt(currentExpStr);
                    int maxExp = Integer.parseInt(maxExpStr);
                    int playerTokens = getPlayerTokens(player);

                    farmedAmount++;
                    currentExp++;
                    playerTokens += 1;
                    setPlayerTokens(player, playerTokens);

                    if (currentExp >= maxExp) {
                        level++;
                        currentExp = 0;
                        maxExp += 100;
                    }

                    playerCropCounts.putIfAbsent(player.getName(), new HashMap<>());
                    Map<Material, Integer> cropCounts = playerCropCounts.get(player.getName());
                    cropCounts.put(blockType, cropCounts.getOrDefault(blockType, 0) + 1);

                    HoeData hoeData = new HoeData(farmedAmount, level, currentExp, maxExp);
                    hoeDataMap.put(player, hoeData);

                    HarvesterHoeUtils.updateHarvesterHoe(item, farmedAmount, level, currentExp, maxExp);
                    System.out.println("Updated hoe: farmedAmount=" + farmedAmount + ", level=" + level + ", currentExp=" + currentExp + ", maxExp=" + maxExp);

                    if (HarvesterHoe.getInstance().isReplaceSeedEnabled()) {
                        replaceSeed(event.getBlock(), blockType, player);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String extractBetween(String text, String start, String end) {
        int startIndex = text.indexOf(start);
        int endIndex = text.indexOf(end, startIndex);
        if (startIndex != -1 && endIndex != -1) {
            return text.substring(startIndex + start.length(), endIndex);
        }
        return null;
    }

    public static String stripColorCodes(String input) {
        return input.replaceAll("(?i)§[0-9a-fk-or]", "");
    }

    public static HoeData getHoeData(Player player) {
        return hoeDataMap.getOrDefault(player, new HoeData(0, 1, 0, 100));
    }

    public static class HoeData {
        public final int farmedAmount;
        public final int level;
        public final int currentExp;
        public final int maxExp;

        public HoeData(int farmedAmount, int level, int currentExp, int mapExp) {
            this.farmedAmount = farmedAmount;
            this.level = level;
            this.currentExp = currentExp;
            this.maxExp = mapExp;
        }
    }

    public static int getPlayerTokens(Player player) {
        return playerTokens.getOrDefault(player, 0);
    }

    public static void setPlayerTokens(Player player, int tokens) {
        playerTokens.put(player, tokens);
    }

    public static Map<Material, Integer> getPlayerCropCounts(String playerName) {
        return playerCropCounts.getOrDefault(playerName, new HashMap<>());
    }

    public static Map<String, String> getHoeStats(ItemStack hoe) {
        Map<String, String> stats = new HashMap<>();
        ItemMeta meta = hoe.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            List<String> lore = meta.getLore();
            if (lore.size() >= 4) {
                stats.put("farmedAmount", stripColorCodes(hoe.getItemMeta().getDisplayName().split("\\[")[1].split("\\]")[0]));
                stats.put("level", stripColorCodes(lore.get(2).split(": ")[1]));
                String[] expParts = stripColorCodes(lore.get(3).split(": ")[1]).split(" / ");
                if (expParts.length == 2) {
                    stats.put("currentExp", expParts[0]);
                    stats.put("maxExp", expParts[1]);
                }
            }
        }
        return stats;
    }

    private void replaceSeed(Block block, Material blockType, Player player) {
        Material seedType = getSeedType(blockType);

        if (seedType != null) {
            if (player.getInventory().contains(seedType)) {
                System.out.println("Replacing seed: " + seedType);
                block.setType(seedType);
                player.getInventory().removeItem(new ItemStack(seedType, 1));
            } else {
                System.out.println("Player does not have seed: " + seedType);
            }
        } else {
            System.out.println("No seed type found for crop: " + blockType);
        }
    }


    private Material getSeedType(Material cropType) {
        switch (cropType) {
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case POTATOES:
                return Material.POTATOES;
            case CARROTS:
                return Material.CARROTS;
            case BEETROOTS:
                return Material.BEETROOT_SEEDS;
            case NETHER_WART:
                return Material.NETHER_WART;
            default:
                System.out.println("No seed type found for crop: " + cropType);
                return null;
        }
    }
}
