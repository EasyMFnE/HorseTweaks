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

import java.io.File;
import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import com.mumfrey.liteloader.transformers.event.EventInfo;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.ReturnEventInfo;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

/**
 * LiteLoader-loaded mod that modifies the behavior of various Horse mechanics.
 *
 * @author Eric Hildebrand
 */
@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "horsetweaks.config.json")
public class LiteModHorseTweaks implements LiteMod, InitCompleteListener, Configurable {

  /**
   * Modify the return value of the EntityHorse.canWearArmor() method as needed.
   */
  public static void canWearArmor(ReturnEventInfo<EntityHorse, Boolean> event) {
    if (instance.canWearArmor) {
      event.setReturnValue(Boolean.TRUE);
    }
  }

  /**
   * Draw horse statistics onto the horse inventory GUI screen if enabled.
   */
  public static void drawHorseStats(EventInfo<GuiScreenHorseInventory> event, int mouseX, int mouseY) {
    if (instance.drawHorseStats && instance.guiScreenHorseInstanceField != null) {
      try {
        FontRenderer fontR = Minecraft.getMinecraft().fontRendererObj;
        EntityHorse horse =
            (EntityHorse) instance.guiScreenHorseInstanceField.get(event.getSource());
        if (horse.isChested()) {
          return;
        }
        fontR.drawString(
            I18n.format("horsetweaks.horsestats.jumpheight",
                formatFloatingPoint(getJumpHeight(horse.getHorseJumpStrength()), 2)), 82, 20,
            0x404040);
        fontR.drawString(I18n.format(
            "horsetweaks.horsestats.maxspeed",
            formatFloatingPoint(
                20.0 * horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                    .getAttributeValue(), 2)), 82, 32, 0x404040);
        fontR.drawString(
            I18n.format("horsetweaks.horsestats.maxhealth",
                formatFloatingPoint(horse.getHealth(), 1),
                formatFloatingPoint(horse.getMaxHealth(), 1)), 82, 44, 0x404040);
      } catch (IllegalArgumentException e) {
        instance.warning(e.toString());
      } catch (IllegalAccessException e) {
        instance.warning(e.toString());
      }
    }
  }

  /**
   * Format a floating point number to a specified precision. Will truncate the fractional digits if
   * the number is an integer.
   * 
   * @param floatingPoint The number to format.
   * @param precision The number of decimal places to truncate to.
   * @return The formatted number string.
   */
  private static String formatFloatingPoint(double floatingPoint, int precision) {
    if (floatingPoint == (int) floatingPoint) {
      return Integer.toString((int) floatingPoint);
    } else {
      return String.format("%." + precision + "f", floatingPoint);
    }
  }

  /**
   * Calculate possible jump height based on initial jump power (jump velocity).
   *
   * @param jumpPower Initial jump power (velocity).
   * @return Maximum jump height.
   */
  private static double getJumpHeight(double jumpPower) {
    return jumpPower * jumpPower / (2 * 0.08);
  }

  /**
   * Set the horse's texture layers based on it's type and armor, if enabled.
   */
  public static void setTextures(EventInfo<EntityHorse> event) {
    if (event.getSource().getHorseType() == 0) {
      return;
    }
    if (instance.textureLayersField != null && instance.renderArmorLayer) {
      try {
        String[] textureLayers = (String[]) instance.textureLayersField.get(event.getSource());
        switch (event.getSource().getHorseType()) {
          case 1:
            textureLayers[0] = "textures/entity/horse/donkey.png";
            break;
          case 2:
            textureLayers[0] = "textures/entity/horse/mule.png";
            break;
          case 3:
            textureLayers[0] = "textures/entity/horse/horse_zombie.png";
            break;
          case 4:
            textureLayers[0] = "textures/entity/horse/horse_skeleton.png";
            break;
          default:
            textureLayers[0] = "textures/entity/horse/horse_white.png";
        }
        instance.textureLayersField.set(event.getSource(), textureLayers);
      } catch (IllegalArgumentException e) {
        instance.severe(e.toString());
      } catch (IllegalAccessException e) {
        instance.severe(e.toString());
      }
    }
  }

  /** Name/Version information. */
  public static final String MOD_NAME = "HorseTweaks";
  public static final String MOD_VERSION = "1.0.0";

  /** Method references. */
  public static final String MOD_CLASS = "net.easymfne.horsetweaks.LiteModHorseTweaks";
  public static final MethodInfo canWearArmorMethod = new MethodInfo(MOD_CLASS, "canWearArmor");
  public static final MethodInfo setTexturesMethod = new MethodInfo(MOD_CLASS, "setTextures");
  public static final MethodInfo drawHorseStatsMethod = new MethodInfo(MOD_CLASS, "drawHorseStats");

  /** Modification instance. */
  public static LiteModHorseTweaks instance;

  /**
   * Whether or not to enable the ability to equip armor for all horse types.
   */
  @Expose
  @SerializedName("can_wear_armor")
  public boolean canWearArmor = true;

  /**
   * Whether to modify the horse rendering to show the armor layer.
   */
  @Expose
  @SerializedName("render_armor_layer")
  public boolean renderArmorLayer = true;

  /**
   * Whether to draw the horse's statistics to the horse's inventory screen.
   */
  @Expose
  @SerializedName("draw_horse_stats")
  public boolean drawHorseStats = true;

  /** Field references for reflection. */
  private Field textureLayersField = null;
  private Field guiScreenHorseInstanceField = null;

  /** Construct new instance of the mod and update static reference to it. */
  public LiteModHorseTweaks() {
    if (instance != null) {
      severe("Attempted to instantiate " + MOD_NAME + " twice.");
      throw new RuntimeException();
    } else {
      instance = this;
    }
  }

  /** Get the class responsible for configuration panel functionality. */
  @Override
  public Class<? extends ConfigPanel> getConfigPanelClass() {
    return GuiConfigPanel.class;
  }

  /** Get the human-readable modification name. */
  @Override
  public String getName() {
    return MOD_NAME;
  }

  /** Get the human-readable modification version. */
  @Override
  public String getVersion() {
    return MOD_VERSION;
  }

  /**
   * Log at INFO level.
   */
  public void info(String format, Object... data) {
    LiteLoaderLogger.info("[" + MOD_NAME + "] " + format, data);
  }

  /**
   * Set up reflection fields at startup.
   */
  @Override
  public void init(File configPath) {
    setupReflection();
  }

  /**
   * Nothing must be done post-initialization.
   */
  @Override
  public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {}

  /**
   * Nothing must be done per-tick.
   */
  @Override
  public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {}

  /**
   * Set up the reflection fields necessary for the modification functionality.
   */
  private void setupReflection() {
    for (String fieldName : HorseTweaksObf.guiScreenHorseInstance.names) {
      if (guiScreenHorseInstanceField == null) {
        try {
          guiScreenHorseInstanceField = GuiScreenHorseInventory.class.getDeclaredField(fieldName);
          guiScreenHorseInstanceField.setAccessible(true);
          info("Set Gui's horse instance visibility to true; horse stats will be accessible in the inventory screen.");
          break;
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        }
      }
    }
    for (String fieldName : HorseTweaksObf.textureLayers.names) {
      if (textureLayersField == null) {
        try {
          textureLayersField = EntityHorse.class.getDeclaredField(fieldName);
          textureLayersField.setAccessible(true);
          info("Successfully reflected horse texture layers; all horses can be rendered wearing armor.");
          break;
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        }
      }
    }
  }

  /**
   * Log at SEVERE level.
   */
  public void severe(String format, Object... data) {
    LiteLoaderLogger.severe("[" + MOD_NAME + "] " + format, data);
  }

  /**
   * Nothing must be done to upgrade settings.
   */
  @Override
  public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

  /**
   * Log at WARNING level.
   */
  public void warning(String format, Object... data) {
    LiteLoaderLogger.warning("[" + MOD_NAME + "] " + format, data);
  }

  /** Write current configuration values to disk. */
  public void writeConfig() {
    LiteLoader.getInstance().writeConfig(this);
  }

}
