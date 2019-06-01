package salandora.newlight.mixin.client;

import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import salandora.newlight.lighting.LightingHooks;

@Mixin(ChunkProviderClient.class)
public class ChunkProviderClientMixin
{

    @Redirect(method = "func_212474_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;read(Lnet/minecraft/network/PacketBuffer;IZ)V"))
    private void ChunkProviderClient_readChunk(Chunk chunk, PacketBuffer buffer,  int availableSections, boolean fullChunk)
    {
        chunk.read(buffer, availableSections, fullChunk);
        LightingHooks.onLoadClient(chunk.getWorld(), chunk);
    }
}
