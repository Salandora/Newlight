package salandora.newlight.lighting.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import org.dimdev.rift.network.ClientMessageContext;
import org.dimdev.rift.network.Message;
import salandora.newlight.lighting.LightTrackingHooks;

public class SPacketLightTracking extends Message
{
    public int chunkX;
    public int chunkZ;
    public int[] data;

    public SPacketLightTracking()
    {
    }

    public SPacketLightTracking(final int chunkX, final int chunkZ, final int[] data)
    {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.data = data;
    }

    public SPacketLightTracking(final ChunkPos pos, final int[] data)
    {
        this(pos.x, pos.z, data);
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeInt(this.chunkX).writeInt(this.chunkZ);

        for (final int i : this.data)
            buffer.writeInt(i);
    }

    @Override
    public void read(PacketBuffer buffer)
    {
        this.chunkX = buffer.readInt();
        this.chunkZ = buffer.readInt();

        this.data = new int[LightTrackingHooks.SYNC_FLAG_COUNT];

        for (int i = 0; i < this.data.length; ++i)
            this.data[i] = buffer.readInt();
    }

    @Override
    public void process(ClientMessageContext context)
    {
        context.getClient().addScheduledTask(() -> LightTrackingHooks.applySyncLightTrackins(data, chunkX, chunkZ, context.getClient().world));
    }
}
