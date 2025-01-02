package org.aqu0ryy.barmessages;

import org.aqu0ryy.barmessages.commands.CommandMessage;
import org.aqu0ryy.barmessages.configs.Config;
import org.aqu0ryy.barmessages.hooks.HookManager;
import org.aqu0ryy.barmessages.hooks.PlayerPointsHook;
import org.aqu0ryy.barmessages.hooks.VaultHook;
import org.aqu0ryy.barmessages.utils.BarUtil;
import org.aqu0ryy.barmessages.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loader extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        ChatUtil chatUtil = new ChatUtil();
        BarUtil barUtil = new BarUtil(this, chatUtil);
        VaultHook vaultHook = new VaultHook(this);
        PlayerPointsHook playerPointsHook = new PlayerPointsHook(this);

        if (playerPointsHook.setupPlayerPoints() && vaultHook.setupEconomy()) {
            config = new Config(this);
            HookManager hookManager = new HookManager(this, config, vaultHook, playerPointsHook, chatUtil);

            new CommandMessage(this, config, chatUtil, barUtil, hookManager);
        } else {
            chatUtil.sendMessage(Bukkit.getConsoleSender(), "[x] Установите Vault и PlayerPoints.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void reload() {
        config.reload();
    }
}
