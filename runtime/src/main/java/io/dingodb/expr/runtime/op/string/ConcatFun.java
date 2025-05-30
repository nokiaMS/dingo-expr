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

import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.op.BinaryOp;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ConcatFun extends BinaryOp {
    public static final String NAME = "CONCAT";
    public static final ConcatFun INSTANCE = new ConcatFun();

    private static final long serialVersionUID = 5454356467741754567L;

    @Override
    public Object evalValue(Object value0, Object value1, ExprConfig config) {
        if (value0 == null || value1 == null) {
            return null;
        }
        return String.valueOf(value0) + value1;
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }
}
