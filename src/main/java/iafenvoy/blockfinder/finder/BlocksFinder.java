package iafenvoy.blockfinder.finder;

import com.ibm.icu.impl.Pair;
import iafenvoy.blockfinder.render.Color;
import iafenvoy.blockfinder.render.Cube;
import iafenvoy.blockfinder.util.TripleGroup;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlocksFinder {
    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(5);
    private static final CopyOnWriteArrayList<ChunkData> data = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Pair<Block, Color>> blocks = new CopyOnWriteArrayList<>();
    private static int range = 10;
    public static boolean parseOnData = true;

    public static void onChunkData(World world, int chunkX, int chunkZ) {
        if (!parseOnData) return;
        SERVICE.submit(() -> {
            try {
                ChunkData cd = new ChunkData(chunkX, chunkZ, world);
                for (Pair<Block, Color> block : blocks)
                    cd.addFinder(block.first, block.second);
                data.add(cd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void addBlock(Block block, int r, int g, int b) {
        Color color = new Color(r, g, b);
        blocks.add(Pair.of(block, color));
        if (parseOnData)
            for (ChunkData cd : data)
                SERVICE.submit(() -> cd.addFinder(block, color));
    }

    public static boolean hadBlock(Block block) {
        for (Pair<Block, Color> b : blocks)
            if (b.first == block) return true;
        return false;
    }

    public static List<Pair<Block, Color>> getBlockFinders() {
        return blocks;
    }

    public static void removeBlock(Block block) {
        blocks.removeIf(x -> x.first == block);
        for (ChunkData cd : data)
            SERVICE.submit(() -> cd.removeFinder(block));
    }

    public static void onChunkUnload(int chunkX, int chunkZ) {
        data.removeIf(x -> x.match(chunkX, chunkZ));
    }

    public static void addTo(List<Cube> renderers, int chunkX, int chunkZ) {
        for (ChunkData d : data)
            if (inRange(d.getChunkX(), d.getChunkZ(), chunkX, chunkZ, range))
                for (TripleGroup<BlockPos, Block, Color> pos : d.getPos())
                    renderers.add(new Cube(pos.first, pos.third));
    }

    private static boolean inRange(int cX, int cZ, int pX, int pZ, int dis) {
        return Math.abs(cX - pX) + Math.abs(cZ - pZ) <= dis;
    }

    public static void setRange(int range) {
        BlocksFinder.range = range;
    }

    public static void onBlockUpdate(BlockPos pos, Block block) {
        ChunkPos chunk = new ChunkPos(pos);
        for (ChunkData d : data)
            if (d.match(chunk.x, chunk.z)) {
                d.updateBlock(pos, block, blocks);
                break;
            }
    }

    public static void clear() {
        for (ChunkData d : data)
            d.clear();
    }
}
