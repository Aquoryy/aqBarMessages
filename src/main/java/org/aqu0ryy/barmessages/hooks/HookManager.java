package org.aqu0ryy.barmessages.hooks;

import org.aqu0ryy.barmessages.Loader;
import org.aqu0ryy.barmessages.configs.Config;
import org.aqu0ryy.barmessages.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HookManager {

    private final Loader plugin;
    private final Config config;
    private final VaultHook vaultHook;
    private final PlayerPointsHook playerPointsHook;
    private final ChatUtil chatUtil;

    public HookManager(Loader plugin, Config config, VaultHook vaultHook, PlayerPointsHook playerPointsHook, ChatUtil chatUtil) {
        this.plugin = plugin;
        this.config = config;
        this.vaultHook = vaultHook;
        this.playerPointsHook = playerPointsHook;
        this.chatUtil = chatUtil;
    }

    public boolean checkHook(Player player) {
        String hook = config.get().getString("hook-settings.use-hook");

        if (hook == null) {
            config.get().set("hook-settings.use-hook", "vault");
            config.save();
            plugin.reload();
            return true;
        }

        switch (hook.toLowerCase()) {
            case "vault":
                if (vaultHook.setupEconomy()) {
                    if (vaultHook.getMoney(player) >= config.get().getInt("hook-settings.money")) {
                        vaultHook.takeMoney(player, config.get().getInt("hook-settings.money"));
                        chatUtil.sendMessage(player, config.get().getString("command-messages.args.success").replace("{money}", config.get().getString("hook-settings.money")));
                        return true;
                    } else {
                        chatUtil.sendMessage(player, config.get().getString("command-messages.args.no-money"));
                    }
                }

                break;
            case "playerpoints":
                if (playerPointsHook.setupPlayerPoints()) {
                    if (playerPointsHook.getMoney(player) >= config.get().getInt("hook-settings.money")) {
                        playerPointsHook.takeMoney(player, config.get().getInt("hook-settings.money"));
                        chatUtil.sendMessage(player, config.get().getString("command-messages.args.success").replace("{money}", config.get().getString("hook-settings.money")));
                        return true;
                    } else {
                        chatUtil.sendMessage(player, config.get().getString("command-messages.args.no-money"));
                    }
                }

                break;
            default:
                config.get().set("hook-settings.use-hook", "vault");
                config.save();
                plugin.reload();
        }

        return false;
    }
}
