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

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a service for registering skills
 */
public interface McSkillsSkillService {

    /**
     * Register a {@link McSkillsSkill}.
     * <p>
     * Should be called during the {@link org.spongepowered.api.event.game.state.GameStartedServerEvent}.
     *
     * @param skill The {@link McSkillsSkill} to register.
     */
    void registerSkill(McSkillsSkill skill);

    /**
     * Retrieves the registered {@link McSkillsSkill} with the specified ID if it exists.
     * @param skillID The ID of the skill to retrieve
     * @return {@link Optional} containing the {@link McSkillsSkill} if it exists or {@link Optional#empty()} otherwise.
     */
    Optional<McSkillsSkill> getSkill(String skillID);

    /**
     * Retrive all of the registered skills.
     * @return All of the registered skills
     */
    Collection<McSkillsSkill> getRegisteredSkills();

}
