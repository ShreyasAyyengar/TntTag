package me.shreyasayyengar.tnttag.arena;

import me.shreyasayyengar.tnttag.TntTagPlugin;
import me.shreyasayyengar.tnttag.game.GameState;
import me.shreyasayyengar.tnttag.utils.Utility;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCountdown extends BukkitRunnable {

    private final Arena arena;
    private int seconds;

    public ArenaCountdown(Arena arena) {
        this.arena = arena;
        this.seconds = 5;
    }

    public void begin() {
        this.runTaskTimer(TntTagPlugin.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (seconds == 0) {
            cancel();
            arena.getGame().setState(GameState.LIVE);
            return;
        }

        if (seconds % 30 == 0 || seconds <= 10) {
            if (seconds == 1) {
                arena.broadcast(Utility.colourise("&eThe game will start in &c1 &esecond!"));
            } else {
                arena.broadcast(Utility.colourise("&eThe game will start in &c" + seconds + "&e seconds!"));
            }
        }

        if (arena.getPlayers().size() < Arena.REQUIRED_PLAYERS) {
            cancel();
            arena.getGame().setState(GameState.WAITING);
            arena.broadcast(Utility.colourise("&cToo little players, start cancelled"));
            // todo send title
            return;
        }

        seconds--;
    }

}
