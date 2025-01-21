package me.ImJoshh.elytra_physics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.ImJoshh.elytra_physics.ElytraPhysicsTransformations;
import me.ImJoshh.elytra_physics.config.ConfigData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntityRenderer.class, priority = 500)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends EntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M>
{
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

//    @Inject(method="render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at=@At("HEAD"))
//    private void test(S state, PoseStack pose, MultiBufferSource m, int i, CallbackInfo callbackInfo) {
//        System.out.println("Test");
//    }

    @WrapOperation(method="render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V"
    ))
    private void injectTransformation(RenderLayer<S, M> instance, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, S entityRenderState, float f1, float f2, Operation original)
    {
        if (entityRenderState instanceof PlayerRenderState playerState)
        {
            boolean shouldInject = false;

            // check whether the render is an elytra renderer
            for (Class<RenderLayer<?, ?>> clazz : ConfigData.getLayersToInject())
            {
                if (clazz.equals(instance.getClass()))
                {
                    shouldInject = true;
                    break;
                }
            }

            if (shouldInject)
            {
                poseStack.pushPose();
                ElytraPhysicsTransformations.applyTransformTest(poseStack, playerState);
//                ElytraPhysicsTransformations.applyMovementTransformation(poseStack, (T) livingEntity, partialTick);
            }

            original.call(instance, poseStack, multiBufferSource, i, playerState, playerState.yRot, playerState.xRot);

            // pop the pose from the stack once it has been rendered
            if (shouldInject)
                poseStack.popPose();
        }
        else {
            LivingEntityRenderState state = (LivingEntityRenderState) entityRenderState;
            original.call(instance, poseStack, multiBufferSource, i, state, state.yRot, state.xRot);
        }
    }
}
