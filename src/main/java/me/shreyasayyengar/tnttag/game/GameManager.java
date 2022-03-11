package me.shreyasayyengar.tnttag.game;

import me.shreyasayyengar.tnttag.arena.Arena;
import me.shreyasayyengar.tnttag.arena.TntPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameManager {

    private static final Set<Arena> arenas = new HashSet<>();

    public static boolean isPlaying(Player player) {
        UUID uuid = player.getUniqueId();

        for (Arena arena : arenas) {
            for (TntPlayer arenaPlayer : arena.getPlayers()) {
                if (arenaPlayer.getUUID().equals(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Arena getArena(Player player) {
        UUID uuid = player.getUniqueId();

        for (Arena arena : arenas) {
            for (TntPlayer arenaPlayer : arena.getPlayers()) {
                if (arenaPlayer.getUUID().equals(uuid)) {
                    return arena;
                }
            }
        }
        return null;
    }

    public static Arena createArena(Arena arena) {
        arenas.add(arena);
        return arena;
    }

    public static void removeArena(Arena arena) {
        arenas.remove(arena);
    }
}
