package me.umbreon.xcraftworlds.Generator;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class GeneratorVoid extends ChunkGenerator {

    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData data = this.createChunkData(world);
        if (x == 0 && z == 0) {
            data.setBlock(0, 64, 0, Material.STONE);
        }
        return data;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.0, 64.0, 0.0);
    }
}
