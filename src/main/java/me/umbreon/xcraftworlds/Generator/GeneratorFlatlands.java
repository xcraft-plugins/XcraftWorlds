package me.umbreon.xcraftworlds.Generator;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class GeneratorFlatlands extends ChunkGenerator {

    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData data = this.createChunkData(world);
        data.setRegion(0, 0, 0, 16, 54, 16, Material.STONE);
        data.setRegion(0, 54, 0, 16, 64, 16, Material.DIRT);
        data.setRegion(0, 64, 0, 16, 65, 16, Material.GRASS);
        return data;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        int x = random.nextInt(200) - 100;
        int z = random.nextInt(200) - 100;
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, (double) x, (double) y, (double) z);
    }
}
