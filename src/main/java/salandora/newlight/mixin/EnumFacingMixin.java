package salandora.newlight.mixin;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import salandora.newlight.util.IEnumFacing;

@Mixin(EnumFacing.class)
public abstract class EnumFacingMixin implements IEnumFacing
{
    @Accessor("directionVec")
    public abstract Vec3i getDirectionVec();
}
