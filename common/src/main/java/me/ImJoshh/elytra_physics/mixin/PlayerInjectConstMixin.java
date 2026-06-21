package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public class PlayerInjectConstMixin {

    @Shadow protected double xCloak;
    @Shadow protected double yCloak;
    @Shadow protected double zCloak;

    @ModifyConstant(method = "moveCloak", constant = @Constant(doubleValue = 0.25))
    private double injectGravityCoefficient(double value) {
        double gravityMultiplier = ElytraPhysics.getConfig().getGravityMultiplier();
        return (0.75 * (gravityMultiplier - 1) * value) + value;
    }
}
