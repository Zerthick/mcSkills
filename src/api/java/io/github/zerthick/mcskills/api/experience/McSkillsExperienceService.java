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

package io.github.zerthick.mcskills.api.experience;

import io.github.zerthick.mcskills.api.experience.formula.McSkillsExperienceFormula;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Represents a service for adding and removing mcSkills experience from players.
 */
public interface McSkillsExperienceService {

    /**
     * Sets the {@link McSkillsExperienceFormula} to be used by this service.
     *
     * @param experienceFormula The {@link McSkillsExperienceFormula}
     */
    void setExperienceFormula(McSkillsExperienceFormula experienceFormula);

    /**
     * Calculate the amount of experience required to complete a level using the {@link McSkillsExperienceFormula} of this service.
     * @param level The level to be calculated
     * @return The amount of experience required to complete the level
     */
    long getLevelExperience(int level);

    /**
     * Attempts to add an amount of experience to the {@link io.github.zerthick.mcskills.api.skill.McSkillsSkill} entry
     * within the {@link io.github.zerthick.mcskills.api.account.McSkillsAccount} associated with the specified
     * {@link Player}.
     *
     * This should be used in favor of modifying the {@link io.github.zerthick.mcskills.api.account.McSkillsAccount}
     * directly as it fires all appropriate {@link io.github.zerthick.mcskills.api.event.experience.McSkillsChangeExperienceEvent}
     * and {@link io.github.zerthick.mcskills.api.event.experience.McSkillsChangeLevelEvent} events.
     *
     * Due to the amount of experience applied being modifiable in these events it is not guaranteed the specified amount
     * will be added, if for example the resultant events are cancelled.
     * @param player The {@link Player} who's account is to be modified
     * @param skillID The ID of the {@link io.github.zerthick.mcskills.api.skill.McSkillsSkill} for which experience
     *                should be added
     * @param experience The amount of experience which should be added
     */
    void addSkillExperience(Player player, String skillID, long experience);

    /**
     * Attempts to remove an amount of experience from the {@link io.github.zerthick.mcskills.api.skill.McSkillsSkill} entry
     * within the {@link io.github.zerthick.mcskills.api.account.McSkillsAccount} associated with the specified
     * {@link Player}.
     *
     * This should be used in favor of modifying the {@link io.github.zerthick.mcskills.api.account.McSkillsAccount}
     * directly as it fires all appropriate {@link io.github.zerthick.mcskills.api.event.experience.McSkillsChangeExperienceEvent}
     * and {@link io.github.zerthick.mcskills.api.event.experience.McSkillsChangeLevelEvent} events.
     *
     * Due to the amount of experience applied being modifiable in these events it is not guaranteed the specified amount
     * will be removed, if for example the resultant events are cancelled.
     * @param player The {@link Player} who's account is to be modified
     * @param skillID The ID of the {@link io.github.zerthick.mcskills.api.skill.McSkillsSkill} for which experience
     *                should be removed
     * @param experience The amount of experience which should be removed
     */
    void removeSkillExperience(Player player, String skillID, long experience);

}
