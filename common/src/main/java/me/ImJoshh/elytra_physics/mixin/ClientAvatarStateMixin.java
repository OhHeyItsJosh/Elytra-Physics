package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.HasLivingEntityRef;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientAvatarState.class)
public class ClientAvatarStateMixin implements HasLivingEntityRef {

    @Unique
    private LivingEntity elytraPhysics$entityRef;

    @Shadow private double xCloak;
    @Shadow private double yCloak;
    @Shadow private double zCloak;
    @Shadow private double xCloakO;
    @Shadow private double yCloakO;
    @Shadow private double zCloakO;

    @Override
    public void elytraPhysics$setEntityRef(LivingEntity entity) {
        this.elytraPhysics$entityRef = entity;

        this.xCloak = entity.getX();
        this.yCloak = entity.getY();
        this.zCloak = entity.getZ();
    }

    @Unique
    private double elytraPhysics$getEntityGravity() {
        final double DEFAULT = 0.08;

        if (this.elytraPhysics$entityRef == null)
            return DEFAULT;

        AttributeInstance gravityAttribute = this.elytraPhysics$entityRef.getAttribute(Attributes.GRAVITY);
        if (gravityAttribute == null)
            return DEFAULT;

        return gravityAttribute.getValue();
    }

//    @ModifyConstant(method = "moveCloak", constant = @Constant(doubleValue = 0.25))
//    private double injectGravityCoefficient(double value) {
//        double playerGravity = this.elytraPhysics$getEntityGravity();
//        double gravityMultiplier = 1;
//
//        return ((gravityMultiplier * playerGravity * 12.5 - 1) * value) + value;
//    }

    /**
     * @author ImJoshh
     * @reason Re-written moveCloak method to prevent snapping issues at high speeds
     */
    @Overwrite
    private void moveCloak(Vec3 vec3) {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;

        Vec3 cloakDistanceVec = new Vec3(vec3.x() - this.xCloak, vec3.y() - this.yCloak, vec3.z() - this.zCloak);

        double length = cloakDistanceVec.length();

        if (length > 10.0) {
            // clamp distance
            Vec3 distanceToCloak = new Vec3(this.xCloak - vec3.x(), this.yCloak - vec3.y(), this.zCloak - vec3.z());
            distanceToCloak = distanceToCloak.normalize().scale(9.5);

            this.xCloak = vec3.x + distanceToCloak.x;
            this.xCloakO = this.xCloak;

            this.yCloak = vec3.y + distanceToCloak.y;
            this.yCloakO = this.yCloak;

            this.zCloak = vec3.z + distanceToCloak.z;
            this.zCloakO = this.zCloak;
        }
        else {
            double base = 0.25;

            double playerGravity = this.elytraPhysics$getEntityGravity();
            double gravityMultiplier = 1;

            double gravityCoefficient = (0.75 * (gravityMultiplier * playerGravity * 12.5 - 1) * base) + base;

            this.xCloak += cloakDistanceVec.x * gravityCoefficient;
            this.yCloak += cloakDistanceVec.y * gravityCoefficient;
            this.zCloak += cloakDistanceVec.z * gravityCoefficient;
        }
    }
}
