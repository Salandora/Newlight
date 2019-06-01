package salandora.newlight.mixin;

import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSection.class)
public class ChunkSectionMixin
{
    @Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
    private void onIsEmpty(CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(false);
    }
}
