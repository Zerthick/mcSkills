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

package io.github.zerthick.mcskills.api.account;

import java.util.UUID;

/**
 * Represents a service for retrieving {@link McSkillsAccount}s
 */
public interface McSkillsAccountService {

    /**
     * Gets the {@link McSkillsAccount} associated with the specified player's {@link UUID}.
     * If the account does not exist, a new account will be created associated with the specified {@link UUID}
     *
     * @param playerUniqueIdentifier the {@link UUID} of the player associated with the account
     * @return The {@link McSkillsAccount} associated with the specified {@link UUID}
     */
    McSkillsAccount getOrCreateAccount(UUID playerUniqueIdentifier);

    /**
     * Returns whether an {@link McSkillsAccount} associated with the specified player's {@link UUID} exists.
     * @param playerUniqueIdentifier the {@link UUID} of the player associated with the account
     * @return Whether an {@link McSkillsAccount} associated with the specified player's {@link UUID} exists
     */
    boolean hasAccount(UUID playerUniqueIdentifier);

}
