package me.shreyasayyengar.tnttag.events;

import me.shreyasayyengar.tnttag.arena.Arena;
import me.shreyasayyengar.tnttag.arena.TntPlayer;
import me.shreyasayyengar.tnttag.game.GameManager;
import me.shreyasayyengar.tnttag.game.GameState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class GameEvents implements Listener {

    @EventHandler
    @SuppressWarnings("ConstantConditions")
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.FIREWORK && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }

        if (event.getDamager() instanceof Player tagger && event.getEntity() instanceof Player victim) {

            if (GameManager.getGamePlayer(victim.getUniqueId()) != null && GameManager.getGamePlayer(tagger.getUniqueId()) != null) {

                TntPlayer victimGamePlayer = GameManager.getGamePlayer(victim.getUniqueId());
                TntPlayer taggerGamePlayer = GameManager.getGamePlayer(tagger.getUniqueId());

                if (victimGamePlayer.isTagged()) return;

                if (taggerGamePlayer.isSpectator()) return;

                if (victimGamePlayer.getCurrentArena().equals(taggerGamePlayer.getCurrentArena())) {

                    Arena arena = victimGamePlayer.getCurrentArena();
                    if (arena.getGame().getState() != GameState.LIVE) return;
                    taggerGamePlayer.tagPlayer(victimGamePlayer);
                }
            }
        }
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler
    private void onPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp()) event.setCancelled(true);
    }
}
