package me.Stone.Magic.abilities.kinetic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.util.Vector;

import me.Stone.Magic.Magic;

public class Kinetic implements Listener {
	
	@EventHandler
	public void onElytraToggle(EntityToggleGlideEvent event) {
		if(event.isGliding()) {
			Entity entity = event.getEntity();
			if(entity instanceof Player) {
				Player player = (Player) entity;
				if(player.hasPermission(Magic.KINETIC_ELYTRA)) {
					Vector velocity = player.getLocation().getDirection();
					velocity.setY(velocity.getY() + 0.75D);
					player.setVelocity(velocity);
				}
			}
		}
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {
		if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.FLY_INTO_WALL) {
			Entity entity = event.getEntity();
			if(entity instanceof Player) {
				Player player = (Player) entity;
				if(player.hasPermission(Magic.KINETIC_FALL)) {
					event.setCancelled(true);
					player.setVelocity(player.getVelocity().setY(0.5D));
				}
			}
		}
	}
	
}
