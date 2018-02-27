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

import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.McSkillsSkillService;
import io.github.zerthick.mcskills.cmd.CmdPermissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreboardExecutor extends AbstractCmdExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<User> userOptional = args.getOne(CmdArgs.USER);

        if (userOptional.isPresent()) {
            if (src.hasPermission(CmdPermissions.SCOREBOARD_OTHER)) {
                buildPaginationList(userOptional.get()).sendTo(src);
            } else {
                throw new CommandException(Text.of("You don't have permission to view other user's scoreboards!"));
            }
        } else if (src instanceof Player) {
            buildPaginationList((Player) src).sendTo(src);
        } else {
            src.sendMessage(Text.of("You must specify a player as an argument from the console!"));
        }

        return CommandResult.success();
    }

    private PaginationList buildPaginationList(User user) {

        McSkillsAccount account = getAccountService().getOrCreateAccount(user.getUniqueId());
        McSkillsSkillService skillService = getSkillService();
        McSkillsExperienceService experienceService = getExperienceService();

        List<Text> contents = new ArrayList<>();

        skillService.getRegisteredSkills().forEach(mcSkillsSkill -> {
            if (user.hasPermission(mcSkillsSkill.getSkillPermission())) {
                int level = account.getSkillLevel(mcSkillsSkill.getSkillID());
                long levelExp = experienceService.getLevelExperience(level);
                long exp = account.getSkillExperience(mcSkillsSkill.getSkillID());

                contents.add(Text.of(mcSkillsSkill.getSkillName(), ": ", level));
                contents.add(Text.of(exp + "/" + levelExp));
            }
        });

        PaginationService pagServ = Sponge.getServiceManager().provideUnchecked(PaginationService.class);
        return pagServ.builder()
                .title(Text.of("Level | Experience"))
                .contents(contents)
                .build();
    }
}
