package org.aqu0ryy.barmessages.utils;

import org.aqu0ryy.barmessages.Loader;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BarUtil {

    private static final Map<Player, BukkitRunnable> bossBars = new HashMap<>();
    private final Loader plugin;
    private final ChatUtil chatUtil;

    public BarUtil(Loader plugin, ChatUtil chatUtil) {
        this.plugin = plugin;
        this.chatUtil = chatUtil;
    }

    public void sendBossBar(Player player, String text, BarColor color, BarStyle style, int duration) {
        if (hasBossBars(player)) {
            BossBar bossBar = Bukkit.createBossBar(chatUtil.color(text), color, style);

            bossBar.addPlayer(player);
            bossBar.setVisible(true);

            BukkitRunnable runnable = new BukkitRunnable() {
                int time = duration;

                @Override
                public void run() {
                    if (time > 0) {
                        bossBar.setProgress((double) time / duration);
                        time--;
                    } else {
                        bossBar.removePlayer(player);
                        bossBar.setVisible(false);
                        bossBars.remove(player);
                        this.cancel();
                    }
                }
            };

            runnable.runTaskTimer(plugin, 0L, 20L);
            bossBars.put(player, runnable);
        }
    }

    public boolean hasBossBars(Player player) {
        return !bossBars.containsKey(player);
    }
}
