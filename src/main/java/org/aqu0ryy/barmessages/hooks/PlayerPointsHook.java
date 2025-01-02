package org.aqu0ryy.barmessages.hooks;

import org.aqu0ryy.barmessages.Loader;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsHook {

    private static PlayerPointsAPI playerPoints;
    private final Loader plugin;

    public PlayerPointsHook(Loader plugin) {
        this.plugin = plugin;
    }

    public boolean setupPlayerPoints() {
        if (plugin.getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            return false;
        } else {
            playerPoints = PlayerPoints.getInstance().getAPI();
        }

        return true;
    }

    public void takeMoney(Player player, int money) {
        playerPoints.take(player.getUniqueId(), money);
    }

    public int getMoney(Player player) {
        return playerPoints.look(player.getUniqueId());
    }
}
