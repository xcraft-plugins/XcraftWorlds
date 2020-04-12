package me.umbreon.xcraftworlds.utils;

import java.util.Arrays;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;

public class Util {

    public static GameMode DEFAULT_GAMEMODE = GameMode.ADVENTURE;
    public static Difficulty DEFAULT_DIFFICULTY = Difficulty.NORMAL;

    public static GameMode castGameMode(Object o) {
        return castGameMode(o, DEFAULT_GAMEMODE);
    }

    public static GameMode castGameMode(Object o, GameMode defaultGameMode) {
        if (o instanceof GameMode) {
            return (GameMode) o;
        }
        if (o instanceof Number) {
            return GameMode.getByValue((int) o);
        }
        if (o instanceof String) {
            String gm = (String) o;
            if (gm.matches("[0-3]")) {
                return GameMode.getByValue(Util.castInt(gm));
            } else {
                return Arrays.stream(GameMode.values())
                        .filter(mode -> mode.toString().equalsIgnoreCase(gm))
                        .findAny().orElse(defaultGameMode);
            }
        }
        return defaultGameMode;
    }

    public static Difficulty castDifficulty(Object o) {
        return castDifficulty(o, DEFAULT_DIFFICULTY);
    }

    public static Difficulty castDifficulty(Object o, Difficulty defaultDifficulty) {
        if (o instanceof Difficulty) {
            return (Difficulty) o;
        }
        if (o instanceof Number) {
            return Difficulty.getByValue((int) o);
        }
        if (o instanceof String) {
            String gm = (String) o;
            if (gm.matches("[0-3]")) {
                return Difficulty.getByValue(Util.castInt(gm));
            } else {
                return Arrays.stream(Difficulty.values())
                        .filter(mode -> mode.toString().equalsIgnoreCase(gm))
                        .findAny().orElse(defaultDifficulty);
            }
        }
        return defaultDifficulty;
    }

    public static int castInt(Object o) {
        if (o == null) {
            return 0;
        } else if (o instanceof Byte) {
            return (int) (Byte) o;
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Double) {
            return (int) (double) (Double) o;
        } else if (o instanceof Float) {
            return (int) (float) (Float) o;
        } else if (o instanceof Long) {
            return (int) (long) (Long) o;
        } else if (o instanceof String) {
            try {
                return Integer.parseInt((String) o);
            } catch (NumberFormatException ex) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static Boolean castBoolean(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            return ((String) o).equalsIgnoreCase("true");
        } else {
            return false;
        }
    }

    public static String getLocationString(Location location) {
        if (location.getWorld() != null) {
            return location.getWorld().getName() + ","
                    + Math.floor(location.getX()) + ","
                    + Math.floor(location.getY()) + ","
                    + Math.floor(location.getZ());
        } else {
            return null;
        }
    }

    public static Location getSaneLocation(Location loc) {
        double x = Math.floor(loc.getX()) + 0.5;
        double y = loc.getY();
        double z = Math.floor(loc.getZ()) + 0.5;

        return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
    }
}
