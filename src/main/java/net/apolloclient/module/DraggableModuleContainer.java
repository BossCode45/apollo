/*⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼
  Copyright (C) 2020-2021 developed by Icovid and Apollo Development Team

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published
  by the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.
  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see https://www.gnu.org/licenses/.

  Contact: Icovid#3888 @ https://discord.com
 ⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼*/

package net.apolloclient.module;

import net.apolloclient.Apollo;
import net.apolloclient.event.Event;
import net.apolloclient.event.bus.EventContainer;
import net.apolloclient.event.bus.SubscribeEventContainer;
import net.apolloclient.module.bus.Instance;
import net.apolloclient.module.bus.ModContainer;
import net.apolloclient.module.bus.Module;
import net.apolloclient.module.bus.ModuleFactory;
import net.apolloclient.module.bus.draggable.ScreenPosition;
import net.apolloclient.module.bus.event.DisableEvent;
import net.apolloclient.module.bus.event.EnableEvent;
import net.apolloclient.module.bus.event.ModuleEvent;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Holds module information from any class annotated with {@link Module}
 * that implements {@link net.apolloclient.module.bus.draggable.Draggable}
 *
 * <p>Use to get class instance events are being called on if not using the
 * {@link Instance} annotation and to get display information for {@link } </p>
 *
 * @author Icovid | Icovid#3888
 * @since b0.2
 */
public class DraggableModuleContainer implements ModContainer {

    // Instance of the module class for events to be called on.
    private final Object instance;
    // Default screen position of module.
    private ScreenPosition screenPosition;

    // Information displayed in gui.
    private final String   name;
    private final String   description;
    private final String   author;
    private final Category category;

    // Information to help in finding / toggling module
    private final String[] aliases;
    private final String[] recommendedServersIP;
    private final String[] disallowedServersIP;

    // Can be set in run time
    private int priority;
    private boolean enabled;

    // HashMap of all events so they can be triggered in order of module priority
    private final HashMap<Class<? extends ModuleEvent>, CopyOnWriteArrayList<EventContainer>> handlers = new HashMap<>();
    private final HashMap<Class<? extends Event>, CopyOnWriteArrayList<SubscribeEventContainer>> events = new HashMap<>();

    public DraggableModuleContainer(Module moduleAnnotation, ScreenPosition screenPosition, Object instance) {
        this(moduleAnnotation.name(), moduleAnnotation.description(), moduleAnnotation.author(),
             moduleAnnotation.category(), moduleAnnotation.aliases(), moduleAnnotation.recommendedServersIP(),
             moduleAnnotation.disallowedServersIP(), moduleAnnotation.priority(), moduleAnnotation.enabled(), screenPosition, instance);
    }

    /**
     * @param name name of module
     * @param description description of what the module does
     * @param author author of module
     * @param category category for module to be displayed in
     * @param aliases aliases to show module for in search terms
     * @param recommendedServersIP servers module works best on
     * @param disallowedServersIP servers module is not allowed on
     * @param priority default priority of module
     * @param enabled default enabled state of module
     * @param screenPosition default screen position of module
     * @param instance instance of the module class for events to be called on
     */
    public DraggableModuleContainer(String name, String description, String author, Category category, String[] aliases, String[] recommendedServersIP, String[] disallowedServersIP, int priority, boolean enabled, ScreenPosition screenPosition, Object instance) {
        this.name                 = name;
        this.description          = description;
        this.author               = author;
        this.category             = category;
        this.aliases              = aliases;
        this.recommendedServersIP = recommendedServersIP;
        this.disallowedServersIP  = disallowedServersIP;
        this.priority             = priority;
        this.enabled              = enabled;
        this.screenPosition       = screenPosition;
        this.instance             = instance;

        if (this.enabled) Apollo.EVENT_BUS.register(this);
    }

    /**
     * Set module enabled state to certain value.
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this.enabled) {
            Apollo.EVENT_BUS.register(this);
            post(new EnableEvent(this));
        } else {
            Apollo.EVENT_BUS.unregister(this);
            post(new DisableEvent(this));
        }
    }

    /**
     * Sets priority of current module and sort {@link ModuleFactory} again.
     */
    @Override
    public void setPriority(int priority) {
        this.priority = priority;
        Apollo.MODULE_FACTORY.sortModules();
    }

    /**
     * Post an event to module and any module requesting its events
     *
     * @param moduleEvent event to be posted.
     */
    @Override
    public void post(ModuleEvent moduleEvent) {
        for (EventContainer eventContainer : handlers.getOrDefault(moduleEvent.getClass(), new CopyOnWriteArrayList<>()))
            eventContainer.invoke(moduleEvent);
    }

    /**
     * Toggle module enabled state
     */
    @Override
    public void toggle() {
        this.setEnabled(!enabled);
    }

    /** @return If module is currently enabled. */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /** @return Get the actual module class object */
    @Override
    public Object getInstance() {
        return instance;
    }

    /** @return Used to define settings in file / must be unique to module. */
    @Override
    public String getName() {
        return this.name;
    }

    /** @return Description of module displayed in gui list. */
    @Override
    public String getDescription() {
        return this.description;
    }

    /** @return Category used to section modules. */
    @Override
    public Category getCategory() {
        return this.category;
    }

    /** @return Aliases are search terms people can type instead of module name to find the module. */
    @Override
    public String[] getAliases() {
        return this.aliases;
    }

    /** @return Priority of modules events compared to other modules. */
    @Override
    public int getPriority() {
        return this.priority;
    }

    /** @return Author of module to be displayed in credits. */
    @Override
    public String getAuthor() {
        return this.author;
    }

    /** @return List of servers module is best compatible with. */
    @Override
    public String[] getRecommendedServersIPs() {
        return this.recommendedServersIP;
    }

    /** @return List of servers module is not allowed on.*/
    @Override
    public String[] getDisallowedServersIPs() {
        return this.disallowedServersIP;
    }

    /** @return HashMap of {@link ModuleEvent} with a list of {@link EventContainer} */
    @Override
    public HashMap<Class<? extends ModuleEvent>, CopyOnWriteArrayList<EventContainer>> getHandlers() { return handlers; }

    /** @return HashMap of {@link Event} with a list of {@link EventContainer} */
    @Override
    public HashMap<Class<? extends Event>, CopyOnWriteArrayList<SubscribeEventContainer>> getEvents() {
        return events;
    }
}

