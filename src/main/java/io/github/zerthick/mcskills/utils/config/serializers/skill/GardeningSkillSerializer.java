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

package io.github.zerthick.mcskills.utils.config.serializers.skill;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.mcskills.api.skill.SkillIDs;
import io.github.zerthick.mcskills.api.skill.ability.AbilityIDs;
import io.github.zerthick.mcskills.api.skill.ability.AbilityPermissions;
import io.github.zerthick.mcskills.skill.ability.passive.harvest.DoubleDropAbility;
import io.github.zerthick.mcskills.skill.harvest.gardening.GardeningSkill;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.HashMap;
import java.util.Map;

public class GardeningSkillSerializer implements TypeSerializer<GardeningSkill> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GardeningSkill.class), new GardeningSkillSerializer());
    }

    @Override
    public GardeningSkill deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        Text skillName = TextSerializers.FORMATTING_CODE.deserialize(value.getNode("skillName").getString());
        Text skillDescription = TextSerializers.FORMATTING_CODE.deserialize(value.getNode("skillDescription").getString());
        Map<BlockState, Integer> blockExperienceMap = new HashMap<>();

        Map<BlockType, Integer> blockTypeExperienceMap = value.getNode("experience", "blockTypes").getValue(new TypeToken<Map<BlockType, Integer>>() {
        });
        blockTypeExperienceMap.forEach((k, v) -> k.getAllBlockStates().forEach(blockState -> blockExperienceMap.put(blockState, v)));

        blockExperienceMap.putAll(value.getNode("experience", "blockStates").getValue(new TypeToken<Map<BlockState, Integer>>() {
        }, new HashMap<>()));

        GardeningSkill gardeningSkill = new GardeningSkill(blockExperienceMap);
        gardeningSkill.setSkillName(skillName);
        gardeningSkill.setSkillDescription(skillDescription);

        // Abilities
        ConfigurationNode abilityNode = value.getNode("abilities");

        // Double Drops
        ConfigurationNode doubleDropNode = abilityNode.getNode("doubleDrops");
        Text abilityName = TextSerializers.FORMATTING_CODE.deserialize(doubleDropNode.getNode("abilityName").getString());
        Text abilityDescription = TextSerializers.FORMATTING_CODE.deserialize(doubleDropNode.getNode("abilityDescription").getString());
        int minLevel = doubleDropNode.getNode("minLevel").getInt();
        float scaleFactor = doubleDropNode.getNode("scaleFactor").getFloat();

        DoubleDropAbility doubleDropAbility = new DoubleDropAbility(AbilityIDs.GARDENING_DOUBLE_DROPS, AbilityPermissions.GARDENING_DOUBLE_DROPS, minLevel, scaleFactor, blockExperienceMap.keySet(), SkillIDs.GARDENING);
        doubleDropAbility.setAbilityName(abilityName);
        doubleDropAbility.setAbilityDescription(abilityDescription);

        gardeningSkill.addAbility(doubleDropAbility);

        return gardeningSkill;
    }

    @Override
    public void serialize(TypeToken<?> type, GardeningSkill obj, ConfigurationNode value) {
        throw new UnsupportedOperationException("Skills are not serialized to configs!");
    }

}
