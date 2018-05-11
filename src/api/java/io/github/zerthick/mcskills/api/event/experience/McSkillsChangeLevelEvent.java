/*
 * Copyright (C) 2018  Zerthick
 *
 * This file is part of mcSkills.
 *
 * mcSkills is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * mcSkills is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mcSkills.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.mcskills.api.event.experience;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;

/**
 * An event related to a change in mcSkills levels.
 */
public interface McSkillsChangeLevelEvent extends TargetPlayerEvent, Cancellable {

    /**
     * Get the skill level after the event has been processed.
     *
     * @return The skill level
     */
    int getLevel();

    /**
     * Sets the skill level after an event has been processed.
     * @param level The skill level
     */
    void setLevel(int level);

    /**
     * Gets the original skill level unmodified by event changes.
     * @return The skill level
     */
    int getOriginalLevel();

    /**
     * Gets the remaining experience after the event has been processed.
     * @return The experience
     */
    long getRemainingExperience();

    /**
     * Sets the remaining experience after the event has been processed.
     * @param experience The experience
     */
    void setRemainingExperience(long experience);

    /**
     * An event related to a gain in mcSkills levels.
     */
    interface Up extends McSkillsChangeLevelEvent {
    }

    /**
     * An event related to a loss in mcSkills levels.
     */
    interface Down extends McSkillsChangeLevelEvent {
    }

}
