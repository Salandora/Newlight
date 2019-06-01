package salandora.newlight.mixin;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import salandora.newlight.util.IWorldNewLight;


@Mixin(ChunkProviderServer.class)
public class ChunkProviderServerMixin
{
    @Shadow @Final private WorldServer world;

    @Inject(method = "saveChunks", at = @At(value = "HEAD"))
    private void onSaveChunks(boolean all, CallbackInfoReturnable<Boolean> cir)
    {
        ((IWorldNewLight)this.world).getLightingEngine().procLightUpdates();
    }

    /*@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/LongSet;isEmpty()Z"))
    private boolean onTick(LongSet set)
    {
        final boolean isEmpty = set.isEmpty();
        if (!isEmpty)
        {
            ((IWorldNewLight)this.world).getLightingEngine().procLightUpdates();
        }
        return isEmpty;
    }*/
}
