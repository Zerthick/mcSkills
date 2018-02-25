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

package io.github.zerthick.mcskills.api.skill;

import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.skill.ability.McSkillsAbility;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Collection;

public abstract class AbstractMcSkillsSkill implements McSkillsSkill {

    protected final String skillID;
    protected final String skillPermission;
    protected final Text skillName;
    protected final Text skillDescription;

    protected Collection<McSkillsAbility> abilities;

    public AbstractMcSkillsSkill(String skillID, String skillPermission, Text skillName, Text skillDescription, Collection<McSkillsAbility> abilities) {
        this.skillID = skillID;
        this.skillPermission = skillPermission;
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.abilities = abilities;
    }

    @Override
    public String getSkillID() {
        return skillID;
    }

    @Override
    public String getSkillPermission() {
        return skillPermission;
    }

    @Override
    public Text getSkillName() {
        return skillName;
    }

    @Override
    public Text getSkillDescription() {
        return skillDescription;
    }

    @Override
    public Collection<McSkillsAbility> getAbilities() {
        return abilities;
    }

    @Override
    public void registerListeners(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
        abilities.forEach(a -> a.registerListeners(plugin));
    }

    protected McSkillsExperienceService getExperienceService() {
        return Sponge.getServiceManager().provideUnchecked(McSkillsExperienceService.class);
    }
}
