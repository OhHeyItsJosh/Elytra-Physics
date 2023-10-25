package me.ImJoshh.elytra_physics;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.ImJoshh.elytra_physics.config.ConfigKeys;
import me.ImJoshh.elytra_physics.config.ElytraPhysicsConfigManager;
import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
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

		// add layer injectors from config
		try {
			List<Object> layerInjectors = (List<Object>) ElytraPhysicsConfigManager.getConfigValue(ConfigKeys.LAYER_INJECTORS);
			if (layerInjectors != null)
			{
				List<String> injectorStrings = layerInjectors.stream().map(Object::toString).toList();

				INJECT_LAYERS_STRINGS.addAll(injectorStrings);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to apply config render layer injectors");
		}

		// add vanilla elytra class
		INJECT_LAYERS_STRINGS.add(ElytraLayer.class.getName());

		// convert string list to class list for cheaper comparison
		for (String layerString : INJECT_LAYERS_STRINGS)
		{
			try {
				Class<RenderLayer<?, ?>> clazz = (Class<RenderLayer<?, ?>>) Class.forName(layerString);
				INJECT_LAYERS.add(clazz);

				LOGGER.info("Successfully added class '" + clazz.getName() + "' to layer inject list");
			}
			catch (ClassNotFoundException e) {
				LOGGER.info("Class '" + layerString + "' not found");
			}
		}
	}

	public static Set<Class<RenderLayer<?, ?>>> INJECT_LAYERS = new HashSet<>();
	public static List<String> INJECT_LAYERS_STRINGS = new ArrayList<>();
}