package me.Stone.Magic.abilities.pheonix;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Stone.Magic.Magic;

public class Pheonix implements Listener {

	public static final String FIREBALL_NAME = "MAGIC_FIREBALL";
	private static final double LAVA_HEALING = 2, FIRE_HEALING = 1;

	private static Map<OfflinePlayer, Long> healDelay = new HashMap<>();
	private static int delayInMili = 1000;

	@EventHandler
	public void onFireDamage(EntityDamageEvent event) {
		DamageCause cause = event.getCause();
		if(cause == DamageCause.FIRE_TICK || cause == DamageCause.FIRE || cause == DamageCause.LAVA) {
			Entity entity = event.getEntity();
			if(entity instanceof Player) {
				Player player = (Player) entity;

				if(player.hasPermission(Magic.PHEONIX_HEAL)) {
					if(isHealDelayOver(player)) {
						double recover = cause == DamageCause.LAVA ? LAVA_HEALING : FIRE_HEALING;
						double health = Math.min(player.getHealth() + recover, 20);
						player.setHealth(health);
						healDelay.put(player, System.currentTimeMillis() + delayInMili);
					}

					event.setCancelled(true);
				}
			}
		}
	}

	private boolean isHealDelayOver(Player player) {
		if(healDelay.containsKey(player)) {
			long now = System.currentTimeMillis();
			long over = healDelay.get(player);
			if(over <= now) {
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}

}
