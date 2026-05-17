package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("clickcounter")
	public interface ClickCounterConfig extends Config
	{
			@ConfigItem(
						keyName = "xpPorClick",
						name = "XP por click disponible",
						description = "Cuanta XP se necesita para ganar 1 click disponible (por defecto: 100)"
					)
			default int xpPorClick()
		{
					return 100;
		}

	@ConfigItem(
				keyName = "mostrarOverlay",
				name = "Mostrar overlay",
				description = "Muestra el contador de clicks en pantalla"
			)
			default boolean mostrarOverlay()
		{
					return true;
		}
	}
