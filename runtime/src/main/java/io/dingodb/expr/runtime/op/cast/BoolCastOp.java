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

package io.dingodb.expr.runtime.op.cast;

import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.common.type.Types;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;

//@Operators
abstract class BoolCastOp extends CastOp {
    private static final long serialVersionUID = 2195457099624343481L;

    static boolean boolCast(int value) {
        return value != 0;
    }

    static boolean boolCast(long value) {
        return value != 0;
    }

    static boolean boolCast(float value) {
        return value != 0.0f;
    }

    static boolean boolCast(double value) {
        return value != 0.0;
    }

    static boolean boolCast(boolean value) {
        return value;
    }

    static boolean boolCast(@NonNull BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) != 0;
    }

    static boolean boolCast(byte[] value) {
        if (value == null) {
            return false;
        } else {
            if (value.length == 1) {
                return value[0] != 0x00;
            }
            try {
                String val = new String(value);
                if ("".equalsIgnoreCase(val)) {
                    return false;
                } else {
                    return !"0".equalsIgnoreCase(val);
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    static @Nullable Boolean boolCast(Void ignoredValue) {
        return null;
    }

    @Override
    public final Type getType() {
        return Types.BOOL;
    }
}
