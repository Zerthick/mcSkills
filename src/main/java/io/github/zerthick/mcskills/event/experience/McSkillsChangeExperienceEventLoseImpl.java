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

package io.github.zerthick.mcskills.event.experience;

import io.github.zerthick.mcskills.api.event.experience.McSkillsChangeExperienceEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

public class McSkillsChangeExperienceEventLoseImpl implements McSkillsChangeExperienceEvent.Lose {

    private final Player player;
    private final Cause cause;
    private final long originalExperience;
    private long experience;
    private boolean cancelled = false;

    public McSkillsChangeExperienceEventLoseImpl(Player player, long experience, Cause cause) {
        this.player = player;
        this.cause = cause;
        originalExperience = experience;
        this.experience = experience;
    }

    @Override
    public long getExperience() {
        return experience;
    }

    @Override
    public void setExperience(long experience) {
        this.experience = experience;
    }

    @Override
    public long getOriginalExperience() {
        return originalExperience;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Player getTargetEntity() {
        return player;
    }

    @Override
    public Cause getCause() {
        return cause;
    }
}
