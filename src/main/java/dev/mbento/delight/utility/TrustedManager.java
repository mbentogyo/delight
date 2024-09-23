package dev.mbento.delight.utility;

import com.booksaw.betterTeams.Team;
import dev.mbento.delight.DelightMain;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TrustedManager {
    public enum Plugins{
        BETTER_TEAMS()
    }

    private static final PluginManager pm = DelightMain.getInstance().getServer().getPluginManager();
    private static Plugins plugin;

    /**
     * Initializes the Trusted Players Manager class
     */
    public static void initialize(){
        if (pm.getPlugin("BetterTeams") != null) {
            DelightConsole.sendWithPrefix("BetterTeams plugin detected. It will be used for trusted players mechanics.");
            plugin = Plugins.BETTER_TEAMS;
            return;
        }

        DelightConsole.sendWithPrefix("No teams plugin detected. Trusted players mechanics will not be used.");
        plugin = null;
    }

    /**
     * Get the list of online trusted players of a player
     * @param player the player
     * @return list of trusted players, can be an empty set
     */
    @NotNull
    public static List<Player> getTrustedPlayers(@NotNull Player player){
        if (plugin == Plugins.BETTER_TEAMS) {
            Team team = Team.getTeam(player);

            if (team != null) {
                List<Player> list = new ArrayList<>(Team.getTeam(player).getOnlineMembers());
                for (UUID uuid : team.getAllies().get()) list.addAll(Team.getTeam(uuid).getOnlineMembers());
                return list;
            }
        }

        return Collections.emptyList();
    }


}
