package me.notenoughskill.harvesterhoe.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TokensCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /tokens <help|balance|reset|pay|give|remove|set>");
            return true;
        }

        if (args[0].equals("help")) {
            // Show help
            return true;
        } else if (args[0].equals("balance") || args[0].equals("bal")) {
            // Check Balance
            return true;
        } else if (args[0].equals("reset")) {
            // Reset Balance
            return true;
        } else if (args[0].equals("pay")) {
            // Pay Tokens
            return true;
        } else if (args[0].equals("give")) {
            // Give Tokens
            return true;
        } else if (args[0].equals("remove")) {
            // Remove Tokens
            return true;
        } else if (args[0].equals("set")) {
            // Set Tokens
            return true;
        }
        return false;
    }
}
