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

package io.github.zerthick.mcskills.cmd.executors;

import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.McSkillsSkillService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandExecutor;

public abstract class AbstractCmdExecutor implements CommandExecutor {

    protected McSkillsAccountService getAccountService() {
        return Sponge.getServiceManager().provideUnchecked(McSkillsAccountService.class);
    }

    protected McSkillsSkillService getSkillService() {
        return Sponge.getServiceManager().provideUnchecked(McSkillsSkillService.class);
    }

    protected McSkillsExperienceService getExperienceService() {
        return Sponge.getServiceManager().provideUnchecked(McSkillsExperienceService.class);
    }
}
