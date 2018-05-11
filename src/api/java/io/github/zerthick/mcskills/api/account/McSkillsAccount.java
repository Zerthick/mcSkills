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

package io.github.zerthick.mcskills.api.account;

import java.util.UUID;

/**
 * Represents a mcSkills player account, containing the current skill experience and levels
 * of a player.
 */
public interface McSkillsAccount {

    /**
     * Retrieve the player {@link UUID} associated with this account
     *
     * @return the player {@link UUID} associated with this account
     */
    UUID getPlayerUUID();

    /**
     * Retrieve the current level of a skill within this account.
     * If the player has no levels or this account does not contain and entry
     * for a skill, should return 0.
     * @param skillID The id of the skill to retrieve
     * @return The player's current level
     */
    int getSkillLevel(String skillID);

    /**
     * Retrieve the current experience of a skill within this account.
     * If the player has no experience or this account does not contain and entry
     * for a skill, should return 0.
     * @param skillID The id of the skill to retrieve
     * @return The player's current experience
     */
    long getSkillExperience(String skillID);

    /**
     * Set the current level of a skill within this account.
     * The level should be set to a non-negative value.
     * @param skillID The id of the skill to set
     * @param level The level to set the skill to
     */
    void setSkillLevel(String skillID, int level);

    /**
     * Set the current amount of experience of a skill within this account.
     * The amount of experience should be set to a non-negative value.
     * @param skillID The ide of the skill to set
     * @param experience The amount of experience to set the skill to
     */
    void setSkillExperience(String skillID, long experience);
}
