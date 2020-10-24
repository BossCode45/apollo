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

package net.apolloclient.module.bus;

import net.apolloclient.module.Category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Allocates a class to be handled by the {@link ModuleFactory}. Any class found with this annotation applied will
 * be loaded as a module and the instance that is loaded will receive all event calls from the {@link ModuleFactory} as
 * long as the module is enabled when the event is posted. Settings can be added to the module with annotation</p>
 *
 * <li> {@link Instance } : Track instance of module created by {@link ModuleFactory}</li>
 * <li> {@link EventHandler } : Called on Module Specific functions such as {@code init} or {@code enable}</li>
 *
 * @author Icovid | Icovid#3888
 * @since 1.2.0-BETA
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {

    /**
     * @return Name of module to displayed in gui list.
     * <p>Used to define settings in file / must be unique to module</p>
     */
    String name();

    /**
     * @return Description of module displayed in gui list.
     */
    String description();

    /**
     * @return Category used to section modules.
     */
    Category category() default Category.UTIL;

    /**
     * @return Aliases are search terms people can type instead of module
     * name to find the module.
     */
    String[] aliases() default "";

    /**
     * @return Priority of modules events compared to other modules
     */
    int priority() default 5;

    /**
     * @return Author of module to be displayed in credits.
     */
    String author() default "Apollo Development Team";

    /**
     * @return If module should be enabled by default will first opening client.
     */
    boolean enabled() default false;

    /**
     * @return List of servers module is compatible with.
     * <p>Use this if module is dependant on certain aspects of a server such as chat formatting
     * or scoreboard information.</p>
     */
    String[] recommendedServersIP() default "";

    /**
     * @return List of servers module is not allowed on.
     * <p>Use this if module should always be disabled to follow server guidelines.</p>
     */
    String[] disallowedServersIP() default "";

}
