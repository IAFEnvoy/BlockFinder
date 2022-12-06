package iafenvoy.blockfinder.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class Cuboid extends Renderer {
    public final BlockPos start;
    public final Vec3i size;
    private final List<Line> edges = new ArrayList<>();

    public Cuboid(BlockPos start, Vec3i size, Color color) {
        this.start = start;
        this.size = size;
        this.edges.add(new Line(toVec3d(this.start), toVec3d(this.start.add(this.size.getX(), 0, 0)), color));
        this.edges.add(new Line(toVec3d(this.start), toVec3d(this.start.add(0, this.size.getY(), 0)), color));
        this.edges.add(new Line(toVec3d(this.start), toVec3d(this.start.add(0, 0, this.size.getZ())), color));
        this.edges.add(new Line(toVec3d(this.start.add(this.size.getX(), 0, this.size.getZ())), toVec3d(this.start.add(this.size.getX(), 0, 0)), color));
        this.edges.add(new Line(toVec3d(this.start.add(this.size.getX(), 0, this.size.getZ())), toVec3d(this.start.add(this.size.getX(), this.size.getY(), this.size.getZ())), color));
        this.edges.add(new Line(toVec3d(this.start.add(this.size.getX(), 0, this.size.getZ())), toVec3d(this.start.add(0, 0, this.size.getZ())), color));
        this.edges.add(new Line(toVec3d(this.start.add(this.size.getX(), this.size.getY(), 0)), toVec3d(this.start.add(this.size.getX(), 0, 0)), color));
        this.edges.add(new Line(toVec3d(this.start.add(this.size.getX(), this.size.getY(), 0)), toVec3d(this.start.add(0, this.size.getY(), 0)), color));
        this.edges.add(new Line(toVec3d(this.start.add(this.size.getX(), this.size.getY(), 0)), toVec3d(this.start.add(this.size.getX(), this.size.getY(), this.size.getZ())), color));
        this.edges.add(new Line(toVec3d(this.start.add(0, this.size.getY(), this.size.getZ())), toVec3d(this.start.add(0, 0, this.size.getZ())), color));
        this.edges.add(new Line(toVec3d(this.start.add(0, this.size.getY(), this.size.getZ())), toVec3d(this.start.add(0, this.size.getY(), 0)), color));
        this.edges.add(new Line(toVec3d(this.start.add(0, this.size.getY(), this.size.getZ())), toVec3d(this.start.add(this.size.getX(), this.size.getY(), this.size.getZ())), color));
    }

    @Override
    public void render() {
        if (this.start == null || this.size == null) return;
        for (Line edge : this.edges)
            edge.render();
    }

    @Override
    public BlockPos getPos() {
        return this.start.add(this.size.getX() / 2, this.size.getY() / 2, this.size.getZ() / 2);
    }
}
