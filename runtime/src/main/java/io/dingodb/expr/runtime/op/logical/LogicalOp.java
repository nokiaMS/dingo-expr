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

package io.dingodb.expr.runtime.op.logical;

import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.common.type.Types;
import io.dingodb.expr.runtime.op.BinaryOp;
import io.dingodb.expr.runtime.op.OpKey;
import io.dingodb.expr.runtime.op.OpKeys;
import org.checkerframework.checker.nullness.qual.NonNull;

abstract class LogicalOp extends BinaryOp {
    private static final long serialVersionUID = -5778606762979793469L;

    @Override
    public OpKey bestKeyOf(@NonNull Type @NonNull [] types) {
        return OpKeys.ALL_BOOL.bestKeyOf(types);
    }

    @Override
    public BinaryOp getOp(OpKey key) {
        return (key != null && key.equals(Types.BOOL)) ? this : null;
    }

    @Override
    public final Type getType() {
        return Types.BOOL;
    }
}
