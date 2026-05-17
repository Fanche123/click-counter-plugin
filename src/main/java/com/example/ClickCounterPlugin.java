package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.MouseListener;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.event.MouseEvent;

@Slf4j
	@PluginDescriptor(
			name = "Click Counter",
			description = "Cuenta tus clicks y te otorga clicks disponibles por XP obtenida",
			tags = {"click", "counter", "xp"}
		)
	public class ClickCounterPlugin extends Plugin implements MouseListener
	{
			@Inject
			private Client client;

	@Inject
			private ClickCounterConfig config;

	@Inject
			private OverlayManager overlayManager;

	@Inject
			private MouseManager mouseManager;

	@Inject
			private ClickCounterOverlay overlay;

	@Getter
			private int clicksRealizados = 0;

	@Getter
			private int clicksGanadosPorXp = 0;

	private int xpAcumulada = 0;

	private int[] xpAnterior = new int[Skill.values().length];

	@Override
			protected void startUp() throws Exception
		{
					overlayManager.add(overlay);
					mouseManager.registerMouseListener(this);

				for (Skill skill : Skill.values())
					{
									int idx = skill.ordinal();
									if (idx < xpAnterior.length)
									{
														xpAnterior[idx] = client.getSkillExperience(skill);
									}
					}

				log.debug("Click Counter iniciado!");
		}

	@Override
			protected void shutDown() throws Exception
		{
					overlayManager.remove(overlay);
					mouseManager.unregisterMouseListener(this);
					log.debug("Click Counter detenido!");
		}

	@Subscribe
			public void onStatChanged(StatChanged statChanged)
		{
					Skill skill = statChanged.getSkill();
					int idx = skill.ordinal();

				if (idx >= xpAnterior.length)
				{
								return;
				}

				int xpActual = client.getSkillExperience(skill);
					int diferencia = xpActual - xpAnterior[idx];

				if (diferencia > 0)
				{
								xpAnterior[idx] = xpActual;
								xpAcumulada += diferencia;

						int clicksNuevos = xpAcumulada / config.xpPorClick();
								if (clicksNuevos > 0)
								{
													clicksGanadosPorXp += clicksNuevos;
													xpAcumulada -= clicksNuevos * config.xpPorClick();
													log.debug("XP ganada: +{} XP -> +{} click(s) disponible(s)", diferencia, clicksNuevos);
								}
				}
		}

	public int getClicksTotales()
		{
					return clicksRealizados + clicksGanadosPorXp;
		}

	public int getClicksDisponibles()
		{
					int disponibles = clicksGanadosPorXp - clicksRealizados;
					return Math.max(disponibles, 0);
		}

	@Override
			public MouseEvent mouseClicked(MouseEvent mouseEvent)
		{
					if (mouseEvent.getButton() == MouseEvent.BUTTON1)
					{
									clicksRealizados++;
					}
					return mouseEvent;
		}

	@Override
			public MouseEvent mousePressed(MouseEvent mouseEvent) { return mouseEvent; }

	@Override
			public MouseEvent mouseReleased(MouseEvent mouseEvent) { return mouseEvent; }

	@Override
			public MouseEvent mouseEntered(MouseEvent mouseEvent) { return mouseEvent; }

	@Override
			public MouseEvent mouseExited(MouseEvent mouseEvent) { return mouseEvent; }

	@Override
			public MouseEvent mouseDragged(MouseEvent mouseEvent) { return mouseEvent; }

	@Override
			public MouseEvent mouseMoved(MouseEvent mouseEvent) { return mouseEvent; }

	@Provides
			ClickCounterConfig provideConfig(ConfigManager configManager)
		{
					return configManager.getConfig(ClickCounterConfig.class);
		}
	}
