/**
 * This file is part of HorseTweaks by Eric Hildebrand.
 *
 * HorseTweaks is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * HorseTweaks is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with HorseTweaks. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package net.easymfne.horsetweaks;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import com.mumfrey.liteloader.client.gui.GuiCheckbox;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;

/**
 * In-game configuration panel with buttons for independently enabling and disabling functionality
 * and changing settings.
 */
public class GuiConfigPanel extends Gui implements ConfigPanel {

  /** Line spacing, in points. */
  private final static int SPACING = 16;

  /** Handy references. */
  private LiteModHorseTweaks mod;
  private Minecraft mc;

  /* Gui components. */
  private List<GuiButton> buttons;
  private GuiCheckbox canWearArmorBox;
  private GuiCheckbox renderArmorLayerBox;
  private GuiCheckbox drawHorseStatsBox;

  /* Active component tracker/reference. */
  private GuiButton activeButton;

  /** Construct a new configuration panel. */
  public GuiConfigPanel() {
    mc = Minecraft.getMinecraft();
  }

  /** Perform the action for the button. */
  private void actionPerformed(GuiButton button) {
    if (button == canWearArmorBox) {
      LiteModHorseTweaks.instance.canWearArmor = !LiteModHorseTweaks.instance.canWearArmor;
      canWearArmorBox.checked = LiteModHorseTweaks.instance.canWearArmor;
    } else if (button == renderArmorLayerBox) {
      LiteModHorseTweaks.instance.renderArmorLayer = !LiteModHorseTweaks.instance.renderArmorLayer;
      renderArmorLayerBox.checked = LiteModHorseTweaks.instance.renderArmorLayer;
    } else if (button == drawHorseStatsBox) {
      LiteModHorseTweaks.instance.drawHorseStats = !LiteModHorseTweaks.instance.drawHorseStats;
      drawHorseStatsBox.checked = LiteModHorseTweaks.instance.drawHorseStats;
    }

  }

  /** Draw the configuration panel's elements every refresh. */
  @Override
  public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks) {
    for (GuiButton button : buttons) {
      button.drawButton(mc, mouseX, mouseY);
    }
  }

  /** Get the height of the panel in points. */
  @Override
  public int getContentHeight() {
    return SPACING * buttons.size();
  }

  /** Get the title to display for the panel. */
  @Override
  public String getPanelTitle() {
    return I18n.format("horsetweaks.configpanel.title", LiteModHorseTweaks.MOD_NAME);
  }

  /** Allow ESCAPE and RETURN keys to close the configuration panel. */
  @Override
  public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode) {
    if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
      host.close();
    }
  }

  /** On mouse movement, nothing needs to be done. */
  @Override
  public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY) {}

  /** On click, activate button under cursor if one exists. */
  @Override
  public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
    for (GuiButton button : buttons) {
      if (button.mousePressed(mc, mouseX, mouseY)) {
        activeButton = button;
        button.playPressSound(mc.getSoundHandler());
        actionPerformed(button);
      }
    }
  }

  /** On release of click, deactivate the selected button (if any). */
  @Override
  public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
    if (activeButton != null) {
      activeButton.func_146111_b(mouseX, mouseY);
      activeButton = null;
    }
  }

  /** On closing of panel, save current configuration to disk. */
  @Override
  public void onPanelHidden() {
    mod.writeConfig();
  }

  /** On panel resize, nothing needs to be done. */
  @Override
  public void onPanelResize(ConfigPanelHost host) {}

  /** On opening of panel, instantiate the user interface components. */
  @Override
  public void onPanelShown(ConfigPanelHost host) {
    mod = (LiteModHorseTweaks) host.getMod();
    int id = 0;
    int line = 0;
    buttons = new ArrayList<GuiButton>();
    buttons.add(canWearArmorBox =
        new GuiCheckbox(id++, 10, SPACING * line++, I18n
            .format("horsetweaks.configpanel.canweararmor")));
    canWearArmorBox.checked = LiteModHorseTweaks.instance.canWearArmor;
    buttons.add(renderArmorLayerBox =
        new GuiCheckbox(id++, 10, SPACING * line++, I18n
            .format("horsetweaks.configpanel.renderarmorlayer")));
    renderArmorLayerBox.checked = LiteModHorseTweaks.instance.renderArmorLayer;
    buttons.add(drawHorseStatsBox =
        new GuiCheckbox(id++, 10, SPACING * line++, I18n
            .format("horsetweaks.configpanel.drawhorsestats")));
    drawHorseStatsBox.checked = LiteModHorseTweaks.instance.drawHorseStats;
  }

  /** On each tick, nothing needs to be done. */
  @Override
  public void onTick(ConfigPanelHost host) {}

}
