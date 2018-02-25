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

package io.github.zerthick.mcskills.skill.harvest.mining;

import io.github.zerthick.mcskills.api.skill.ability.McSkillsAbility;
import io.github.zerthick.mcskills.skill.harvest.AbstractHarvestSkill;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Map;

public class MiningSkill extends AbstractHarvestSkill {

    public MiningSkill(String skillID, String skillPermission, Text skillName, Text skillDescription, Collection<McSkillsAbility> abilities, Map<BlockState, Integer> blockExperienceMap) {
        super(skillID, skillPermission, skillName, skillDescription, abilities, blockExperienceMap);
    }
}
