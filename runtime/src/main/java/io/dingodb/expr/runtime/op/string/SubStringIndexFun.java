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
abstract class SubStringIndexFun extends TertiaryOp {
    public static final String NAME = "SUBSTRING_INDEX";
    private static final long serialVersionUID = 2403329900389855640L;

    static String substringIndex(String value0, String value1, int count) {
        if (count == 0) {
            return "";
        }
        if (count > 0) {
            int index = 0;
            for (int i = 0; i < count; i++) {
                index = value0.indexOf(value1, index);
                if (index == -1) {
                    return value0;
                }
                index += value1.length();
            }
            return value0.substring(0, index - value1.length());
        } else {
            int index = value0.length();
            for (int i = 0; i < -count; i++) {
                index = value0.lastIndexOf(value1, index - 1);
                if (index == -1) {
                    return value0;
                }
            }
            return value0.substring(index + value1.length());
        }
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }

    @Override
    public OpKey keyOf(@NonNull Type type0, @NonNull Type type1, @NonNull Type type2) {
        return OpKeys.STRING_STRING_INT.keyOf(type0, type1, type2);
    }

    @Override
    public OpKey bestKeyOf(@NonNull Type @NonNull [] types) {
        return OpKeys.STRING_STRING_INT.bestKeyOf(types);
    }
}
