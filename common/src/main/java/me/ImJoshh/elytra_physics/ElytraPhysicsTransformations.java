package me.ImJoshh.elytra_physics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ElytraPhysicsTransformations {
    public static void applyMovementTransformation(PoseStack matrixStack, LivingEntity livingEntity, float partialTicks)
    {
        if (livingEntity instanceof AbstractClientPlayer abstractClientPlayerEntity) {
            if (!livingEntity.isCrouching() && !livingEntity.isFallFlying()) {
                //basically the code from the cape renderer with a few tweaks
                double d = Mth.lerp((double)partialTicks, abstractClientPlayerEntity.xCloakO, abstractClientPlayerEntity.xCloak) - Mth.lerp((double)partialTicks, abstractClientPlayerEntity.xo, abstractClientPlayerEntity.getX());
                double e = Mth.lerp((double)partialTicks, abstractClientPlayerEntity.yCloakO, abstractClientPlayerEntity.yCloak )- Mth.lerp((double)partialTicks, abstractClientPlayerEntity.yo, abstractClientPlayerEntity.getY());
                double m = Mth.lerp((double)partialTicks, abstractClientPlayerEntity.zCloakO, abstractClientPlayerEntity.zCloak) - Mth.lerp((double)partialTicks, abstractClientPlayerEntity.zo, abstractClientPlayerEntity.getZ());
                float n = Mth.rotLerp(partialTicks, abstractClientPlayerEntity.yBodyRotO, abstractClientPlayerEntity.yBodyRot);
                double o = (double)Mth.sin(n * 0.017453292F);
                double p = (double)(-Mth.cos(n * 0.017453292F));
                float q = (float)e * 10.0F;
                q = Mth.clamp(q, -6.0F, 32.0F);
                float r = (float)(d * o + m * p) * 100.0F;
                r = Mth.clamp(r, 0.0F, 150.0F);
                float s = (float)(d * p - m * o) * 100.0F;
                s = Mth.clamp(s, -20.0F, 20.0F);
                if (r < 0.0F) {
                    r = 0.0F;
                }

                float t = Mth.lerp(partialTicks, abstractClientPlayerEntity.oBob, abstractClientPlayerEntity.bob);
                q += Mth.sin(Mth.lerp(partialTicks, abstractClientPlayerEntity.walkDistO, abstractClientPlayerEntity.walkDist) * 6.0F) * 32.0F * t;
//                if (abstractClientPlayerEntity.isInSneakingPose()) {
//                    q += 25.0F;
//                }

                float declineFactor = 0.2f;
                matrixStack.translate(0, declineFactor * (r / 150), 0);

                matrixStack.mulPose(Axis.XP.rotationDegrees(6.0F + r / 2.0F + q));
                matrixStack.mulPose(Axis.YP.rotationDegrees(s / 2.0F));
                matrixStack.mulPose(Axis.ZP.rotationDegrees(s / 2.0F));
            }

        }
    }

    public static float setWingRoll(float originalValue, LivingEntity entity)
    {
        //System.out.println("input value for player " + entity.getName() + " is " + originalValue);
        if (entity instanceof AbstractClientPlayer) {
            if (entity.isFallFlying()) return originalValue;

            //float speed = (float) entity.getVelocity().length();
            double deltaX = Math.abs(entity.xo - entity.getX());
            double deltaY = Math.abs(entity.yo - entity.getY());
            double deltaZ = Math.abs(entity.zo - entity.getZ());

            float speed = (float) Mth.length(deltaX, deltaY, deltaZ);
            if (speed < 0.08) speed = 0;

            speed /= 3;
            speed = Mth.clamp(speed, 0f, 0.5f);

            return originalValue - speed;
        } else return originalValue;
    }
}
