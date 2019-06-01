package salandora.newlight.mixin.client;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import salandora.newlight.lighting.LightTrackingHooks;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin
{
    private Chunk copyChunk;

    @Inject(method = "handleChunkData", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;markBlockRangeForRenderUpdate(IIIIII)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleChunkData_copyVariables(SPacketChunkData packet, CallbackInfo ci, int i, int j, Chunk chunk)
    {
        this.copyChunk = chunk;
    }

    @Inject(method = "handleChunkData", at = @At("RETURN"))
    private void handleChunkData_injectLightTrackingHooks(SPacketChunkData packetIn, CallbackInfo ci)
    {
        LightTrackingHooks.onChunkReceive(this.copyChunk, packetIn.getExtractedSize());
    }
}
