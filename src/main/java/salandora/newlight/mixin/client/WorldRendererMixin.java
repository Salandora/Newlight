package salandora.newlight.mixin.client;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
    @Shadow protected abstract void markBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean updateImmediately);

    @Inject(method = "notifyLightSet", at = @At("HEAD"), cancellable = true)
    private void onNotifyLightSet(BlockPos pos, CallbackInfo ci)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        this.markBlocksForUpdate(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1, false);
        ci.cancel();
    }
}

