package iafenvoy.blockfinder.mixin;

import iafenvoy.blockfinder.finder.BlocksFinder;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onChunkData", at = @At(value = "TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        BlocksFinder.onChunkData(this.world, packet.getX(), packet.getZ());
    }

    @Inject(method = "onUnloadChunk", at = @At("TAIL"))
    private void onUnloadChunk(UnloadChunkS2CPacket packet, CallbackInfo ci) {
        BlocksFinder.onChunkUnload(packet.getX(), packet.getZ());
    }

    @Inject(method = "onBlockUpdate", at = @At("TAIL"))
    private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        BlocksFinder.onBlockUpdate(packet.getPos(), packet.getState().getBlock());
    }
}
