/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.expr.runtime.op.time;

import io.dingodb.expr.annotations.Operators;
import io.dingodb.expr.runtime.type.Type;
import io.dingodb.expr.runtime.type.Types;
import io.dingodb.expr.runtime.utils.DateTimeUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Time;

@Operators
abstract class TimeFormat2Fun extends BinaryFormatFun {
    public static final String NAME = "TIME_FORMAT";

    private static final long serialVersionUID = 6159797220072404086L;

    static @NonNull String timeFormat(Time value, String format) {
        return DateTimeUtils.timeFormat(value, format);
    }

    @Override
    public Object keyOf(@NonNull Type type0, @NonNull Type type1) {
        if (type0.equals(Types.TIME) && type1.equals(Types.STRING)) {
            return Types.TIME;
        }
        return null;
    }

    @Override
    public Object bestKeyOf(@NonNull Type @NonNull [] types) {
        if (types[1].equals(Types.STRING)) {
            types[0] = Types.TIME;
            return Types.TIME;
        }
        return null;
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }
}