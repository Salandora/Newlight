package salandora.newlight.mixin;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimerTickList;
import net.minecraft.world.chunk.UpgradeData;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AnvilChunkLoader.class)
public class AnvilChunkLoaderMixin
{
    @Inject(method = "writeChunkToNBT",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;put(Ljava/lang/String;Lnet/minecraft/nbt/INBTBase;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER))
    private void writeLightData(Chunk chunkIn, World worldIn, NBTTagCompound compound, CallbackInfo ci) {
        LightingHooks.writeLightData(chunkIn, compound);
    }

    @Inject(method = "readChunkFromNBT",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;setSections([Lnet/minecraft/world/chunk/ChunkSection;)V",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void readLightData(IWorld worldIn, NBTTagCompound compound, CallbackInfoReturnable<Chunk> cir,
                               int i, int j, Biome abiome[], BlockPos.MutableBlockPos blockpos$mutableblockpos, UpgradeData upgradedata, ChunkPrimerTickList chunkprimerticklist1, ChunkPrimerTickList chunkprimerticklist, long l, Chunk chunk, NBTTagList nbttaglist) {
        LightingHooks.readLightData(chunk, compound);
    }
}
