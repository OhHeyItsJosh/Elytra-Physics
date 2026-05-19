package me.ImJoshh.elytra_physics;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
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

    public static boolean shouldInjectLayer(RenderLayer<?, ?> layer) {
        // check whether the render layer is an elytra renderer
        for (Class<RenderLayer<?, ?>> clazz : getConfig().getElytraLayers())
        {
            if (clazz.equals(layer.getClass()))
            {
                return true;
            }
        }

        return false;
    }

    public static void applyElytraTransformation(PoseStack matrixStack, AvatarRenderState avatarState) {
        float leanMultiplier = getConfig().getLeanMultiplier();
        float leanMultiplierAdjusted = !avatarState.isVisuallySwimming
                ? leanMultiplier * 0.85f
                : leanMultiplier * 0.25f;

        Quaternionf transformation = (new Quaternionf())
                .rotateX(((leanMultiplierAdjusted * avatarState.capeLean) / 2.0F + avatarState.capeFlap) * 0.017453292F) // TILT
                .rotateZ((avatarState.capeLean2 / 2.0F) * 0.017453292F) // PAN
                .rotateY((-avatarState.capeLean2 / 2.0F) * 0.017453292F); // ROLL

        // fade transformation out while flying
        if (avatarState.isFallFlying) {
            transformation = transformation.slerp(new Quaternionf(), avatarState.fallFlyingScale());
        }

        float declineFactor = 0.2f * getConfig().getDeclineMultiplier();
        matrixStack.translate(0.0f, declineFactor * (avatarState.capeLean / 150.0f), 0.0f);

        matrixStack.mulPose(transformation);
    }

    public static float setWingSpread(AvatarRenderState avatarState) {
        return (getConfig().getSpreadMultiplier() * 0.75f * avatarState.capeLean / 2.0F + avatarState.capeFlap) * 0.004363323F;
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
