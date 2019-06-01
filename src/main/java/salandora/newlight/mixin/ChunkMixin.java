package salandora.newlight.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import salandora.newlight.lighting.LightInitHooks;
import salandora.newlight.lighting.LightingHooks;
import salandora.newlight.util.IChunkNewLight;
import salandora.newlight.util.IWorldNewLight;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements IChunkNewLight
{
    @Shadow @Final private World world;


    @Shadow protected abstract void generateHeightMap();

    @Shadow public abstract Heightmap getHeightmap(Heightmap.Type type);

    private int neighborLoaded;

    private int[] neighborLightChecks = null;
    private int[] lightTrackings = null;
    private int[] neighborLightTrackings = null;

    private boolean pendingBoundaryChecks;
    private short pendingNeighborLightInits;

    private boolean isLightPopulated;

    private int generateSkyLightMap_x;
    private int generateSkyLightMap_z;

    private int relightBlock_x;
    private int relightBlock_z;
    private int relightBlock_i;
    private int relightBlock_j;

    private boolean read_fullChunk;

    public int[] getNeighborLightChecks() {
        return this.neighborLightChecks;
    }
    public void setNeighborLightChecks(int[] in) {
        this.neighborLightChecks = in;
    }

    public int[] getLightTrackings() {
        return this.lightTrackings;
    }
    public void setLightTrackings(int[] in) {
        this.lightTrackings = in;
    }

    public int[] getNeighborLightTrackings() {
        return this.neighborLightTrackings;
    }
    public void setNeighborLightTrackings(int[] in) {
        this.neighborLightTrackings = in;
    }

    public int getNeighborsLoaded() {
        return this.neighborLoaded;
    }
    public void setNeighborsLoaded(int in) {
        this.neighborLoaded = in;
    }

    public boolean getPendingBoundaryChecks() { return this.pendingBoundaryChecks; }
    public void setPendingBoundaryChecks(boolean value) { this.pendingBoundaryChecks = value; }

    public short getPendingNeighborLightInits() {
        return this.pendingNeighborLightInits;
    }
    public void setPendingNeighborLightInits(short in) {
        this.pendingNeighborLightInits = in;
    }

    public boolean getIsLightPopulated()
    {
        return this.isLightPopulated;
    }
    public void setIsLightPopulated(boolean value)
    {
        this.isLightPopulated = value;
    }

    @Inject(method = "generateSkylightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;hasSkyLight()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void generateSkyLightMap_copyVariables(CallbackInfo ci, int i, int i1, int j1)
    {
        this.generateSkyLightMap_x = i1;
        this.generateSkyLightMap_z = j1;
    }

    @Redirect(method = "generateSkylightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;hasSkyLight()Z"))
    private boolean fillSkylightColumnRedirect(Dimension dimension)
    {
        if (dimension.hasSkyLight())
        {
            LightInitHooks.fillSkylightColumn((Chunk) (Object) this, generateSkyLightMap_x, generateSkyLightMap_z);
        }

        return false;
    }

    @Redirect(method = "relightBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;markBlocksDirtyVertical(IIII)V"))
    private void abortMarkBlocksDirtyVertical(World worldIn, int x, int z, int y1, int y2)
    {
    }

    @Inject(method = "relightBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;hasSkyLight()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void relightBlock_copyVariables(int x, int y, int z, IBlockState p_76615_4_, CallbackInfo ci, Heightmap heightmap, int i, int j, int k, int l, World worldIn, int var17, int var18, int var19, int var20)
    {
        this.relightBlock_x = x;
        this.relightBlock_z = z;
        this.relightBlock_i = i;
        this.relightBlock_j = j;
    }

    @Redirect(method = "relightBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;hasSkyLight()Z", ordinal = 0))
    private boolean relightBlockSkylightColumnRedirect(Dimension dimension)
    {
        if (dimension.hasSkyLight())
        {
            LightingHooks.relightSkylightColumn(this.world, (Chunk) (Object) this, this.relightBlock_x, this.relightBlock_z, this.relightBlock_i, this.relightBlock_j);
        }

        return false;
    }

    @Redirect(method = "relightBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;hasSkyLight()Z", ordinal = 1))
    private boolean abortUpdateSkylightNeighborHeight(Dimension dimension)
    {
        return false;
    }

    @Redirect(method = "setBlockState", at = @At(value = "NEW", target = "net/minecraft/world/chunk/ChunkSection"))
    private ChunkSection initSkylight(final int y, final boolean storeSkylight)
    {
        final ChunkSection section = new ChunkSection(y, storeSkylight);
        LightingHooks.initSkylightForSection(this.world, (Chunk) (Object) this, section);
        return section;
    }

    @ModifyVariable(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;set(IIILnet/minecraft/block/state/IBlockState;)V", ordinal = 0), ordinal = 1)
    private boolean setFlagToFalse(boolean flag)
    {
        return false;
    }

    @Redirect(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;propagateSkylightOcclusion(II)V"))
    private void cancelPropagateSkylightOcclusion(Chunk chunk, int x, int z)
    {
    }


    @Redirect(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getLightFor(Lnet/minecraft/world/EnumLightType;Lnet/minecraft/util/math/BlockPos;)I"))
    private int cancelGetLightFor(Chunk chunk, EnumLightType lightType, BlockPos pos)
    {
        return 0;
    }

    @Inject(method = "getLightFor", at = @At("HEAD"))
    private void onGetLightFor(EnumLightType lightType, BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        ((IWorldNewLight)this.world).getLightingEngine().procLightUpdates(lightType);
    }

    @Redirect(method = "setLightFor(Lnet/minecraft/world/EnumLightType;ZLnet/minecraft/util/math/BlockPos;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;generateSkylightMap()V"))
    private void cancelGenerateSkylightMap(Chunk chunk)
    {
    }

    @Inject(method = "setLightFor(Lnet/minecraft/world/EnumLightType;ZLnet/minecraft/util/math/BlockPos;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;generateSkylightMap()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void redirectGenerateSkylightMap(EnumLightType type, boolean hasSkylight, BlockPos pos, int lightValue, CallbackInfo ci, int i, int j, int k, int l, ChunkSection chunksection)
    {
        LightingHooks.initSkylightForSection(this.world, (Chunk) (Object) this, chunksection);
    }

    @Inject(method = "getLightSubtracted(Lnet/minecraft/util/math/BlockPos;IZ)I", at = @At("HEAD"))
    private void onGetLightSubtracted(BlockPos pos, int amount, boolean hasSkyLight, CallbackInfoReturnable<Integer> cir) {
        ((IWorldNewLight) this.world).getLightingEngine().procLightUpdates();
    }

    @Inject(method = "onLoad", at = @At("RETURN"))
    private void postOnLoad(CallbackInfo ci) {
        LightingHooks.onLoadServer(this.world, (Chunk) (Object) this);
    }

    @Inject(method = "onUnload", at = @At("HEAD"))
    private void preOnUnload(CallbackInfo ci)
    {
        ((IWorldNewLight)this.world).getLightingEngine().procLightUpdates();
    }
    @Inject(method = "onUnload", at = @At("RETURN"))
    private void postOnUnload(CallbackInfo ci)
    {
        LightingHooks.onUnload(this.world, (Chunk) (Object) this);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick_onTickInjection(CallbackInfo ci)
    {
        LightingHooks.onTick(this.world, (Chunk) (Object)this);
    }

    @Inject(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;generateHeightMap()V", shift = At.Shift.BEFORE))
    private void read_copyVariable(PacketBuffer buf, int availableSections, boolean fullChunk, CallbackInfo ci)
    {
        this.read_fullChunk = fullChunk;
    }

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;generateHeightMap()V"))
    private void read_redirectGenerateHeightMap(Chunk chunk)
    {
        this.isLightPopulated = true;

        long[] oldHeightMap = null;
        if (!this.read_fullChunk)
        {
            Heightmap map = this.getHeightmap(Heightmap.Type.LIGHT_BLOCKING);
            oldHeightMap = new long[map.getDataArray().length];
            System.arraycopy(map.getDataArray(), 0, oldHeightMap, 0, oldHeightMap.length);
        }

        this.generateHeightMap();
        LightingHooks.relightSkylightColumns(this.world, (Chunk) (Object)this, oldHeightMap);
    }
}