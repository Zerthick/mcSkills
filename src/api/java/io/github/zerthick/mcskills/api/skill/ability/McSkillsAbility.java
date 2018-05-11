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

package io.github.zerthick.mcskills.api.skill.ability;

import org.spongepowered.api.text.Text;

/**
 * Represents either an active or passive ability of a {@link io.github.zerthick.mcskills.api.skill.McSkillsSkill}.
 * <p>
 * Note: It is recommended that instead of implementing this interface directly, you instead extend either
 * {@link AbstractMcSkillsActiveAbility} or {@link AbstractMcSkillsPassiveAbility}.
 */
public interface McSkillsAbility {

    /**
     * Get the ID of this ability.
     * @return The ID
     */
    String getAbilityID();

    /**
     * Get the permission node of this ability.
     * @return The permission node
     */
    String getAbilityPermission();

    /**
     * Get the minimum skill level necessary to use this ability.
     * @return The minimum skill level
     */
    int getAbilityLevel();

    /**
     * Get the name of this ability, formatted as {@link Text}.
     * @return The ability name
     */
    Text getAbilityName();

    /**
     * Get the description of this ability, formatted as {@link Text}.
     * @return The ability description
     */
    Text getAbilityDescription();

    /**
     * Get the {@link AbilityType} of this ability.
     * @return The {@link AbilityType}
     */
    AbilityType getAbilityType();

    /**
     * Method for registering any necessary listeners for this ability.
     * @param plugin The main plugin object
     */
    void registerListeners(Object plugin);

}
