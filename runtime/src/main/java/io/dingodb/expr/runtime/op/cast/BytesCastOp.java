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

import io.dingodb.expr.annotations.Operators;
import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.common.type.Types;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.charset.StandardCharsets;

//@Operators
abstract class BytesCastOp extends CastOp {
    private static final long serialVersionUID = -5563522687431694359L;

    static byte @NonNull [] bytesCast(@NonNull String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    static byte @NonNull [] bytesCast(byte @NonNull [] value) {
        return value;
    }

    static byte @Nullable [] bytesCast(Void ignoredValue) {
        return null;
    }

    @Override
    public final Type getType() {
        return Types.BYTES;
    }
}
