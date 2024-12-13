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

package io.dingodb.expr.runtime.op.aggregation;

import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.common.type.Types;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.op.OpType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CountAllAgg extends NullaryAgg {
    public static final String NAME = "COUNT";

    public static final CountAllAgg INSTANCE = new CountAllAgg();

    private static final long serialVersionUID = 4984223618404125180L;

    @Override
    public Type getType() {
        return Types.LONG;
    }

    @Override
    public @NonNull OpType getOpType() {
        return OpType.AGG;
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }

    @Override
    public @NonNull Object first(ExprConfig config) {
        return 1L;
    }

    @Override
    public @NonNull Long add(Object var, ExprConfig config) {
        return (long) var + 1L;
    }

    @Override
    public @NonNull Long merge(@NonNull Object var1, @NonNull Object var2, ExprConfig config) {
        return (long) var1 + (long) var2;
    }

    @Override
    public @NonNull Long emptyValue() {
        return 0L;
    }
}
