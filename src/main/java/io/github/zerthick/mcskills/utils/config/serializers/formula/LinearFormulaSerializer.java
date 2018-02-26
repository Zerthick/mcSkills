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

package io.github.zerthick.mcskills.utils.config.serializers.formula;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.mcskills.experience.formula.LinearFormula;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

public class LinearFormulaSerializer implements TypeSerializer<LinearFormula> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(LinearFormula.class), new LinearFormulaSerializer());
    }

    @Override
    public LinearFormula deserialize(TypeToken<?> type, ConfigurationNode value) {
        float base = value.getNode("base").getFloat(0);
        float multiplier = value.getNode("multiplier").getFloat(1);
        return new LinearFormula(base, multiplier);
    }

    @Override
    public void serialize(TypeToken<?> type, LinearFormula obj, ConfigurationNode value) {
        throw new UnsupportedOperationException("Formulas are not serialized to configs!");
    }
}
