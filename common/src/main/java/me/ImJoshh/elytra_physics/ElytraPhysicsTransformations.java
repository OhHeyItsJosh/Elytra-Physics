package me.ImJoshh.elytra_physics;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.joml.Quaternionf;

public class ElytraPhysicsTransformations {

    public static void applyElytraTransformation(PoseStack matrixStack, AvatarRenderState avatarState) {
        Quaternionf transformation = (new Quaternionf())
                .rotateY(-3.1415927F)
                .rotateX(((0.85f * avatarState.capeLean) / 2.0F + avatarState.capeFlap) * -0.017453292F)
                .rotateZ(avatarState.capeLean2 / 2.0F * 0.017453292F)
                .rotateY((180.0F - avatarState.capeLean2 / 2.0F) * 0.017453292F);

        float declineFactor = 0.2f;
        matrixStack.translate(0, declineFactor * (avatarState.capeLean / 150), 0);

        matrixStack.mulPose(transformation);
    }

    public static float setWingSpread(AvatarRenderState avatarState) {
        return (avatarState.capeLean / 2.0F + avatarState.capeFlap) * 0.004363323F;
    }
}
