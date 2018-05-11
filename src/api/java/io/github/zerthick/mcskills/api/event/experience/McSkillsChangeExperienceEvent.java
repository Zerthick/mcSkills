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
 * An event related to a change in mcSkills experience.
 */
public interface McSkillsChangeExperienceEvent extends TargetPlayerEvent, Cancellable {

    /**
     * Get the experience after the event has been processed.
     *
     * @return The experience
     */
    long getExperience();

    /**
     * Sets the amount of experience after an event has been processed.
     * @param experience the amount of experience
     */
    void setExperience(long experience);

    /**
     * Gets the original experience unmodified by event changes.
     * @return The experience
     */
    long getOriginalExperience();

    /**
     * An event related to gaining mcSkills experience.
     */
    interface Gain extends McSkillsChangeExperienceEvent {
    }

    /**
     * A event related to losing mcSkills experience.
     */
    interface Lose extends McSkillsChangeExperienceEvent {
    }
}
