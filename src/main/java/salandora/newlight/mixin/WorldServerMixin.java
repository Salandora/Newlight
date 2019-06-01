package salandora.newlight.mixin;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import salandora.newlight.util.IWorldNewLight;

import javax.annotation.Nullable;

@Mixin(WorldServer.class)
public abstract class WorldServerMixin extends World
{
    protected WorldServerMixin(ISaveHandler p_i49813_1_, @Nullable WorldSavedDataStorage p_i49813_2_, WorldInfo p_i49813_3_, Dimension p_i49813_4_, Profiler p_i49813_5_, boolean p_i49813_6_)
    {
        super(p_i49813_1_, p_i49813_2_, p_i49813_3_, p_i49813_4_, p_i49813_5_, p_i49813_6_);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Teleporter;tick(J)V", shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci)
    {
        this.profiler.endStartSection("lighting");
        ((IWorldNewLight)this).getLightingEngine().procLightUpdates();
    }
}
