package iafenvoy.blockfinder.render;

import com.mojang.blaze3d.systems.RenderSystem;
import iafenvoy.blockfinder.finder.BlocksFinder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class CubeRenderer {
    public static final List<Cube> renderers = new ArrayList<>();
    public static final MinecraftClient client = MinecraftClient.getInstance();

    public static synchronized void renderFinders(MatrixStack matrices) {
        renderers.clear();
        assert client.player != null;
        BlocksFinder.addTo(renderers, client.player.chunkX, client.player.chunkZ);
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrices.peek().getModel());
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        for (Cube renderer : renderers)
            renderer.render();
        RenderSystem.popMatrix();
    }
}
