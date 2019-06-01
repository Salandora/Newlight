package salandora.newlight.mixin;

import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import salandora.newlight.util.IWorldNewLight;

@Mixin(SPacketChunkData.class)
public class SPacketChunkDataMixin
{
    @Inject(method = "calculateChunkSize", at = @At("HEAD"))
    private void constructorMixin(Chunk chunkIn, boolean flag, int changedSectionFilter, CallbackInfoReturnable<Integer> ci)
    {
        ((IWorldNewLight)chunkIn.getWorld()).getLightingEngine().procLightUpdates();
    }
}
