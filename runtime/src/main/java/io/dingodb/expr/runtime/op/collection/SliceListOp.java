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

import io.dingodb.expr.common.type.ArrayType;
import io.dingodb.expr.common.type.ListType;
import io.dingodb.expr.common.type.TupleType;
import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.common.type.Types;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.utils.ExceptionUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

abstract class SliceListOp extends SliceOp {
    private static final long serialVersionUID = -1472675231395486262L;

    protected SliceListOp(@NonNull ListType originalType, ListType type) {
        super(originalType, type);
    }

    public static @Nullable SliceListOp of(@NonNull ListType type) {
        Type elementType = type.getElementType();
        if (elementType instanceof ArrayType) {
            return new SliceListOfArrayOp(type);
        } else if (elementType instanceof ListType) {
            return new SliceListOfListOp(type);
        } else if (elementType instanceof TupleType) {
            return new SliceListOfTupleOp(type, Types.LIST_ANY);
        }
        return null;
    }

    @Override
    protected @NonNull Object evalNonNullValue(@NonNull Object value0, @NonNull Object value1, ExprConfig config) {
        int index = (Integer) value1;
        List<?> list = (List<?>) value0;
        int size = list.size();
        List<Object> result = new ArrayList<>(size);
        for (Object element : list) {
            result.add(ExceptionUtils.nonNullElement(getValueOf(element, index)));
        }
        return result;
    }
}
