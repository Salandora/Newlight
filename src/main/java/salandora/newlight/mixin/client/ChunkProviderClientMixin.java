package salandora.newlight.mixin.client;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import salandora.newlight.lighting.LightingHooks;

@Mixin(ChunkProviderClient.class)
public class ChunkProviderClientMixin
{
    @Shadow @Final private World world;

    @Inject(method = "func_212474_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;read(Lnet/minecraft/network/PacketBuffer;IZ)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void preReadChunk(int x, int z, PacketBuffer buf, int p_212474_4_, boolean p_212474_5_, CallbackInfoReturnable<Chunk> ci, Long2ObjectMap var6, long i, Chunk chunk)
    {
        LightingHooks.onLoadClient(this.world, chunk);
    }
}
