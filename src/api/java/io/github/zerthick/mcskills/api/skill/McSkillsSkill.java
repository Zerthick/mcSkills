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

package io.github.zerthick.mcskills.api.skill;

import io.github.zerthick.mcskills.api.skill.ability.McSkillsAbility;
import org.spongepowered.api.text.Text;

import java.util.Collection;

/**
 * Represents a skill for which the player can gain experience and levels.
 * <p>
 * Note: It is recommended that instead of implementing this interface directly, you instead extend
 * {@link AbstractMcSkillsSkill}.
 */
public interface McSkillsSkill {

    /**
     * Get the ID of this skill.
     * @return The ID
     */
    String getSkillID();

    /**
     * Get the permission node of this skill.
     * @return The permission node
     */
    String getSkillPermission();

    /**
     * Get the name of this skill, formatted as {@link Text}.
     * @return The skill name
     */
    Text getSkillName();

    /**
     * Get the description of this skill, formatted as {@link Text}.
     * @return The skill description
     */
    Text getSkillDescription();

    /**
     * Gets the abilities associated with this skill.
     * @return The skill abilities
     */
    Collection<McSkillsAbility> getAbilities();

    /**
     * Method for registering any necessary listeners for this skill.
     * @param plugin The main plugin object
     */
    void registerListeners(Object plugin);

}
