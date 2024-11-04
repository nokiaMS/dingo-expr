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

package io.dingodb.expr.runtime.type;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * 类型访问者基类，实现了TypeVisitor接口.
 * @param <R>  待补充。
 * @param <T>  待补充。
 */
public abstract class TypeVisitorBase<R, T> implements TypeVisitor<R, T> {
    public R visit(@NonNull Type type) {
        return type.accept(this, null);
    }

    public R visit(@NonNull Type type, T obj) {
        return type.accept(this, obj);
    }

    @Override
    public R visitNullType(@NonNull NullType type, T obj) {
        return null;
    }

    @Override
    public R visitIntType(@NonNull IntType type, T obj) {
        return null;
    }

    @Override
    public R visitLongType(@NonNull LongType type, T obj) {
        return null;
    }

    @Override
    public R visitFloatType(@NonNull FloatType type, T obj) {
        return null;
    }

    @Override
    public R visitDoubleType(@NonNull DoubleType type, T obj) {
        return null;
    }

    @Override
    public R visitBoolType(@NonNull BoolType type, T obj) {
        return null;
    }

    @Override
    public R visitDecimalType(@NonNull DecimalType type, T obj) {
        return null;
    }

    @Override
    public R visitStringType(@NonNull StringType type, T obj) {
        return null;
    }

    @Override
    public R visitBytesType(@NonNull BytesType type, T obj) {
        return null;
    }

    @Override
    public R visitDateType(@NonNull DateType type, T obj) {
        return null;
    }

    @Override
    public R visitTimeType(@NonNull TimeType type, T obj) {
        return null;
    }

    @Override
    public R visitTimestampType(@NonNull TimestampType type, T obj) {
        return null;
    }

    @Override
    public R visitAnyType(@NonNull AnyType type, T obj) {
        return null;
    }

    @Override
    public R visitArrayType(@NonNull ArrayType type, T obj) {
        return null;
    }

    @Override
    public R visitListType(@NonNull ListType type, T obj) {
        return null;
    }

    @Override
    public R visitMapType(@NonNull MapType type, T obj) {
        return null;
    }

    @Override
    public R visitTupleType(@NonNull TupleType type, T obj) {
        return null;
    }
}
