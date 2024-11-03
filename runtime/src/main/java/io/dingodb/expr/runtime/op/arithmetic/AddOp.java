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

package io.dingodb.expr.runtime.op.arithmetic;

import io.dingodb.expr.annotations.Operators;
import io.dingodb.expr.runtime.op.BinaryNumericOp;
import io.dingodb.expr.runtime.op.OpType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;

/**
 * 加法操作。
 */
@Operators
abstract class AddOp extends BinaryNumericOp {
    private static final long serialVersionUID = 7159909541314089027L;

    /**
     * int + int 操作。
     * @param value0
     * @param value1
     * @return
     */
    static int add(int value0, int value1) {
        return value0 + value1;
    }

    /**
     * long + long 操作。
     * @param value0
     * @param value1
     * @return
     */
    static long add(long value0, long value1) {
        return value0 + value1;
    }

    /**
     * float + float操作。
     * @param value0
     * @param value1
     * @return
     */
    static float add(float value0, float value1) {
        return value0 + value1;
    }

    /**
     * double + double操作。
     * @param value0
     * @param value1
     * @return
     */
    static double add(double value0, double value1) {
        return value0 + value1;
    }

    /**
     * bigdecimal + bigdecimal操作。
     * @param value0
     * @param value1
     * @return
     */
    static @NonNull BigDecimal add(@NonNull BigDecimal value0, @NonNull BigDecimal value1) {
        return value0.add(value1);
    }

    /**
     * string + string 操作。
     * @param s0
     * @param s1
     * @return
     */
    // This is not arithmetic op, but put here to share the same op factory.
    static @NonNull String add(@NonNull String s0, @NonNull String s1) {
        return s0 + s1;
    }

    /**
     * 返回此op的操作类型。
     * @return
     */
    @Override
    public @NonNull OpType getOpType() {
        return OpType.ADD;
    }
}
