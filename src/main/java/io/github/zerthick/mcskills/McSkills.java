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
import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.McSkillsSkillService;
import io.github.zerthick.mcskills.experience.McSkillsExperienceServiceImpl;
import io.github.zerthick.mcskills.skill.McSkillsSkillServiceImpl;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.sql.SQLException;

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
    public void onGamePostInit(GamePostInitializationEvent event) {

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
        Sponge.getServiceManager().setProvider(this, McSkillsExperienceService.class, experienceService);

        // Register default Skill Service
        McSkillsSkillService skillService = new McSkillsSkillServiceImpl(this);
        Sponge.getServiceManager().setProvider(this, McSkillsSkillService.class, skillService);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Log Start Up to Console
        logger.info(
                instance.getName() + " version " + instance.getVersion().orElse("unknown")
                        + " enabled!");

    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {


        McSkillsAccountService accountService = Sponge.getServiceManager().provideUnchecked(McSkillsAccountService.class);

        McSkillsAccount mcSkillsAccount = accountService.getOrCreateAccount(player.getUniqueId());

        mcSkillsAccount.setSkillExperience("mcSkills:Test", 50);
        mcSkillsAccount.setSkillLevel("mcSkills:Test", 1);

        logger.info("Experience for Test skill of level " +
                mcSkillsAccount.getSkillLevel("mcSkills:Test") + " is " +
                mcSkillsAccount.getSkillExperience("mcSkills:Test"));
    }

}
