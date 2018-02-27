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

package io.github.zerthick.mcskills.cmd;

import io.github.zerthick.mcskills.McSkills;
import io.github.zerthick.mcskills.cmd.executors.CmdArgs;
import io.github.zerthick.mcskills.cmd.executors.ScoreboardExecutor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CmdRegister {

    public static void registerCommands(McSkills plugin) {

        CommandSpec scoreboardCommand = CommandSpec
                .builder()
                .description(Text.of("Display you current level"))
                .permission("mcskills.commands.scoreboard")
                .arguments(GenericArguments.optional(GenericArguments.user(CmdArgs.USER)))
                .executor(new ScoreboardExecutor())
                .build();

        Sponge.getCommandManager().register(plugin, scoreboardCommand, "mcsb");

    }

}
