package me.umbreon.xcraftworlds.Generator;

import java.util.HashMap;
import java.util.Map;

import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.generator.ChunkGenerator;

public enum Generator {
    DEFAULT(0),
    FLATLANDS(1),
    VOID(2);

    private final int id;
    private static final Map<Integer, Generator> lookup = new HashMap<>();

    private Generator(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Generator getGenerator(int id) {
        return lookup.get(id);
    }

    public ChunkGenerator getChunkGenerator(XcraftWorlds plugin) {
        switch (id) {
            case 0:
                return (ChunkGenerator) null;
            case 1:
                return new GeneratorFlatlands();
            case 2:
                return new GeneratorVoid();
        }

        return null;
    }

    static {
        for (Generator env : values()) {
            lookup.put(env.getId(), env);
        }
    }

}
