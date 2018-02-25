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

public abstract class AbstractMcSkillsPassiveAbility extends AbstractMcSkillsAbility {

    public AbstractMcSkillsPassiveAbility(String skillID, String abilityID, String abilityPermission, int abilityLevel, Text abilityName, Text abilityDescription) {
        super(skillID, abilityID, abilityPermission, abilityLevel, abilityName, abilityDescription);
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
    }
}
