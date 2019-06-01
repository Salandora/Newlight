package salandora.newlight.util;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map;

public interface IPlayerChunkMapEntry
{
    Map<EntityPlayerMP, int[]> getLightTrackings();
    Map<EntityPlayerMP, int[]> getNeighborLightTrackings();
}
