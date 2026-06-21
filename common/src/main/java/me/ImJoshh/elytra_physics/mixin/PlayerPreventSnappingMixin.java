package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
abstract public class PlayerPreventSnappingMixin extends Entity {

    @Shadow protected double xCloak;
    @Shadow protected double yCloak;
    @Shadow protected double zCloak;
    @Shadow protected double xCloakO;
    @Shadow protected double yCloakO;
    @Shadow protected double zCloakO;

    public PlayerPreventSnappingMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author ImJoshh
     * @reason Re-written moveCloak method to prevent snapping issues at high speeds
     */
    @Overwrite
    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;

        Vec3 cloakDistanceVec = new Vec3(this.getX() - this.xCloak, this.getY() - this.yCloak, this.getZ() - this.zCloak);

        double length = cloakDistanceVec.length();

        if (length > 10.0) {
            // clamp distance
            Vec3 distanceToCloak = new Vec3(this.xCloak - this.getX(), this.yCloak - this.getY(), this.zCloak - this.getZ());
            distanceToCloak = distanceToCloak.normalize().scale(9.5);

            this.xCloak = this.getX() + distanceToCloak.x;
            this.xCloakO = this.xCloak;

            this.yCloak = this.getY() + distanceToCloak.y;
            this.yCloakO = this.yCloak;

            this.zCloak = this.getZ() + distanceToCloak.z;
            this.zCloakO = this.zCloak;
        }
        else {
            double base = 0.25;

            double gravityMultiplier = ElytraPhysics.getConfig().getGravityMultiplier();

            double gravityCoefficient = (0.75 * (gravityMultiplier - 1) * base) + base;

            this.xCloak += cloakDistanceVec.x * gravityCoefficient;
            this.yCloak += cloakDistanceVec.y * gravityCoefficient;
            this.zCloak += cloakDistanceVec.z * gravityCoefficient;
        }
    }
}
