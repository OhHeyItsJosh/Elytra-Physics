package me.ImJoshh.elytra_physics;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfig;
import me.ImJoshh.elytra_physics.config.FabricConfig;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElytraPhysicsClientMod implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("elytra_physics");

	@Override
	public void onInitializeClient()
	{
		FabricConfig.init();
		ElytraPhysics.setConfig(new ElytraPhysicsConfig(FabricConfig.CONFIG_BRIDGE));
	}

}