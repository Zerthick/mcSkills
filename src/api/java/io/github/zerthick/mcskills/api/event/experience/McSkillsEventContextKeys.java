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

package io.github.zerthick.mcskills.api.event.experience;

import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import org.spongepowered.api.event.cause.EventContextKey;

public class McSkillsEventContextKeys {

    public static final EventContextKey<String> MCSKILLS_SKILL_ID = EventContextKey.builder(String.class)
            .id("mcskills:skill_id")
            .name("mcSkills Skill ID")
            .build();

    public static final EventContextKey<McSkillsAccount> MCSKILLS_ACCOUNT = EventContextKey.builder(McSkillsAccount.class)
            .id("mcskills:account")
            .name("mcSkills Account")
            .build();

    private McSkillsEventContextKeys() {
    }
}
