package me.shreyasayyengar.tnttag.arena;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TntPlayer /* extends AbstractGamePlayer */ {

    private final UUID uuid;
    private final Arena currentArena;
    private boolean isTagged;

    public TntPlayer(UUID uuid, Arena currentArena) {
        this.uuid = uuid;
        this.currentArena = currentArena;
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void tagPlayer(TntPlayer taggedPlayer) {
        isTagged = false;
        if (toBukkitPlayer() != null) {
            Player player = toBukkitPlayer();
            player.setPlayerListName(toBukkitPlayer().getName());
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.sendMessage("§cYou have tagged " + taggedPlayer.toBukkitPlayer().getName());
            currentArena.getTaggedPlayers().remove(this);
        }
        taggedPlayer.setTagged();
        taggedPlayer.toBukkitPlayer().sendMessage("§cYou have been tagged by " + toBukkitPlayer().getName());
    }

    public void setTagged() {
        isTagged = true;

        if (toBukkitPlayer() != null) {

            Player player = toBukkitPlayer();
            player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
            player.setPlayerListName(" §c[IT]" + toBukkitPlayer().getName());
            player.getInventory().setHelmet(new ItemStack(Material.TNT));
            currentArena.getTaggedPlayers().add(this);
        }
    }

    public void eliminate() {
        Player player = toBukkitPlayer();
        player.setPlayerListName(" §7[ELIMINATED] " + toBukkitPlayer().getName());
        player.sendMessage("§cYou have been eliminated from the game!");
        player.getInventory().clear();

        currentArena.getTaggedScoreboard().removePlayer(this.toBukkitPlayer());
        currentArena.getRegularScoreboard().addPlayer(this.toBukkitPlayer());
    }

    public UUID getUUID() {
        return uuid;
    }
}
