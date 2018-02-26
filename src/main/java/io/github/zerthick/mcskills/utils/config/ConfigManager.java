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

package io.github.zerthick.mcskills.utils.config;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.mcskills.api.experience.formula.McSkillsExperienceFormula;
import io.github.zerthick.mcskills.api.skill.McSkillsSkill;
import io.github.zerthick.mcskills.experience.formula.ExponentialFormula;
import io.github.zerthick.mcskills.experience.formula.LinearFormula;
import io.github.zerthick.mcskills.skill.harvest.mining.MiningSkill;
import io.github.zerthick.mcskills.utils.config.serializers.formula.ExponentialFormulaSerializer;
import io.github.zerthick.mcskills.utils.config.serializers.formula.LinearFormulaSerializer;
import io.github.zerthick.mcskills.utils.config.serializers.skill.GardeningSkillSerializer;
import io.github.zerthick.mcskills.utils.config.serializers.skill.MiningSkillSerializer;
import io.github.zerthick.mcskills.utils.config.serializers.skill.WoodcuttingSkillSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ConfigManager {

    public static void registerSerializers() {

        ExponentialFormulaSerializer.register();
        LinearFormulaSerializer.register();

        MiningSkillSerializer.register();
        GardeningSkillSerializer.register();
        WoodcuttingSkillSerializer.register();
    }

    public static Optional<McSkillsExperienceFormula> loadDefaultFormula(PluginContainer instance, Path configDir, Logger logger) throws IOException, ObjectMappingException {

        String formulaConfig = "experienceFormulas.conf";

        Path formulaConfigPath = configDir.resolve(formulaConfig);

        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(formulaConfigPath).build();

        if (!Files.isRegularFile(formulaConfigPath)) {
            Optional<Asset> defaultConfig = instance.getAsset(formulaConfig);

            if (defaultConfig.isPresent()) {
                defaultConfig.get().copyToFile(formulaConfigPath);
            }
        }

        CommentedConfigurationNode node = configLoader.load();

        String formulaType = node.getNode("type").getString();

        if (formulaType.equals("LINEAR")) {
            return Optional.of(node.getNode("linear").getValue(TypeToken.of(LinearFormula.class)));
        }

        if (formulaType.equals("EXPONENTIAL")) {
            return Optional.of(node.getNode("exponential").getValue(TypeToken.of(ExponentialFormula.class)));
        }

        return Optional.empty();
    }

    public static Collection<McSkillsSkill> loadSkills(PluginContainer instance, Path configDir, Logger logger) throws IOException, ObjectMappingException {

        Collection<McSkillsSkill> skills = new ArrayList<>();

        loadSkill(instance, "mining.conf", TypeToken.of(MiningSkill.class), configDir)
                    .ifPresent(skills::add);
        loadSkill(instance, "gardening.conf", TypeToken.of(MiningSkill.class), configDir)
                    .ifPresent(skills::add);
        loadSkill(instance, "woodcutting.conf", TypeToken.of(MiningSkill.class), configDir)
                    .ifPresent(skills::add);

        return skills;
    }

    private static Optional<McSkillsSkill> loadSkill(PluginContainer instance, String skillConfig, TypeToken<? extends McSkillsSkill> typeToken, Path configDir) throws IOException, ObjectMappingException {

        Path skillConfigPath = configDir.resolve("skills/" + skillConfig);

        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(skillConfigPath).build();

        if (!Files.isRegularFile(skillConfigPath)) {
            Optional<Asset> defaultConfig = instance.getAsset(skillConfig);

            if (defaultConfig.isPresent()) {
                Path parent = skillConfigPath.getParent();

                if (!Files.isDirectory(parent)) {
                    Files.createDirectory(parent);
                }
                defaultConfig.get().copyToFile(skillConfigPath);
            }
        }

        CommentedConfigurationNode node = configLoader.load();
        if (node.getNode("enabled").getBoolean(true)) {
            return Optional.of(node.getValue(typeToken));
        }

        return Optional.empty();
    }

}
