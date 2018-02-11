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

package io.github.zerthick.mcskills.skill;

import io.github.zerthick.mcskills.McSkills;
import io.github.zerthick.mcskills.api.skill.McSkillsSkill;
import io.github.zerthick.mcskills.api.skill.McSkillsSkillService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class McSkillsSkillServiceImpl implements McSkillsSkillService {

    private McSkills plugin;
    private Map<String, McSkillsSkill> skillMap;

    public McSkillsSkillServiceImpl(McSkills plugin) {
        this.plugin = plugin;
        skillMap = new HashMap<>();
    }

    @Override
    public void registerSkill(McSkillsSkill skill) {
        skill.registerListeners(plugin);
        skillMap.put(skill.getSkillID(), skill);
    }

    @Override
    public Optional<McSkillsSkill> getSkill(String skillID) {
        return Optional.ofNullable(skillMap.get(skillID));
    }

    @Override
    public Collection<McSkillsSkill> getRegisteredSkills() {
        return skillMap.values();
    }
}
