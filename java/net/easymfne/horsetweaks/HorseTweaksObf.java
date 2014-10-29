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

import com.mumfrey.liteloader.core.runtime.Obf;

/**
 * Obfuscation helper class that keeps track of the necessary class, method, and field names for the
 * functioning of Event injection.
 */
public class HorseTweaksObf extends Obf {

  /** The EntityHorse class. */
  public static HorseTweaksObf entityHorse = new HorseTweaksObf(
      "net.minecraft.entity.passive.EntityHorse", "wi");

  /** The canWearArmor() method of the EntityHorse class. */
  public static HorseTweaksObf canWearArmor = new HorseTweaksObf("func_110259_cr", "cB",
      "canWearArmor");

  /** The setHorseTexturePaths() method of the EntityHorse class. */
  public static HorseTweaksObf setHorseTexturePaths = new HorseTweaksObf("func_110247_cG", "cQ",
      "setHorseTexturePaths");
  
  /** The texture layers field of the EntityHorse class. */
  public static HorseTweaksObf textureLayers = new HorseTweaksObf("field_110280_bR", "bR");
  
  /** The GuiScreenHorseInventory class. */
  public static HorseTweaksObf guiScreenHorseInventory = new HorseTweaksObf(
      "net.minecraft.client.gui.inventory.GuiScreenHorseInventory", "bft");
  
  /** The drawGuiContainerForegroundLayer() method of the GuiScreenHorseInventory class. */
  public static HorseTweaksObf drawGuiContainerForegroundLayer = new HorseTweaksObf(
      "func_146979_b", "b", "drawGuiContainerForegroundLayer");
  
  /** The entityHorse instance field of the GuiScreenHorseInventory class. */
  public static HorseTweaksObf guiScreenHorseInstance = new HorseTweaksObf("x", "field_147034_x");

  /**
   * Create a new obfuscation mapping.
   *
   * @param seargeName Searge's name for it
   * @param obfName Obfuscated name for it
   */
  protected HorseTweaksObf(String seargeName, String obfName) {
    super(seargeName, obfName);
  }

  /**
   * Create a new obfuscation mapping.
   *
   * @param seargeName Searge's name for it
   * @param obfName Obfuscated name for it
   * @param mcpName MCP name for it
   */
  protected HorseTweaksObf(String seargeName, String obfName, String mcpName) {
    super(seargeName, obfName, mcpName);
  }

}
