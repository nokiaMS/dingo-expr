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

package io.dingodb.expr.common.type;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class BoolType extends ScalarType {
    public static final String NAME = "BOOL";

    private static final int CODE = 3;

    BoolType() {
        super();
    }

    @Override
    public int numericPrecedence() {
        return 1;
    }

    @Override
    public <R, T> R accept(@NonNull TypeVisitor<R, T> visitor, T obj) {
        return visitor.visitBoolType(this, obj);
    }

    @Override
    public int hashCode() {
        return CODE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoolType;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
