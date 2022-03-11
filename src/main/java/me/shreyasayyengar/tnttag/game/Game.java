package me.shreyasayyengar.tnttag.game;

import me.shreyasayyengar.tnttag.TntTagPlugin;
import me.shreyasayyengar.tnttag.arena.Arena;
import me.shreyasayyengar.tnttag.arena.ArenaCountdown;
import me.shreyasayyengar.tnttag.arena.TntPlayer;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Arena arena;
    private GameState state;
    private int roundTime = 35;
    private int roundNumber = 1;

    public Game(Arena arena) {
        this.arena = arena;
        setState(GameState.WAITING);
    }

    public void setState(GameState newState) {
        this.state = newState;
        switch (newState) {
            case WAITING -> shouldStartCountdown();
            case COUNTDOWN -> startCountdown();
            case LIVE -> startGame();
            case WON -> {
            }
        }
    }

    private void startGame() {
        List<Player> bukkitPlayers = arena.getPlayers().stream().map(TntPlayer::toBukkitPlayer).toList();
        bukkitPlayers.forEach(player -> {
            player.teleport(arena.getRegroupSpawnPoint());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2, false, false, false));
        });

        List<TntPlayer> tntPlayers = arena.dictateRoundTagged();
        tntPlayers.forEach(tntPlayer -> {
            tntPlayer.setTagged();
            tntPlayer.toBukkitPlayer().sendMessage("You started this round being the IT!");
            tntPlayer.toBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 3, false, false, false));
        });

        startRoundRunnable();

    }

    private void startRoundRunnable() {

        List<String> taggedLines = new ArrayList<>(List.of(
                "&7Round #" + roundNumber,
                "",
                "explosion_timer",
                "",
                "Goal: &cTag Someone!"
        ));

        List<String> regularLines = new ArrayList<>(List.of(
                "&7Round #" + roundNumber,
                "",
                "explosion_timer",
                "",
                "Goal: &aRun Away!"
        ));

        arena.getTaggedPlayers().forEach(taggedPlayer -> arena.getTaggedScoreboard().addPlayer(taggedPlayer.toBukkitPlayer()));
        CollectionUtils.subtract(arena.getPlayers(), arena.getTaggedPlayers()).forEach(regularPlayer -> arena.getRegularScoreboard().addPlayer(regularPlayer.toBukkitPlayer()));

        new BukkitRunnable() {

            boolean roundCharging = true;

            @Override
            public void run() {

                if (roundNumber != 5) {

                    if (roundTime != 0) {
                        if (Game.this.roundTime <= 5) {
                            taggedLines.set(2, "&eExplosion in &c" + Game.this.roundTime + "s");
                            regularLines.set(2, "&eExplosion in &c" + Game.this.roundTime + "s");
                        } else {
                            taggedLines.set(2, "&eExplosion in &a" + Game.this.roundTime + "s");
                            regularLines.set(2, "&eExplosion in &a" + Game.this.roundTime + "s");
                        }

                        // set scoreboard lines
                        arena.getTaggedScoreboard().setLines(taggedLines);
                        arena.getRegularScoreboard().setLines(regularLines);

                        // update scoreboard
                        arena.getTaggedScoreboard().updateScoreboard();
                        arena.getRegularScoreboard().updateScoreboard();

                        Game.this.roundTime--;
                    } else {
                        System.out.println("Elminated tagged players");

                    }
                } else {
                    // TODO: End game and annouce winner
                    cancel();
                }

            }

        }.runTaskTimer(TntTagPlugin.getInstance(), 20L, 20);
    }

    private void eliminateTaggedPlayers() {
        arena.getTaggedPlayers().forEach(TntPlayer::eliminate);

        arena.getTaggedPlayers().clear();
        arena.getRegularScoreboard().updateScoreboard();
        roundNumber++;
        roundTime = 35;
    }

    private void startCountdown() {
        new ArenaCountdown(arena).begin();
    }

    private void shouldStartCountdown() {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (arena.getPlayers().size() >= Arena.REQUIRED_PLAYERS) {
                    setState(GameState.COUNTDOWN);
                    cancel();
                }
            }
        }.runTaskTimer(TntTagPlugin.getInstance(), 0, 20);
    }
}
