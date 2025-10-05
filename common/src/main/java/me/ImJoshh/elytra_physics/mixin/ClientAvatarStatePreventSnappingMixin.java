package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import me.ImJoshh.elytra_physics.HasLivingEntityRef;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientAvatarState.class)
public class ClientAvatarStatePreventSnappingMixin implements HasLivingEntityRef {

    @Unique
    private LivingEntity elytraPhysics$entityRef;

    @Shadow
    protected double xCloak;
    @Shadow protected double yCloak;
    @Shadow protected double zCloak;
    @Shadow protected double xCloakO;
    @Shadow protected double yCloakO;
    @Shadow protected double zCloakO;

    @Override
    public void elytraPhysics$setEntityRef(LivingEntity entity) {
        this.elytraPhysics$entityRef = entity;

        this.xCloak = entity.getX();
        this.yCloak = entity.getY();
        this.zCloak = entity.getZ();
    }

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

            double playerGravity = ElytraPhysics.getConfig().adaptiveGravityEnabled() ? ElytraPhysics.getEntityGravity(this.elytraPhysics$entityRef) : 0.08;
            double gravityMultiplier = ElytraPhysics.getConfig().getGravityMultiplier();

            double gravityCoefficient = (0.75 * (gravityMultiplier * playerGravity * 12.5 - 1) * base) + base;

            this.xCloak += cloakDistanceVec.x * gravityCoefficient;
            this.yCloak += cloakDistanceVec.y * gravityCoefficient;
            this.zCloak += cloakDistanceVec.z * gravityCoefficient;
        }
    }
}
