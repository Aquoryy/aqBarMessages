package org.aqu0ryy.barmessages.hooks;

import net.milkbowl.vault.economy.Economy;
import org.aqu0ryy.barmessages.Loader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Economy economy = null;
    private final Loader plugin;

    public VaultHook(Loader plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

    public void takeMoney(Player player, int money) {
        economy.withdrawPlayer(player, money);
    }

    public int getMoney(Player player) {
        return (int) economy.getBalance(player);
    }
}
