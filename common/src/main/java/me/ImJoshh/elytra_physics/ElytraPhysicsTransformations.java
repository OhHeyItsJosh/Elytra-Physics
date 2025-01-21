package me.ImJoshh.elytra_physics;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.joml.Quaternionf;

public class ElytraPhysicsTransformations {

    public static void applyElytraTransformation(PoseStack matrixStack, PlayerRenderState playerState) {
        Quaternionf transformation = (new Quaternionf())
                .rotateY(-3.1415927F)
                .rotateX((6.0F + playerState.capeLean / 2.0F + playerState.capeFlap) * -0.017453292F)
                .rotateZ(playerState.capeLean2 / 2.0F * 0.017453292F)
                .rotateY((180.0F - playerState.capeLean2 / 2.0F) * 0.017453292F);

        float declineFactor = 0.2f;
        matrixStack.translate(0, declineFactor * (playerState.capeLean / 150), 0);

        matrixStack.mulPose(transformation);
    }

    public static float setWingSpread(PlayerRenderState playerState) {
        return (playerState.capeLean / 2.0F + playerState.capeFlap) * 0.004363323F;
    }
}
