package me.shreyasayyengar.tnttag.arena;

import me.shreyasayyengar.tnttag.TntTagPlugin;
import me.shreyasayyengar.tnttag.utils.Utility;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.UUID;

public class TntPlayer /* extends AbstractGamePlayer */ {

    private final UUID uuid;
    private final Arena currentArena;
    private boolean isTagged;
    private boolean isSpectator = false;

    public TntPlayer(UUID uuid, Arena currentArena) {
        this.uuid = uuid;
        this.currentArena = currentArena;
    }

    private void setSpectator() {
        isSpectator = true;
        Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(TntTagPlugin.getInstance(), toBukkitPlayer()));
    }

    private void summonFireworks(Location location) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.GREEN).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();

        Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        fw2.setFireworkMeta(fwm);
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setTagged() {
        isTagged = true;

        if (toBukkitPlayer() != null) {

            Player player = toBukkitPlayer();
            player.setPlayerListName("§c[IT] " + toBukkitPlayer().getName());
            player.getInventory().setHelmet(new ItemStack(Material.TNT));
            summonFireworks(player.getLocation());
            currentArena.getTaggedPlayers().add(this);
        }
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

    public void prepare() {
        Player player = toBukkitPlayer();
        player.setPlayerListName(player.getName());
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{Utility.air(), Utility.air(), Utility.air(), Utility.air()});
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setExp(0);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);
        player.setHealthScale(20);
        player.setExhaustion(0);
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        Bukkit.getOnlinePlayers().forEach(otherPlayer -> otherPlayer.showPlayer(TntTagPlugin.getInstance(), player));
    }

    public void eliminate() {
        Player player = toBukkitPlayer();
        player.setPlayerListName("§7[ELIMINATED] " + toBukkitPlayer().getName());
        player.sendMessage("§cYou have been eliminated from the game!");
        player.getInventory().clear();
        player.getWorld().createExplosion(player.getLocation(), 1.2F, false, false);

        setSpectator();

        currentArena.getTaggedScoreboard().removePlayer(this.toBukkitPlayer());
        currentArena.getRegularScoreboard().addPlayer(this.toBukkitPlayer());
    }



    /* Getters --------------------- */
    public UUID getUUID() {
        return uuid;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public boolean isSpectator() {
        return isSpectator;
    }
}
