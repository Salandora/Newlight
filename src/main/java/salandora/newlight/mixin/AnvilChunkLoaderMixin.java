package salandora.newlight.mixin;

import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.injection.Redirect;
import salandora.newlight.lighting.LightingHooks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilChunkLoader.class)
public class AnvilChunkLoaderMixin
{
    private NBTTagCompound readChunkFromNBT_compound;

    @Inject(method = "writeChunkToNBT",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;put(Ljava/lang/String;Lnet/minecraft/nbt/INBTBase;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER))
    private void writeLightData(Chunk chunkIn, World worldIn, NBTTagCompound compound, CallbackInfo ci) {
        LightingHooks.writeLightData(chunkIn, compound);
    }

    @Inject(method = "readChunkFromNBT", at = @At("HEAD"))
    private void readChunkFromNBT_storeCompound(IWorld worldIn, NBTTagCompound compound, CallbackInfoReturnable<Chunk> cir)
    {
        this.readChunkFromNBT_compound = compound;
    }

    @Redirect(method = "readChunkFromNBT",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;setSections([Lnet/minecraft/world/chunk/ChunkSection;)V"))
    private void readLightData(Chunk chunk, ChunkSection[] sections) {
        chunk.setSections(sections);

        LightingHooks.readLightData(chunk, this.readChunkFromNBT_compound);
    }
}
