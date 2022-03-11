package me.shreyasayyengar.tnttag.arena;

import dev.jcsoftware.jscoreboards.JGlobalMethodBasedScoreboard;
import me.shreyasayyengar.tnttag.game.Game;
import me.shreyasayyengar.tnttag.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Arena {

    public static final int REQUIRED_PLAYERS = 1;

    private final UUID arenaID = UUID.randomUUID();

    private final Set<TntPlayer> players = new HashSet<>();
    private final Set<TntPlayer> taggedPlayers = new HashSet<>();

    // todo configure
    private final Location spawnPoint = new Location(Bukkit.getWorld("world"), -16, 73, -46);
    private final Location regroupSpawnPoint = new Location(Bukkit.getWorld("world"), -16, 73, -46);
    private final Location spectatorSpawnPoint = new Location(Bukkit.getWorld("world"), 8, 0, 0);

    private final Game game;
    private final JGlobalMethodBasedScoreboard taggedScoreboard;
    private final JGlobalMethodBasedScoreboard regularScoreboard;

    public Arena() {
        GameManager.createArena(this);
        this.game = new Game(this);

        taggedScoreboard = new JGlobalMethodBasedScoreboard();
        regularScoreboard = new JGlobalMethodBasedScoreboard();

        registerScoreboards();
    }

    private void registerScoreboards() {
        taggedScoreboard.setTitle("&e&lTNT Tag");
        regularScoreboard.setTitle("&e&lTNT Tag");
    }

    public void broadcast(String message) {
        for (TntPlayer player : players) {
            player.toBukkitPlayer().sendMessage(message);
        }
    }

    public List<TntPlayer> dictateRoundTagged() {
        int forTagged = (30 * getPlayers().size()) / 100;

        List<TntPlayer> players = getPlayers().stream().toList();
        int random = (int) (Math.random() * forTagged);
//        return players.subList(random, players.size());

        return players.subList(0, 1);
    }
    public Set<TntPlayer> getPlayers() {
        return players;
    }

    public Set<TntPlayer> getTaggedPlayers() {
        return taggedPlayers;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public Location getRegroupSpawnPoint() {
        return regroupSpawnPoint;
    }

    public Location getSpectatorSpawnPoint() {
        return spectatorSpawnPoint;
    }

    public Game getGame() {
        return game;
    }

    public JGlobalMethodBasedScoreboard getTaggedScoreboard() {
        return taggedScoreboard;
    }

    public JGlobalMethodBasedScoreboard getRegularScoreboard() {
        return regularScoreboard;
    }

    public void addPlayer(Player toJoin) {
        TntPlayer player = new TntPlayer(toJoin.getUniqueId(), this);
        players.add(player);
        toJoin.teleport(spawnPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return Objects.equals(arenaID, arena.arenaID);
    }
}
