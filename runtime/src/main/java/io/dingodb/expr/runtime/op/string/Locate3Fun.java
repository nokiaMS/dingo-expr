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

package io.dingodb.expr.runtime.op.string;

import io.dingodb.expr.annotations.Operators;
import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.runtime.op.OpKey;
import io.dingodb.expr.runtime.op.OpKeys;
import io.dingodb.expr.runtime.op.TertiaryOp;
import org.checkerframework.checker.nullness.qual.NonNull;

@Operators
abstract class Locate3Fun extends TertiaryOp {
    public static final String NAME = "LOCATE";

    private static final long serialVersionUID = 1338666346403324549L;

    static int locate(@NonNull String value0, @NonNull String value1, int pos) {
        return value1.indexOf(value0, pos - 1) + 1;
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }

    @Override
    public final OpKey keyOf(@NonNull Type type0, @NonNull Type type1, @NonNull Type type2) {
        return OpKeys.STRING_STRING_INT.keyOf(type0, type1, type2);
    }

    @Override
    public final OpKey bestKeyOf(@NonNull Type @NonNull [] types) {
        return OpKeys.STRING_STRING_INT.bestKeyOf(types);
    }
}
