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

package io.dingodb.expr.runtime.op;

import io.dingodb.expr.common.type.Type;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class UnaryNumericOp extends UnaryOp {
    private static final long serialVersionUID = -336042269536007913L;

    @Override
    public OpKey bestKeyOf(@NonNull Type @NonNull [] types) {
        return OpKeys.DEFAULT.bestKeyOf(types);
    }
}
