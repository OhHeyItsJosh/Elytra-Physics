package me.ImJoshh.elytra_physics.mixin;

import com.mojang.authlib.GameProfile;
import me.ImJoshh.elytra_physics.HasLivingEntityRef;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
abstract public class AbstractClientPlayerMixin extends Player implements ClientAvatarEntity {

    @Shadow
    private ClientAvatarState clientAvatarState;

    public AbstractClientPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void bindPlayerRef(CallbackInfo ci) {
        HasLivingEntityRef mixinState = (HasLivingEntityRef) this.clientAvatarState;
        mixinState.elytraPhysics$setEntityRef(this);
    }
}
