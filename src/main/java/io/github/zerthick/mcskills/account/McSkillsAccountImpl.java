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

package io.github.zerthick.mcskills.account;

import io.github.zerthick.mcskills.api.account.McSkillsAccount;

import java.util.Map;
import java.util.UUID;

public class McSkillsAccountImpl implements McSkillsAccount {

    UUID playerUUID;
    Map<String, McSkillsAccountEntry> skillMap;

    public McSkillsAccountImpl(UUID playerUUID, Map<String, McSkillsAccountEntry> skillMap) {
        this.playerUUID = playerUUID;
        this.skillMap = skillMap;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Map<String, McSkillsAccountEntry> getSkillMap() {
        return skillMap;
    }

    @Override
    public int getSkillLevel(String skillID) {
        McSkillsAccountEntry entry = skillMap.get(skillID);
        return entry != null ? entry.getLevel() : 0;
    }

    @Override
    public long getSkillExperience(String skillID) {
        McSkillsAccountEntry entry = skillMap.get(skillID);
        return entry != null ? entry.getExperience() : 0;
    }

    @Override
    public void setSkillLevel(String skillID, int level) {

        if (level < 0) {
            throw new IllegalArgumentException("Level must be non-negative!");
        }

        McSkillsAccountEntry entry = skillMap.getOrDefault(skillID, new McSkillsAccountEntry(0, 0));
        entry.setLevel(level);
        skillMap.put(skillID, entry);
    }

    @Override
    public void setSkillExperience(String skillID, long experience) {

        if (experience < 0) {
            throw new IllegalArgumentException("Experience must be non-negative!");
        }

        McSkillsAccountEntry entry = skillMap.getOrDefault(skillID, new McSkillsAccountEntry(0, 0));
        entry.setExperience(experience);
        skillMap.put(skillID, entry);
    }
}
