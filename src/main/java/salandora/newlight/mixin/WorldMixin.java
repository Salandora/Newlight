package salandora.newlight.mixin;

import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import salandora.newlight.lighting.LightingEngine;
import salandora.newlight.util.IWorldNewLight;

import javax.annotation.Nullable;

@Mixin(World.class)
public class WorldMixin implements IWorldNewLight
{
    private LightingEngine lightingEngine;

    @Override
    public LightingEngine getLightingEngine()
    {
        return lightingEngine;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ISaveHandler saveHandlerIn, @Nullable WorldSavedDataStorage worldSavedDataStorage, WorldInfo info, Dimension dimensionIn, Profiler profilerIn, boolean client, CallbackInfo ci)
    {
        this.lightingEngine = new LightingEngine((World) (Object) this);
    }

    @Inject(method = "checkLightFor", at = @At("HEAD"), cancellable = true)
    private void onCheckLightFor(EnumLightType lighType, BlockPos pos, CallbackInfoReturnable<Boolean> cir)
    {
        this.getLightingEngine().scheduleLightUpdate(lighType, pos);
        cir.setReturnValue(true);
        cir.cancel();
    }
}
