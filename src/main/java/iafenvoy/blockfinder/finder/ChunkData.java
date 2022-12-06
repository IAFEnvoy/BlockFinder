package iafenvoy.blockfinder.finder;

import com.ibm.icu.impl.Pair;
import iafenvoy.blockfinder.render.Color;
import iafenvoy.blockfinder.util.TripleGroup;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChunkData {
    private final int chunkX, chunkZ;
    private final CopyOnWriteArrayList<TripleGroup<BlockPos, Block, Color>> pos = new CopyOnWriteArrayList<>();
    private final World world;

    public ChunkData(int chunkX, int chunkZ, World world) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = world;
    }

    public boolean match(int chunkX, int chunkZ) {
        return this.chunkX == chunkX && this.chunkZ == chunkZ;
    }

    public List<TripleGroup<BlockPos, Block, Color>> getPos() {
        return pos;
    }

    public void updateBlock(BlockPos pos, Block block, List<Pair<Block, Color>> blocks) {
        this.pos.removeIf(e -> e.first.equals(pos));
        for (Pair<Block, Color> pair : blocks)
            if (pair.first == block) {
                this.pos.add(TripleGroup.of(pos, block, pair.second));
                break;
            }
    }

    public void addFinder(Block block, Color color) {
        ChunkPos pos = new ChunkPos(chunkX, chunkZ);
        Chunk chunk = world.getChunk(pos.getStartPos());
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = 0; y < 256; y++)
                    if (block == chunk.getBlockState(new BlockPos(x, y, z)).getBlock())
                        this.pos.add(TripleGroup.of(new BlockPos(x, y, z).add(pos.getStartPos()), block, color));
    }

    public void removeFinder(Block block) {
        this.pos.removeIf(x -> x.second == block);
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void clear() {
        this.pos.clear();
    }
}
