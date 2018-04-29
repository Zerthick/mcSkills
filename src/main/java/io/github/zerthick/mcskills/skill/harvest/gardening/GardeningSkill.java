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

package io.github.zerthick.mcskills.skill.harvest.gardening;

import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.SkillIDs;
import io.github.zerthick.mcskills.api.skill.SkillPermissions;
import io.github.zerthick.mcskills.api.skill.ability.McSkillsAbility;
import io.github.zerthick.mcskills.skill.harvest.AbstractHarvestSkill;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Map;

public class GardeningSkill extends AbstractHarvestSkill {

    public GardeningSkill(Text skillName, Text skillDescription, Collection<McSkillsAbility> abilities, Map<BlockState, Integer> blockExperienceMap) {
        super(SkillIDs.GARDENING, SkillPermissions.GARDENING, skillName, skillDescription, abilities, blockExperienceMap);
    }

    @Override
    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {

        super.onBlockBreak(event, player);

        if (player.hasPermission(skillPermission)) {

            McSkillsExperienceService experienceService = getExperienceService();

            event.getTransactions().stream()
                    .map(Transaction::getOriginal)
                    .map(BlockSnapshot::getState)
                    .filter(blockState -> blockExperienceMap.containsKey(blockState))
                    .forEach(blockState -> {
                        if (blockState.getValue(Keys.GROWTH_STAGE).isPresent()) {
                            experienceService.addSkillExperience(player, skillID, blockExperienceMap.get(blockState));
                        }
                    });
        }

    }
}
