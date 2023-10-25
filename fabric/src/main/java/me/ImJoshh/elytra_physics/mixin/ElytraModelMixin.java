package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysicsClientMod;
import me.ImJoshh.elytra_physics.ElytraPhysicsTransformations;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraModel.class)
public abstract class ElytraModelMixin<T extends LivingEntity> extends AgeableListModel<T> {
    private T entity;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void setReference(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        this.entity = entity;
    }

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "STORE"), ordinal = 6)
    private float setSpread(float l) {
        return ElytraPhysicsTransformations.setWingRoll(l, this.entity);
    }

}
