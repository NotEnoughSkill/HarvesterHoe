package me.notenoughskill.harvesterhoe;

import me.notenoughskill.harvesterhoe.commands.HarvesterHoeCommand;
import me.notenoughskill.harvesterhoe.commands.TokensCommand;
import me.notenoughskill.harvesterhoe.listeners.HarvesterHoeCropListener;
import me.notenoughskill.harvesterhoe.listeners.HarvesterHoeListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class HarvesterHoe extends JavaPlugin {
    private static HarvesterHoe instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.getCommand("harvesterhoe").setExecutor(new HarvesterHoeCommand());
        this.getCommand("tokens").setExecutor(new TokensCommand());

        getServer().getPluginManager().registerEvents(new HarvesterHoeListener(), this);
        getServer().getPluginManager().registerEvents(new HarvesterHoeCropListener(), this);
    }

    @Override
    public void onDisable() {}

    public static HarvesterHoe getInstance() {
        return instance;
    }

    public boolean isReplaceSeedEnabled() {
        return getConfig().getBoolean("REPLACE_SEED", true);
    }
}
