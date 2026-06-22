package me.ImJoshh.elytra_physics.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.ImJoshh.elytra_physics.config.ui.ElytraPhysicsConfigScreen;

public class ModmenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ElytraPhysicsConfigScreen::new;
    }
}
