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
import io.github.zerthick.mcskills.experience.formula.ExponentialFormula;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

public class ExponentialFormulaSerializer implements TypeSerializer<ExponentialFormula> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ExponentialFormula.class), new ExponentialFormulaSerializer());
    }

    @Override
    public ExponentialFormula deserialize(TypeToken<?> type, ConfigurationNode value) {

        float multiplier = value.getNode("multiplier").getFloat(1);
        float exponent = value.getNode("exponent").getFloat(1);
        float base = value.getNode("base").getFloat(0);

        return new ExponentialFormula(multiplier, exponent, base);
    }

    @Override
    public void serialize(TypeToken<?> type, ExponentialFormula obj, ConfigurationNode value) {
        throw new UnsupportedOperationException("Formulas are not serialized to configs!");
    }
}
