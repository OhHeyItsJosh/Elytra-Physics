package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import me.ImJoshh.elytra_physics.HasLivingEntityRef;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientAvatarState.class)
public class ClientAvatarStateInjectConstMixin implements HasLivingEntityRef {

    @Unique
    private LivingEntity elytraPhysics$entityRef;

    @Shadow protected double xCloak;
    @Shadow protected double yCloak;
    @Shadow protected double zCloak;

    @Override
    public void elytraPhysics$setEntityRef(LivingEntity entity) {
        this.elytraPhysics$entityRef = entity;

        this.xCloak = entity.getX();
        this.yCloak = entity.getY();
        this.zCloak = entity.getZ();
    }

    @ModifyConstant(method = "moveCloak", constant = @Constant(doubleValue = 0.25))
    private double injectGravityCoefficient(double value) {
        double playerGravity = ElytraPhysics.getEntityGravity(this.elytraPhysics$entityRef);
        double gravityMultiplier = ElytraPhysics.getConfig().getGravityMultiplier();

        return (0.75 * (gravityMultiplier * playerGravity * 12.5 - 1) * value) + value;
    }
}
