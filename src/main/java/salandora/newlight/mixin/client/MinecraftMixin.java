package salandora.newlight.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import salandora.newlight.util.IWorldNewLight;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Shadow @Final public Profiler profiler;

    @Shadow public WorldClient world;

    @Inject(method = "runTick", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=levelRenderer", shift = At.Shift.BEFORE))
    private void preEndStartSectionLEvelRenderer(CallbackInfo ci)
    {
        this.profiler.endStartSection("lighting");
        ((IWorldNewLight)this.world).getLightingEngine().procLightUpdates();
    }
}
