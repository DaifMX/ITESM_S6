package net.daifo.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight Pattern Example
 *
 * Scenario: A 2D tile-based map (think roguelike / strategy game) where the
 * world is hundreds of thousands of tiles. Each tile has:
 *
 *     - Intrinsic state (shared, immutable): kind of terrain, its display
 *       glyph, base movement cost, whether it blocks line of sight, tint
 *       color. These are the same for every "grass" tile on the map.
 *
 *     - Extrinsic state (unique per tile): the (x, y) coordinates and any
 *       per-tile modifiers like "on fire" or elevation. Passed in at use
 *       time; never stored in the flyweight.
 *
 * A naive design would allocate one full object per tile (grass * 1_000_000
 * copies of the same fields). The Flyweight pattern shares the intrinsic
 * half through {@link TerrainFlyweightFactory}, so a million-tile world
 * keeps only a handful of {@link Terrain} instances in memory.
 */
public class FlyweightExample {

    // ---------- Flyweight (intrinsic, shared, immutable) ----------
    public static final class Terrain {
        private final String kind;
        private final char glyph;
        private final int baseMoveCost;
        private final boolean blocksSight;
        private final String tintHex;

        // package-private: only the factory should construct these
        Terrain(String kind, char glyph, int baseMoveCost,
                boolean blocksSight, String tintHex) {
            this.kind = kind;
            this.glyph = glyph;
            this.baseMoveCost = baseMoveCost;
            this.blocksSight = blocksSight;
            this.tintHex = tintHex;
        }

        /** Render this terrain at a specific map location (extrinsic state). */
        public String render(int x, int y, boolean onFire) {
            char g = onFire ? '^' : glyph;
            return String.format("(%d,%d) %c  kind=%s tint=%s sight=%s cost=%d%s",
                    x, y, g, kind, tintHex, !blocksSight,
                    moveCostAt(onFire),
                    onFire ? "  [BURNING]" : "");
        }

        /** Combine intrinsic base cost with extrinsic modifier. */
        public int moveCostAt(boolean onFire) {
            return onFire ? baseMoveCost + 5 : baseMoveCost;
        }

        public String kind() { return kind; }
    }

    // ---------- Flyweight Factory ----------
    public static final class TerrainFlyweightFactory {
        private final Map<String, Terrain> pool = new HashMap<>();

        public Terrain get(String kind) {
            Terrain cached = pool.get(kind);
            if (cached != null) return cached;

            Terrain created;
            switch (kind) {
                case "grass":
                    created = new Terrain("grass", '.', 1, false, "#6ab04c");
                    break;
                case "forest":
                    created = new Terrain("forest", 'T', 3, true,  "#218c54");
                    break;
                case "water":
                    created = new Terrain("water", '~', 4, false, "#3498db");
                    break;
                case "mountain":
                    created = new Terrain("mountain", '^', 6, true,  "#7f8c8d");
                    break;
                case "road":
                    created = new Terrain("road",  '=', 0, false, "#bdc3c7");
                    break;
                default:
                    throw new IllegalArgumentException("unknown terrain: " + kind);
            }
            pool.put(kind, created);
            return created;
        }

        public int distinctFlyweights() { return pool.size(); }
    }

    // ---------- Context (stores only the extrinsic state + a reference) ----------
    /**
     * One of these exists per map cell, but it is tiny — it stores only the
     * coordinates, a boolean, and a pointer to the shared {@link Terrain}.
     * The heavyweight display/physics data lives exactly once per kind.
     */
    public static final class Tile {
        private final int x, y;
        private final Terrain terrain;   // shared flyweight reference
        private boolean onFire;

        public Tile(int x, int y, Terrain terrain) {
            this.x = x;
            this.y = y;
            this.terrain = terrain;
        }

        public void ignite() { this.onFire = true; }

        public String describe() {
            return terrain.render(x, y, onFire);
        }

        public int moveCost() {
            return terrain.moveCostAt(onFire);
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        TerrainFlyweightFactory terrains = new TerrainFlyweightFactory();

        // A tiny 10x4 map, encoded as a grid. In a real game this would be
        // hundreds of thousands of tiles — but still only 5 Terrain objects.
        String[] map = {
                "gggTTTmmmg",
                "ggRRRRRRgg",
                "gwwwgggTTg",
                "wwwgggmmmm",
        };

        Tile[][] world = new Tile[map.length][map[0].length()];
        int totalTiles = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length(); x++) {
                String kind = kindFor(map[y].charAt(x));
                world[y][x] = new Tile(x, y, terrains.get(kind));
                totalTiles++;
            }
        }

        // Extrinsic mutation — the flyweight itself is never modified.
        world[0][3].ignite(); // set a forest tile on fire
        world[1][5].ignite(); // set a road tile on fire

        // Print a few samples to prove extrinsic state still works per-tile.
        System.out.println(world[0][0].describe());
        System.out.println(world[0][3].describe());
        System.out.println(world[1][5].describe());
        System.out.println(world[3][9].describe());

        System.out.println();
        System.out.println("Total tiles in world:       " + totalTiles);
        System.out.println("Distinct Terrain instances: " + terrains.distinctFlyweights());
        System.out.println("Sharing ratio:              "
                + totalTiles + " : " + terrains.distinctFlyweights());
    }

    private static String kindFor(char c) {
        switch (c) {
            case 'g': return "grass";
            case 'T': return "forest";
            case 'w': return "water";
            case 'm': return "mountain";
            case 'R': return "road";
            default: throw new IllegalArgumentException("bad map char: " + c);
        }
    }
}
