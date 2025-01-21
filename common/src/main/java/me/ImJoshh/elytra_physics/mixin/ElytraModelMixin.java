package me.ImJoshh.elytra_physics.mixin;//package me.ImJoshh.elytra_physics.mixin;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraModel.class)
public abstract class ElytraModelMixin extends EntityModel<HumanoidRenderState> {
//    private T entity;
    @Shadow
    private final ModelPart rightWing;

    @Shadow
    private final ModelPart leftWing;

    protected ElytraModelMixin(ModelPart modelPart) {
        super(modelPart);
        this.leftWing = modelPart.getChild("left_wing");
        this.rightWing = modelPart.getChild("right_wing");
    }


    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void applyTransformation(HumanoidRenderState state, CallbackInfo info) {

//        if (state instanceof PlayerRenderState playerState) {
//            Quaternionf left = (new Quaternionf())
//                    .rotateX(0.2617994F)
//                    .rotateZ(-0.2617994F);
//
//            Quaternionf right = (new Quaternionf())
//                    .rotateX(0.2617994F)
//                    .rotateZ(0.2617994F);
//
//            Quaternionf transformation = (new Quaternionf())
//                    .rotateY(-3.1415927F)
//                    .rotateX((6.0F + playerState.capeLean / 2.0F + playerState.capeFlap) * -0.017453292F)
//                    .rotateZ(playerState.capeLean2 / 2.0F * 0.017453292F)
//                    .rotateY((180.0F - playerState.capeLean2 / 2.0F) * 0.017453292F);
//
////            this.applyRotation(left, playerState);
////            this.applyRotation(right, playerState);
//
//            double testRot = Math.sin(System.currentTimeMillis() / 1000.0d) * 5.0d;
////
////            this.leftWing.rotateBy(transformation);
////            System.out.println(playerState.capeLean);
////            this.rightWing.zRot += testRot;
//
//        }
    }

    private void applyRotation(Quaternionf quat, PlayerRenderState playerState) {
        quat
            .rotateY(-3.1415927F)
            .rotateX((6.0F + playerState.capeLean / 2.0F + playerState.capeFlap) * -0.017453292F)
            .rotateZ(playerState.capeLean2 / 2.0F * 0.017453292F)
            .rotateY((180.0F - playerState.capeLean2 / 2.0F) * 0.017453292F);
    }

//    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
//    private void setReference(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
//        this.entity = entity;
//    }
//
//    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "STORE"), ordinal = 6)
//    private float setSpread(float l) {
//        return ElytraPhysicsTransformations.setWingRoll(l, this.entity);
//    }

}
