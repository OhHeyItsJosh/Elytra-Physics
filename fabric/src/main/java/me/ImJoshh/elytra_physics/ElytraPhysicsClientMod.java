package me.ImJoshh.elytra_physics;
import me.ImJoshh.elytra_physics.config.ConfigData;
import me.ImJoshh.elytra_physics.config.ConfigKeys;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfigManager;
import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ElytraPhysicsClientMod implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("elytra-physics");

	@Override
	@SuppressWarnings("unchecked")
	public void onInitializeClient()
	{
		ElytraPhysicsConfigManager.init();

		List<String> injectLayersStrings = new ArrayList<>();

		// add layer injectors from config
		try {
			List<Object> layerInjectors = (List<Object>) ElytraPhysicsConfigManager.getConfigValue(ConfigKeys.LAYER_INJECTORS);
			if (layerInjectors != null)
			{
				List<String> injectorStrings = layerInjectors.stream().map(Object::toString).toList();

				injectLayersStrings.addAll(injectorStrings);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to apply config render layer injectors");
		}

		// add vanilla elytra class
		injectLayersStrings.add(WingsLayer.class.getName());


		// convert string list to class list for cheaper comparison
		for (String layerString : injectLayersStrings)
		{
			try {
				Class<RenderLayer<?, ?>> clazz = (Class<RenderLayer<?, ?>>) Class.forName(layerString);
				ConfigData.addLayerToInject(clazz);

				LOGGER.info("Successfully added class '" + clazz.getName() + "' to layer inject list");
			}
			catch (ClassNotFoundException e) {
				LOGGER.info("Class '" + layerString + "' not found");
			}
		}
	}

}