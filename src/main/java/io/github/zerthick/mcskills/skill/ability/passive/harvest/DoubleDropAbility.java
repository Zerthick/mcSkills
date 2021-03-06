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

package io.github.zerthick.mcskills.skill.ability.passive.harvest;

import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.api.skill.ability.AbstractMcSkillsPassiveAbility;
import io.github.zerthick.mcskills.utils.RNGenerator;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Set;

public class DoubleDropAbility extends AbstractMcSkillsPassiveAbility {

    private final float scaleFactor;
    private final Set<BlockState> doubleDropBlocks;
    private final String skillID;

    public DoubleDropAbility(String abilityID, String abilityPermission, int abilityLevel, float scaleFactor, Set<BlockState> doubleDropBlocks, String skillID) {
        super(abilityID, abilityPermission, abilityLevel);
        this.scaleFactor = scaleFactor;
        this.doubleDropBlocks = doubleDropBlocks;
        this.skillID = skillID;
    }

    @Listener
    public void onItemDrop(DropItemEvent.Pre event, @Root BlockSnapshot blockSnapshot) {

        if (doubleDropBlocks.contains(blockSnapshot.getState())) {

            event.getCause().getContext().get(EventContextKeys.OWNER).ifPresent(user -> {
                if (user.hasPermission(abilityPermission)) {
                    McSkillsAccountService accountService = Sponge.getServiceManager().provideUnchecked(McSkillsAccountService.class);

                    int playerSkillLevel = accountService.getOrCreateAccount(user.getUniqueId()).getSkillLevel(skillID);

                    if (playerSkillLevel >= abilityLevel) {
                        int diff = playerSkillLevel - abilityLevel;
                        float scale = scaleFactor * diff;

                        float random = RNGenerator.generateRandomFloat(0, 100);
                        if (random <= scale) {

                            if (blockSnapshot.getState().getValue(Keys.GROWTH_STAGE).isPresent() || !blockSnapshot.getCreator().isPresent()) {

                                List<ItemStackSnapshot> drops = event.getDroppedItems();

                                event.getDroppedItems().addAll(drops);

                                blockSnapshot.getLocation().ifPresent(worldLocation -> {
                                    ParticleEffect particleEffect = ParticleEffect.builder()
                                            .type(ParticleTypes.FERTILIZER)
                                            .quantity(5)
                                            .build();
                                    worldLocation.getExtent().spawnParticles(particleEffect, worldLocation.getBlockPosition().toDouble());
                                });

                                user.getPlayer().ifPresent(player -> player.sendMessage(Text.of("Double Drops!")));
                            }
                        }
                    }
                }
            });
        }
    }
}
