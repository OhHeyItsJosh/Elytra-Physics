package me.ImJoshh.elytra_physics;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.joml.Quaternionf;

public class ElytraPhysics {
    public static final String MOD_ID = "elytra_physics";

    private static ElytraPhysicsConfig s_config;

    public static void setConfig(ElytraPhysicsConfig newConfig) {
        s_config = newConfig;
    }

    public static ElytraPhysicsConfig getConfig() {
        return s_config;
    }

    public static void applyMovementTransformation(PoseStack matrixStack, LivingEntity livingEntity, float partialTicks)
    {
        if (livingEntity instanceof AbstractClientPlayer abstractClientPlayerEntity) {
            // calculate cape flap and lean
            double distX = Mth.lerp(partialTicks, abstractClientPlayerEntity.xCloakO, abstractClientPlayerEntity.xCloak) - Mth.lerp(partialTicks, abstractClientPlayerEntity.xo, abstractClientPlayerEntity.getX());
            double distY = Mth.lerp(partialTicks, abstractClientPlayerEntity.yCloakO, abstractClientPlayerEntity.yCloak )- Mth.lerp(partialTicks, abstractClientPlayerEntity.yo, abstractClientPlayerEntity.getY());
            double distZ = Mth.lerp(partialTicks, abstractClientPlayerEntity.zCloakO, abstractClientPlayerEntity.zCloak) - Mth.lerp(partialTicks, abstractClientPlayerEntity.zo, abstractClientPlayerEntity.getZ());
            float yBodyRot = Mth.rotLerp(partialTicks, abstractClientPlayerEntity.yBodyRotO, abstractClientPlayerEntity.yBodyRot);
            double forwardX = Mth.sin(yBodyRot * 0.017453292F);
            double forwardZ = (-Mth.cos(yBodyRot * 0.017453292F));

            float capeFlap = (float)distY * 10.0F;
            capeFlap = Mth.clamp(capeFlap, -6.0F, 32.0F);
            float capeLean = (float)(distX * forwardX + distZ * forwardZ) * 100.0F;
            capeLean = Mth.clamp(capeLean, 0.0F, 150.0F);
            float capeLean2 = (float)(distX * forwardZ - distZ * forwardX) * 100.0F;
            capeLean2 = Mth.clamp(capeLean2, -20.0F, 20.0F);
            if (capeLean < 0.0F) {
                capeLean = 0.0F;
            }

            float pow = Mth.lerp(partialTicks, abstractClientPlayerEntity.oBob, abstractClientPlayerEntity.bob);
            capeFlap += Mth.sin(Mth.lerp(partialTicks, abstractClientPlayerEntity.walkDistO, abstractClientPlayerEntity.walkDist) * 6.0F) * 32.0F * pow;

            // apply decline
            float declineFactor = 0.2f * getConfig().getDeclineMultiplier();
            matrixStack.translate(0, declineFactor * (capeLean / 150), 0);

            // create transform
            float leanMultiplier = getConfig().getLeanMultiplier();
            float leanMultiplierAdjusted = !abstractClientPlayerEntity.isVisuallySwimming()
                    ? leanMultiplier * 0.85f
                    : leanMultiplier * 0.25f;

            Quaternionf transformation = (new Quaternionf())
                    .rotateX(((leanMultiplierAdjusted * capeLean) / 2.0F + capeFlap) * 0.017453292F) // TILT
                    .rotateZ(((capeLean2 * getConfig().getPanMultiplier()) / 2.0F) * 0.017453292F) // PAN
                    .rotateY(((-capeLean2 * getConfig().getRollMultiplier()) / 2.0F) * 0.017453292F); // ROLL

            // fade transformation out while flying
            if (livingEntity.isFallFlying()) {
                float fallFlyingTicks = livingEntity.getFallFlyingTicks() + partialTicks;
                float fallFlyingScale = Mth.clamp(fallFlyingTicks * fallFlyingTicks / 100.0F, 0.0F, 1.0F);
                transformation = transformation.slerp(new Quaternionf(), fallFlyingScale);
            }

            matrixStack.mulPose(transformation);
        }
    }

    public static float setWingSpread(float originalValue, LivingEntity entity)
    {
        if (entity instanceof AbstractClientPlayer abstractClientPlayer) {
            if (entity.isFallFlying())
                return originalValue;

            double distX = abstractClientPlayer.xCloak - abstractClientPlayer.getX();
            double distY = abstractClientPlayer.yCloak - abstractClientPlayer.getY();
            double distZ = abstractClientPlayer.zCloak - abstractClientPlayer.getZ();

            double forwardX = Mth.sin(abstractClientPlayer.yBodyRot * 0.017453292F);
            double forwardZ = (-Mth.cos(abstractClientPlayer.yBodyRot * 0.017453292F));

            float capeFlap = (float)distY * 10.0F;
            capeFlap = Mth.clamp(capeFlap, -6.0F, 32.0F);
            float capeLean = (float)(distX * forwardX + distZ * forwardZ) * 100.0F;
            capeLean = Mth.clamp(capeLean, 0.0F, 150.0F);

            capeFlap += Mth.sin(abstractClientPlayer.walkDist * 6.0F) * 32.0F * abstractClientPlayer.bob;

            return originalValue - (getConfig().getSpreadMultiplier() * 0.75f * capeLean / 2.0F + capeFlap) * 0.004363323F;

        }
        else {
            return originalValue;
        }
    }

    public static boolean shouldInjectLayer(RenderLayer<?, ?> layer) {
        // check whether the render is an elytra renderer
        for (Class<RenderLayer<?, ?>> clazz : getConfig().getElytraLayers())
        {
            if (clazz.equals(layer.getClass()))
            {
                return true;
            }
        }

        return false;
    }

    public static double getEntityGravity(LivingEntity entity) {
        final double DEFAULT = 0.08;

        if (entity == null)
            return DEFAULT;

        AttributeInstance gravityAttribute = entity.getAttribute(Attributes.GRAVITY);
        if (gravityAttribute == null)
            return DEFAULT;

        return gravityAttribute.getValue();
    }
}
