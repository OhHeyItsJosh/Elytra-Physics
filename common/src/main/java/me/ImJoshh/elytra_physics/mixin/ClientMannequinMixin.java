package me.ImJoshh.elytra_physics.mixin;

import me.ImJoshh.elytra_physics.HasLivingEntityRef;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientMannequin.class)
public abstract class ClientMannequinMixin extends Mannequin implements ClientAvatarEntity {

    @Shadow
    private ClientAvatarState avatarState;

    public ClientMannequinMixin(EntityType<Mannequin> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void bindMannequinRef(CallbackInfo ci) {
        HasLivingEntityRef mixinState = (HasLivingEntityRef) this.avatarState;
        mixinState.elytraPhysics$setEntityRef(this);
    }
}
