package salandora.newlight.mixin;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import salandora.newlight.lighting.LightTrackingHooks;
import salandora.newlight.util.IPlayerChunkMapEntry;

import java.util.Iterator;
import java.util.Map;

@Mixin(PlayerChunkMapEntry.class)
public class PlayerChunkMapEntryMixin implements IPlayerChunkMapEntry
{
    @Shadow @Final private PlayerChunkMap playerChunkMap;

    @Shadow private int changedSectionFilter;
    private java.util.Map<EntityPlayerMP, int[]> lightTrackings = new java.util.HashMap<>();
    private final java.util.Map<EntityPlayerMP, int[]> neighborLightTrackings = new java.util.HashMap<>();

    @Override
    public Map<EntityPlayerMP, int[]> getLightTrackings()
    {
        return lightTrackings;
    }

    @Override
    public Map<EntityPlayerMP, int[]> getNeighborLightTrackings()
    {
        return neighborLightTrackings;
    }

    @Inject(method = "addPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;sendToPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V", shift = At.Shift.BEFORE))
    private void addPlayer_addToLightTracking(EntityPlayerMP player, CallbackInfo ci)
    {
        LightTrackingHooks.addPlayer(player, (PlayerChunkMapEntry) (Object)this, this.playerChunkMap);
    }

    @Inject(method = "removePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.BEFORE))
    private void removePlayer_removeFromLightTracking(EntityPlayerMP player, CallbackInfo ci)
    {
        LightTrackingHooks.removePlayer(player, (PlayerChunkMapEntry) (Object)this, this.playerChunkMap);
    }

    @Inject(method = "sendToPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void sendToPlayers_addToLightTracking(CallbackInfoReturnable<Boolean> cir, Packet packet, Iterator var2, EntityPlayerMP player)
    {
        LightTrackingHooks.addPlayer(player, (PlayerChunkMapEntry) (Object)this, this.playerChunkMap);
    }

    @Inject(method = "sendToPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityTracker;sendLeashedEntitiesInChunk(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/world/chunk/Chunk;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void sendToPlayers_sendLightTrackings(CallbackInfoReturnable<Boolean> cir, Packet packet, Iterator var2, EntityPlayerMP player)
    {
        LightTrackingHooks.sendLightTrackings((PlayerChunkMapEntry) (Object)this, player, this.playerChunkMap);
    }

    @Inject(method = "sendToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityTracker;sendLeashedEntitiesInChunk(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/world/chunk/Chunk;)V", shift = At.Shift.AFTER))
    private void sendToPlayer_sendLightTrackings(EntityPlayerMP player, CallbackInfo ci)
    {
        LightTrackingHooks.sendLightTrackings((PlayerChunkMapEntry) (Object)this, player, this.playerChunkMap);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void tick_sendLightTrackings(CallbackInfo ci)
    {
        LightTrackingHooks.sendLightTrackings((PlayerChunkMapEntry) (Object)this, this.changedSectionFilter, this.playerChunkMap);
    }
}
