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

package io.dingodb.expr.runtime.op.collection;

import io.dingodb.expr.common.type.CollectionType;
import io.dingodb.expr.runtime.op.UnaryOp;
import io.dingodb.expr.runtime.op.cast.CastOp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class CastCollectionOpFactory extends UnaryOp {
    private static final long serialVersionUID = 8767652546263331055L;

    protected final CastOp castOp;

    protected CastOp getCastOp(@NonNull CollectionType type) {
        return (CastOp) castOp.getOp(type.getElementType());
    }
}
