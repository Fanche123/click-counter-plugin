package com.example;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class ClickCounterOverlay extends Overlay
  {
    	private final ClickCounterPlugin plugin;
    	private final ClickCounterConfig config;
    	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
    	public ClickCounterOverlay(ClickCounterPlugin plugin, ClickCounterConfig config)
    {
      		this.plugin = plugin;
      		this.config = config;

    		setPosition(OverlayPosition.TOP_LEFT);
      		setLayer(OverlayLayer.ABOVE_SCENE);
    }

	@Override
    	public Dimension render(Graphics2D graphics)
    {
      		if (!config.mostrarOverlay())
          {
            			return null;
          }

    		panelComponent.getChildren().clear();

    		panelComponent.getChildren().add(TitleComponent.builder()
                                         			.text("Click Counter")
                                         			.color(Color.YELLOW)
                                         			.build());

    		panelComponent.getChildren().add(LineComponent.builder()
                                         			.left("Clicks Totales:")
                                         			.leftColor(Color.WHITE)
                                         			.right(String.valueOf(plugin.getClicksTotales()))
                                         			.rightColor(Color.CYAN)
                                         			.build());

    		panelComponent.getChildren().add(LineComponent.builder()
                                         			.left("Clicks Realizados:")
                                         			.leftColor(Color.WHITE)
                                         			.right(String.valueOf(plugin.getClicksRealizados()))
                                         			.rightColor(Color.ORANGE)
                                         			.build());

    		int disponibles = plugin.getClicksDisponibles();
      		Color colorDisponibles = disponibles > 0 ? Color.GREEN : Color.RED;

    		panelComponent.getChildren().add(LineComponent.builder()
                                         			.left("Clicks Disponibles:")
                                         			.leftColor(Color.WHITE)
                                         			.right(String.valueOf(disponibles))
                                         			.rightColor(colorDisponibles)
                                         			.build());

    		return panelComponent.render(graphics);
    }
  }
