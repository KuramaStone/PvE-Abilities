package me.Stone.Magic.abilities.druid;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.material.Sapling;

import me.Stone.Magic.Magic;

public class Druid implements Listener {

	private static Random random = new Random();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBonemealUse(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			if(player.hasPermission(Magic.DRUID_GROW)) {
				ItemStack hand = player.getInventory().getItemInMainHand();
				if(hand.getType() == Material.INK_SACK) {
					if(hand.getDurability() == (short) 15) { // If dye is bonemeal
						Block block = event.getClickedBlock();
						Material mat = block.getType();
						if(block.getState().getData() instanceof Crops || mat == Material.MELON_STEM
								|| mat == Material.PUMPKIN_STEM) {
							if(block.getData() < (byte) 7)
								block.setData((byte) 7);
							else
								return;
						}
						else if(mat == Material.NETHER_WARTS) {
							if(block.getData() < (byte) 3)
								block.setData((byte) 3);
							else
								return;
						}
						else if(mat == Material.SAPLING) {
							growSapling(block, (Sapling) block.getState().getData());
						}
						else {
							return;
						}
						if(player.getGameMode() != GameMode.CREATIVE)
							hand.setAmount(hand.getAmount() - 1);
						addParticleEffects(block.getLocation().add(0.5D, 0.5D, 0.5D));
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void growSapling(Block block, Sapling sapling) {
		TreeType tree = TreeType.TREE;
		switch (sapling.getSpecies()) {
			case ACACIA: 
				tree = TreeType.ACACIA;
				break;
			case BIRCH:
				tree = TreeType.BIRCH;
				break;
			case JUNGLE:
				tree = TreeType.SMALL_JUNGLE;
				break;
			case REDWOOD:
				tree = TreeType.REDWOOD;
				break;
			default:
				tree = TreeType.TREE;
				break;		}
		Material m = block.getType();
		byte b = block.getData();
		block.setType(Material.AIR);
		if(!block.getWorld().generateTree(block.getLocation(), tree)) {
			block.setType(m);
			block.setData(b);
		}
			
	}

	@SuppressWarnings("deprecation")
	private void addParticleEffects(Location location) {
		for(int i = 0; i < 5; i++) {
			Location loc1 = location.clone();
			loc1.add(random.nextDouble() - .5D, random.nextDouble() * 0.5D, random.nextDouble() - .5D);
			location.getWorld().playEffect(loc1, Effect.HAPPY_VILLAGER, 1);
		}
	}

}
