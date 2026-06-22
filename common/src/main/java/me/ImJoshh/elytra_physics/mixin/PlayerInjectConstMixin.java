package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerInjectConstMixin extends LivingEntity {

    @Shadow protected double xCloak;
    @Shadow protected double yCloak;
    @Shadow protected double zCloak;

    protected PlayerInjectConstMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyConstant(method = "moveCloak", constant = @Constant(doubleValue = 0.25))
    private double injectGravityCoefficient(double value) {
        double playerGravity = ElytraPhysics.getConfig().adaptiveGravityEnabled() ? ElytraPhysics.getEntityGravity(this) : 0.08;
        double gravityMultiplier = ElytraPhysics.getConfig().getGravityMultiplier();
        return (0.75 * (gravityMultiplier * playerGravity * 12.5 - 1) * value) + value;
    }
}
