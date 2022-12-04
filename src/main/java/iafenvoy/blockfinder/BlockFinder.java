package iafenvoy.blockfinder;

import iafenvoy.blockfinder.render.CubeRenderer;
import iafenvoy.blockfinder.render.RenderQueue;
import net.fabricmc.api.ClientModInitializer;

public class BlockFinder implements ClientModInitializer {
    public static final String MOD_ID = "blockfinder";

    @Override
    public void onInitializeClient() {
        RenderQueue.get().add("hand", CubeRenderer::renderFinders);
        Commands.register();
    }
}