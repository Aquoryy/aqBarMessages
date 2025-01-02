package org.aqu0ryy.barmessages.commands;

import com.google.common.collect.Lists;
import org.aqu0ryy.barmessages.Loader;
import org.aqu0ryy.barmessages.configs.Config;
import org.aqu0ryy.barmessages.hooks.HookManager;
import org.aqu0ryy.barmessages.utils.BarUtil;
import org.aqu0ryy.barmessages.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandMessage extends AbstractCommand {

    private final Loader plugin;
    private final Config config;
    private final ChatUtil chatUtil;
    private final BarUtil barUtil;
    private final HookManager hookManager;

    public CommandMessage(Loader plugin, Config config, ChatUtil chatUtil, BarUtil barUtil, HookManager hookManager) {
        super(plugin, "aqbarmessages");
        this.plugin = plugin;
        this.config = config;
        this.chatUtil = chatUtil;
        this.barUtil = barUtil;
        this.hookManager = hookManager;
    }

    @Override public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            String help = sender.hasPermission("aqbarmessages.admin") ? "plugin-messages.admin-help" : "plugin-messages.user-help";

            for (String message : config.get().getStringList(help)) {
                chatUtil.sendMessage(sender, message.replace("{label}", label));
            }
        } else {
            if (sender.hasPermission("aqbarmessages.admin")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reload();
                    chatUtil.sendMessage(sender, config.get().getString("command-messages.reload.success"));
                    return;
                }
            } else {
                chatUtil.sendMessage(sender, config.get().getString("plugin-messages.no-perm"));
                return;
            }

            if (sender instanceof Player) {
                String message = String.join(" ", args);

                if (message.length() >= config.get().getInt("text-settings.min-length") && message.length() <= config.get().getInt("text-settings.max-length")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (barUtil.hasBossBars(player)) {
                            if (hookManager.checkHook((Player) sender)) {
                                barUtil.sendBossBar(player, config.get().getString("bar-settings.format").replace("{message}", message).replace("{player}", sender.getName()),
                                        BarColor.valueOf(config.get().getString("bar-settings.color")), BarStyle.valueOf(config.get().getString("bar-settings.style")), config.get().getInt("bar-settings.duration"));
                            }
                        } else {
                            chatUtil.sendMessage(sender, config.get().getString("command-messages.args.already"));
                        }
                    }
                } else {
                    chatUtil.sendMessage(sender, config.get().getString("command-messages.args.no-length"));
                }
            } else {
                chatUtil.sendMessage(sender, config.get().getString("plugin-messages.no-console"));
            }
        }
    }

    @Override public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("aqbarmessages.admin")) {
                return Lists.newArrayList("reload", "сообщение");
            } else {
                return Lists.newArrayList("сообщение");
            }
        }

        return Lists.newArrayList();
    }
}
