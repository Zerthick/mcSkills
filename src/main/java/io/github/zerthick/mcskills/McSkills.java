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

package io.github.zerthick.mcskills;

import com.google.inject.Inject;
import io.github.zerthick.mcskills.account.McSkillsAccountServiceImpl;
import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.api.event.experience.McSkillsChangeExperienceEvent;
import io.github.zerthick.mcskills.api.event.experience.McSkillsEventContextKeys;
import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.McSkillsSkill;
import io.github.zerthick.mcskills.api.skill.McSkillsSkillService;
import io.github.zerthick.mcskills.experience.McSkillsExperienceServiceImpl;
import io.github.zerthick.mcskills.skill.McSkillsSkillServiceImpl;
import io.github.zerthick.mcskills.utils.config.ConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collection;

@Plugin(
        id = "mcskills",
        name = "McSkills",
        description = "A Minecraft RPG plugin",
        authors = {
                "Zerthick",
                "Brian_Elliott"
        }
)
public class McSkills {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfigDir;

    @Inject
    private PluginContainer instance;

    public Path getDefaultConfigDir() {
        return defaultConfigDir;
    }
    public Path getDefaultConfig() {
        return defaultConfig;
    }
    public PluginContainer getInstance() {
        return instance;
    }
    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {

        // Register Config Serializers
        ConfigManager.registerSerializers();

        // Register default Account Service
        try {
            McSkillsAccountService accountService = new McSkillsAccountServiceImpl(this);
            Sponge.getServiceManager().setProvider(this, McSkillsAccountService.class, accountService);
            Sponge.getEventManager().registerListeners(this, accountService);
        } catch (SQLException e) {
            logger.error("Error registering default account service! Error: " + e.getMessage());
        }

        // Register default Experience Service
        McSkillsExperienceService experienceService = new McSkillsExperienceServiceImpl();
        try {
            ConfigManager.loadDefaultFormula(instance, defaultConfigDir, logger)
                    .ifPresent(experienceService::setExperienceFormula);
        } catch (IOException | ObjectMappingException e) {
            logger.error("Error loading experience formula! Error: " + e.getMessage());
        }
        Sponge.getServiceManager().setProvider(this, McSkillsExperienceService.class, experienceService);

        // Register default Skill Service
        McSkillsSkillService skillService = new McSkillsSkillServiceImpl(this);
        Sponge.getServiceManager().setProvider(this, McSkillsSkillService.class, skillService);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Register skills
        try {
            Collection<McSkillsSkill> skills = ConfigManager.loadSkills(instance, defaultConfigDir, logger);
            McSkillsSkillService skillService = Sponge.getServiceManager().provideUnchecked(McSkillsSkillService.class);
            skills.forEach(skillService::registerSkill);
        } catch (IOException | ObjectMappingException e) {
            logger.error("Error loading skill configs! Error: " + e.getMessage());
        }


        // Log Start Up to Console
        logger.info(
                instance.getName() + " version " + instance.getVersion().orElse("unknown")
                        + " enabled!");

    }

    @Listener
    public void onPlayerGainExp(McSkillsChangeExperienceEvent.Gain event) {
        logger.info(event.getCause().toString());
        Player player = event.getTargetEntity();
        String skillID = event.getContext().get(McSkillsEventContextKeys.MCSKILLS_SKILL_ID).get();
        player.sendMessage(Text.of("You gained " + event.getExperience() + " experience in " + skillID));
    }

}
