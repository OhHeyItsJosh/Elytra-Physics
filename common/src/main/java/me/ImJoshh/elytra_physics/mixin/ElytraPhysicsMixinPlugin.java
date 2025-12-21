package me.ImJoshh.elytra_physics.mixin;

import com.mojang.logging.LogUtils;
import me.ImJoshh.elytra_physics.mixinFlagSystem.ElytraPhysicsMixinFlags;
import me.ImJoshh.elytra_physics.mixinFlagSystem.MixinFlagsManager;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElytraPhysicsMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = LogUtils.getLogger();

    private Map<String, Boolean> mixinFlags;
    @Override
    public void onLoad(String mixinPackage) {
        // load mixin flags from file
        MixinFlagsManager mixinFlagsManager = new MixinFlagsManager(ElytraPhysicsMixinFlags.defaults());
        this.mixinFlags = mixinFlagsManager.loadMixinFlags();
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // conditionally apply either the injection or overwrite mixin depending on the mixin flags
        if (mixinClassName.equals("me.ImJoshh.elytra_physics.mixin.ClientAvatarStatePreventSnappingMixin")
                || mixinClassName.equals("me.ImJoshh.elytra_physics.mixin.ClientAvatarStateInjectConstMixin"))
        {
            Boolean applySnappingFix = this.mixinFlags.get(ElytraPhysicsMixinFlags.APPLY_SNAPPING_FIX);

            if (applySnappingFix && mixinClassName.equals("me.ImJoshh.elytra_physics.mixin.ClientAvatarStatePreventSnappingMixin"))
            {
                LOGGER.info("ClientAvatarStatePreventSnappingMixin was chosen to be loaded!");
                return true;
            }
            else if (!applySnappingFix && mixinClassName.equals("me.ImJoshh.elytra_physics.mixin.ClientAvatarStateInjectConstMixin"))
            {
                LOGGER.info("ClientAvatarStateInjectConstMixin was chosen to be loaded!");
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
