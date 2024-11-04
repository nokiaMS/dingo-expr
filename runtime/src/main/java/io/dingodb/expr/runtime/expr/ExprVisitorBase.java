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

package io.dingodb.expr.runtime.expr;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * 表达式访问者基类.
 * @param <R>  待补充。
 * @param <T>  待补充。
 */
public abstract class ExprVisitorBase<R, T> implements ExprVisitor<R, T> {
    /**
     * 表达式的visit函数.
     * @param expr  表达式对象。
     * @return  待补充。
     */
    public R visit(@NonNull Expr expr) {
        return expr.accept(this, null);
    }

    public R visit(@NonNull Expr expr, T obj) {
        return expr.accept(this, obj);
    }

    @Override
    public R visitVal(@NonNull Val expr, T obj) {
        return null;
    }

    @Override
    public R visitVar(@NonNull Var expr, T obj) {
        return null;
    }

    @Override
    public R visitNullaryOpExpr(@NonNull NullaryOpExpr expr, T obj) {
        return null;
    }

    @Override
    public R visitUnaryOpExpr(@NonNull UnaryOpExpr expr, T obj) {
        return null;
    }

    @Override
    public R visitBinaryOpExpr(@NonNull BinaryOpExpr expr, T obj) {
        return null;
    }

    @Override
    public R visitTertiaryOpExpr(@NonNull TertiaryOpExpr expr, T obj) {
        return null;
    }

    @Override
    public R visitVariadicOpExpr(@NonNull VariadicOpExpr expr, T obj) {
        return null;
    }

    @Override
    public R visitIndexOpExpr(@NonNull IndexOpExpr expr, T obj) {
        return visitBinaryOpExpr(expr, obj);
    }

    @Override
    public R visitNullaryAggExpr(@NonNull NullaryAggExpr expr, T obj) {
        return visitNullaryOpExpr(expr, obj);
    }

    @Override
    public R visitUnaryAggExpr(@NonNull UnaryAggExpr expr, T obj) {
        return visitUnaryOpExpr(expr, obj);
    }
}
