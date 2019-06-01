package salandora.newlight;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.dimdev.rift.listener.MessageAdder;
import org.dimdev.rift.network.Message;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import salandora.newlight.lighting.network.SPacketLightTracking;

public class NewLight implements InitializationListener, MessageAdder
{
    @Override
    public void onInitialization()
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.newlight.json");
    }

    @Override
    public void registerMessages(IRegistry<Class<? extends Message>> registry)
    {
        registry.put(new ResourceLocation("newlight"), SPacketLightTracking.class);
    }
}
