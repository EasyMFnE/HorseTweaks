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

import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.inject.BeforeReturn;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;

/**
 * Event injection to allow modification of vanilla behaviors.
 */
public class HorseTweaksEventTransformer extends EventInjectionTransformer {

  /**
   * Create and add events, adding listener hooks in the process.
   */
  @Override
  protected void addEvents() {
    /* Create hook for allowing atypical horse types to wear armor. */
    addEvent(Event.getOrCreate("HorseTweaks_EntityHorse_canWearArmor", true),
        new MethodInfo(HorseTweaksObf.entityHorse, HorseTweaksObf.canWearArmor, Boolean.TYPE),
        new MethodHead()).addListener(LiteModHorseTweaks.canWearArmorMethod);

    /* Create hook for fixing multi-layer texturing of atypically armored horses. */
    addEvent(Event.getOrCreate("HorseTweaks_EntityHorse_setHorseTexturesPath", true),
        new MethodInfo(HorseTweaksObf.entityHorse, HorseTweaksObf.setHorseTexturePaths, Void.TYPE),
        new BeforeReturn()).addListener(LiteModHorseTweaks.setTexturesMethod);

    /* Create hook for drawing horse statistics into the horse inventory GUI. */
    addEvent(
        Event.getOrCreate("HorseTweaks_GuiScreenHorseInventory_drawGuiContainerForegroundLayer",
            true),
        new MethodInfo(HorseTweaksObf.guiScreenHorseInventory,
            HorseTweaksObf.drawGuiContainerForegroundLayer, Void.TYPE, Integer.TYPE, Integer.TYPE),
        new MethodHead()).addListener(LiteModHorseTweaks.drawHorseStatsMethod);
  }

}
