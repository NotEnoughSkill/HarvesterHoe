package me.notenoughskill.harvesterhoe.commands;

import me.notenoughskill.harvesterhoe.utils.HarvesterHoeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class HarvesterHoeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /harvesterhoe <give|reload>");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /harvesterhoe give <player>");
                return true;
            }
            Player target = sender.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("Player not found");
                return true;
            }

            int farmedAmount = 0;
            int level = 1;
            int currentExp = 0;
            int maxExp = 100;

            target.getInventory().addItem(HarvesterHoeUtils.createHarvesterHoe(farmedAmount, level, currentExp, maxExp));
            sender.sendMessage("Gave " + target.getName() + " a Harvester Hoe");
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            // reload
            sender.sendMessage("Harvester Hoes Reloaded");
            return true;
        }

        return false;
    }
}
