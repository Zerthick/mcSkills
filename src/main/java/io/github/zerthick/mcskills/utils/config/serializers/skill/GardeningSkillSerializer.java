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
import io.github.zerthick.mcskills.api.skill.ability.McSkillsAbility;
import io.github.zerthick.mcskills.skill.harvest.gardening.GardeningSkill;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GardeningSkillSerializer implements TypeSerializer<GardeningSkill> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GardeningSkill.class), new GardeningSkillSerializer());
    }

    @Override
    public GardeningSkill deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        Text skillName = TextSerializers.FORMATTING_CODE.deserialize(value.getNode("skillName").getString());
        Text skillDescription = TextSerializers.FORMATTING_CODE.deserialize(value.getNode("skillDescription").getString());
        Collection<McSkillsAbility> abilities = new HashSet<>(); //TODO: Read in abilities from config
        Map<BlockState, Integer> blockExperienceMap = new HashMap<>();

        Map<BlockType, Integer> blockTypeExperienceMap = value.getNode("experience", "blocktypes").getValue(new TypeToken<Map<BlockType, Integer>>() {
        });
        blockTypeExperienceMap.forEach((k, v) -> k.getAllBlockStates().forEach(blockState -> blockExperienceMap.put(blockState, v)));

        blockExperienceMap.putAll(value.getNode("experience", "blockstates").getValue(new TypeToken<Map<BlockState, Integer>>() {
        }, new HashMap<>()));

        return new GardeningSkill(skillName, skillDescription, abilities, blockExperienceMap);
    }

    @Override
    public void serialize(TypeToken<?> type, GardeningSkill obj, ConfigurationNode value) {
        throw new UnsupportedOperationException("Skills are not serialized to configs!");
    }

}
