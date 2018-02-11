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

package io.github.zerthick.mcskills.experience;

import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.api.event.experience.McSkillsChangeExperienceEvent;
import io.github.zerthick.mcskills.api.event.experience.McSkillsChangeLevelEvent;
import io.github.zerthick.mcskills.api.event.experience.McSkillsEventContextKeys;
import io.github.zerthick.mcskills.api.experience.McSkillsExperienceService;
import io.github.zerthick.mcskills.api.experience.formula.McSkillsExperienceFormula;
import io.github.zerthick.mcskills.event.experience.McSkillsChangeExperienceEventGainImpl;
import io.github.zerthick.mcskills.event.experience.McSkillsChangeExperienceEventLoseImpl;
import io.github.zerthick.mcskills.event.experience.McSkillsChangeLevelEventUpImpl;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKeys;

public class McSkillsExperienceServiceImpl implements McSkillsExperienceService {

    private McSkillsExperienceFormula experienceFormula;

    @Override
    public void setExperienceFormula(McSkillsExperienceFormula experienceFormula) {
        this.experienceFormula = experienceFormula;
    }

    @Override
    public long getLevelExperience(int level) {
        return experienceFormula.getLevelExperience(level);
    }

    @Override
    public void addSkillExperience(Player player, String skillID, long experience) {
        postExperienceGainEvent(player, fetchPlayerAccount(player), skillID, experience);
    }

    @Override
    public void removeSkillExperience(Player player, String skillID, long experience) {
        postExperienceLoseEvent(player, fetchPlayerAccount(player), skillID, experience);
    }

    private McSkillsAccount fetchPlayerAccount(Player player) {
        McSkillsAccountService accountService = Sponge.getServiceManager().provideUnchecked(McSkillsAccountService.class);
        return accountService.getOrCreateAccount(player.getUniqueId());
    }

    private void postExperienceGainEvent(Player player, McSkillsAccount account, String skillID, long experience) {

        long currentExperience = account.getSkillExperience(skillID);
        int currentLevel = account.getSkillLevel(skillID);
        long currentLevelExperience = experienceFormula.getLevelExperience(currentLevel);

        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            Sponge.getCauseStackManager().addContext(EventContextKeys.PLAYER, player);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_ACCOUNT, account);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_SKILL_ID, skillID);

            McSkillsChangeExperienceEvent.Gain experienceGainEvent =
                    new McSkillsChangeExperienceEventGainImpl(player, experience,
                            Sponge.getCauseStackManager().getCurrentCause());
            Sponge.getEventManager().post(experienceGainEvent);

            if (!experienceGainEvent.isCancelled()) {

                long deltaExperience = currentExperience + experienceGainEvent.getExperience();
                if (deltaExperience < currentLevelExperience) {
                    account.setSkillExperience(skillID, deltaExperience);
                } else {
                    postLevelUpEvent(player, account, skillID, deltaExperience);
                }
            }

        }
    }

    private void postLevelUpEvent(Player player, McSkillsAccount account, String skillID, long deltaExperience) {

        int currentLevel = account.getSkillLevel(skillID);

        int targetLevel = currentLevel;
        long targetExperience = deltaExperience;

        while (targetExperience >= experienceFormula.getLevelExperience(targetLevel)) {
            targetExperience -= experienceFormula.getLevelExperience(targetLevel);
            targetLevel++;
        }

        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            Sponge.getCauseStackManager().addContext(EventContextKeys.PLAYER, player);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_ACCOUNT, account);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_SKILL_ID, skillID);

            McSkillsChangeLevelEvent.Up levelUpEvent = new McSkillsChangeLevelEventUpImpl(player, currentLevel,
                    targetLevel, targetExperience,
                    Sponge.getCauseStackManager().getCurrentCause());
            Sponge.getEventManager().post(levelUpEvent);

            if (!levelUpEvent.isCancelled()) {
                account.setSkillLevel(skillID, levelUpEvent.getLevel());
                account.setSkillExperience(skillID, levelUpEvent.getRemainingExperience());
            }
        }
    }

    private void postExperienceLoseEvent(Player player, McSkillsAccount account, String skillID, long experience) {

        long currentExperience = account.getSkillExperience(skillID);

        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            Sponge.getCauseStackManager().addContext(EventContextKeys.PLAYER, player);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_ACCOUNT, account);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_SKILL_ID, skillID);

            McSkillsChangeExperienceEvent.Lose experienceLoseEvent =
                    new McSkillsChangeExperienceEventLoseImpl(player, experience,
                            Sponge.getCauseStackManager().getCurrentCause());
            Sponge.getEventManager().post(experienceLoseEvent);

            if (!experienceLoseEvent.isCancelled()) {

                long deltaExperience = currentExperience - experienceLoseEvent.getExperience();
                if (deltaExperience >= 0) {
                    account.setSkillExperience(skillID, deltaExperience);
                } else {
                    postLevelDownEvent(player, account, skillID, deltaExperience);
                }
            }

        }
    }

    private void postLevelDownEvent(Player player, McSkillsAccount account, String skillID, long deltaExperience) {

        int currentLevel = account.getSkillLevel(skillID);

        int targetLevel = currentLevel;
        long targetExperience = deltaExperience;

        while (Math.abs(targetExperience) >= experienceFormula.getLevelExperience(targetLevel)) {
            targetExperience += experienceFormula.getLevelExperience(targetLevel);
            targetLevel--;
        }

        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            Sponge.getCauseStackManager().addContext(EventContextKeys.PLAYER, player);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_ACCOUNT, account);
            Sponge.getCauseStackManager().addContext(McSkillsEventContextKeys.MCSKILLS_SKILL_ID, skillID);

            McSkillsChangeLevelEvent.Up levelUpEvent = new McSkillsChangeLevelEventUpImpl(player, currentLevel,
                    targetLevel, experienceFormula.getLevelExperience(targetLevel) + targetExperience,
                    Sponge.getCauseStackManager().getCurrentCause());
            Sponge.getEventManager().post(levelUpEvent);

            if (!levelUpEvent.isCancelled()) {
                account.setSkillLevel(skillID, levelUpEvent.getLevel());
                account.setSkillExperience(skillID, levelUpEvent.getRemainingExperience());
            }
        }
    }
}
