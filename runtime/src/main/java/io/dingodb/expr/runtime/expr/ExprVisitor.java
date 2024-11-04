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
 * 表达式访问者接口.
 * @param <R>  待补充。
 * @param <T>  待补充。
 */
public interface ExprVisitor<R, T> {
    /**
     * 变量访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitVal(@NonNull Val expr, T obj);

    /**
     * 变量访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitVar(@NonNull Var expr, T obj);

    /**
     * 空元访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitNullaryOpExpr(@NonNull NullaryOpExpr expr, T obj);

    /**
     * 一元操作表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitUnaryOpExpr(@NonNull UnaryOpExpr expr, T obj);

    /**
     * 二元操作表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitBinaryOpExpr(@NonNull BinaryOpExpr expr, T obj);

    /**
     * tertiary操作表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitTertiaryOpExpr(@NonNull TertiaryOpExpr expr, T obj);

    /**
     * 可变参数操作表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitVariadicOpExpr(@NonNull VariadicOpExpr expr, T obj);

    /**
     * 索引操作表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitIndexOpExpr(@NonNull IndexOpExpr expr, T obj);

    /**
     * 空元聚合操作表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitNullaryAggExpr(@NonNull NullaryAggExpr expr, T obj);

    /**
     * 一元聚合表达式访问接口.
     * @param expr  待补充。
     * @param obj  待补充。
     * @return  待补充。
     */
    R visitUnaryAggExpr(@NonNull UnaryAggExpr expr, T obj);
}
