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

package io.github.zerthick.mcskills.skill.harvest;

import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.AbstractMcSkillsSkill;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;

import java.util.Map;

public class AbstractHarvestSkill extends AbstractMcSkillsSkill {

    protected Map<BlockState, Integer> blockExperienceMap;

    public AbstractHarvestSkill(String skillID, String skillPermission, Text skillName, Text skillDescription, Map<BlockState, Integer> blockExperienceMap) {
        super(skillID, skillPermission, skillName, skillDescription);
        this.blockExperienceMap = blockExperienceMap;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {

        if (player.hasPermission(skillPermission)) {

            McSkillsExperienceService experienceService = getExperienceService();

            event.getTransactions().stream()
                    .map(Transaction::getOriginal)
                    .filter(blockSnapshot -> !blockSnapshot.getCreator().isPresent())
                    .map(BlockSnapshot::getState)
                    .filter(blockState -> blockExperienceMap.containsKey(blockState))
                    .forEach(blockState ->
                            experienceService.addSkillExperience(player, skillID, blockExperienceMap.get(blockState)));
        }

    }

}
