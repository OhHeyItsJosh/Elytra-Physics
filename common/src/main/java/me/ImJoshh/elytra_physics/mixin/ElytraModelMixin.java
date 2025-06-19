package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.ElytraPhysicsTransformations;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ElytraModel.class)
public abstract class ElytraModelMixin<T extends LivingEntity> extends AgeableListModel<T> {

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "STORE"), ordinal = 6)
    private float setSpread(float l, T entity) {
        return ElytraPhysicsTransformations.setWingRoll(l, entity);
    }

}
