package me.ImJoshh.elytra_physics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import me.ImJoshh.elytra_physics.ElytraPhysicsTransformations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LivingEntityRenderer.class, priority = 500)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M>
{
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    @WrapOperation(method="render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V"
    ))
    private void injectTransformation(RenderLayer<T, M> instance, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight,
                                      Entity livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                                      float netHeadYaw, float headPitch, Operation original)
    {
        if (livingEntity instanceof LivingEntity)
        {
            boolean shouldInject = false;

            // check whether the render is an elytra renderer
            for (Class<RenderLayer<?, ?>> clazz : ElytraPhysicsClientMod.INJECT_LAYERS)
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
                ElytraPhysicsTransformations.applyMovementTransformation(poseStack, (T) livingEntity, partialTick);
            }

            original.call(instance, poseStack, multiBufferSource, packedLight, (T) livingEntity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);

            // pop the pose from the stack once it has been rendered
            if (shouldInject)
                poseStack.popPose();
        }
    }
}
