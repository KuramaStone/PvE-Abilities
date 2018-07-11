package me.Stone.Magic;

import org.bukkit.Location;

import net.minecraft.server.v1_12_R1.BlockPosition;

public class MagicUtils {
	
	public static BlockPosition toBlockPosition(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
		return new BlockPosition(x, y, z);
	}

}
