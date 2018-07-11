package me.Stone.Magic.abilities.kinetic;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.Stone.Magic.Magic;
import me.Stone.Magic.abilities.Ability;

public class MagicLeap extends Ability {

	public MagicLeap(Magic main) {
		super(main, Material.REDSTONE_TORCH_ON, 200, false, Magic.LEAP_USE, Magic.LEAP_BYPASS);
	}

	@Override
	public void activate(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		Vector direction = player.getLocation().getDirection();
		direction.setY(direction.getY() / 4).multiply(5);
		player.setVelocity(direction);
		player.sendMessage(message("&5You pushed forward with incredible power!"));
		playSound(player, Sound.ITEM_ELYTRA_FLYING);
	}

}
