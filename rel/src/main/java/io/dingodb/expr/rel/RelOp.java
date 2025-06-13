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

package io.dingodb.expr.rel;

import io.dingodb.expr.common.type.TupleType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serializable;

public interface RelOp extends Serializable {
    /**
     * Return a new {@link RelOp} with expression compiled, and cache initialized (for {@link CacheOp}).
     *
     * @param context the compile context
     * @param config  the config
     * @return the new {@link RelOp}
     */
    @NonNull RelOp compile(@NonNull TupleCompileContext context, @NonNull RelConfig config);

    TupleType getType();

    <R, T> R accept(@NonNull RelOpVisitor<R, T> visitor, T obj);
}
