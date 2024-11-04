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

package io.dingodb.expr.parser;

import io.dingodb.expr.runtime.op.BinaryOp;
import io.dingodb.expr.runtime.op.NullaryOp;
import io.dingodb.expr.runtime.op.TertiaryOp;
import io.dingodb.expr.runtime.op.UnaryOp;
import io.dingodb.expr.runtime.op.VariadicOp;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * 函数工厂接口，根据参数获得函数对象.
 */
public interface FunFactory {
    /**
     * 根据名字获得一个无参函数.
     * Get a nullary function (Op) by its name.
     *
     * @param funName the name of the function  函数名。
     * @return the function (Op)    函数对象。
     */
    NullaryOp getNullaryFun(@NonNull String funName);

    /**
     * 注册一个无参函数，给定函数名字符串与函数名对应的操作对象.
     * Register a nullary function.
     *
     * @param funName the name of the function  函数名。
     * @param op      the corresponding {@link NullaryOp}   函数名字对应的操作。
     */
    void registerNullaryFun(@NonNull String funName, @NonNull NullaryOp op);

    /**
     * 获得一个一元函数.
     * Get a unary function (Op) by its name.
     *
     * @param funName the name of the function  函数名。
     * @return the function (Op)    函数对应的操作对象。
     */
    UnaryOp getUnaryFun(@NonNull String funName);

    /**
     * 注册一个一元函数.
     * Register a unary function.
     *
     * @param funName the name of the function  函数名。
     * @param op      the corresponding {@link UnaryOp}     函数名对应的操作对象。
     */
    void registerUnaryFun(@NonNull String funName, @NonNull UnaryOp op);

    /**
     * 获得一个二元函数对象.
     * Get a binary function (Op) by its name.
     *
     * @param funName the name of the function  函数名。
     * @return the function (Op)    函数对应的操作对象（也可以叫做函数对象）。
     */
    BinaryOp getBinaryFun(@NonNull String funName);

    /**
     * 注册一个二元函数.
     * Register a binary function.
     *
     * @param funName the name of the function  函数名。
     * @param op      the corresponding {@link BinaryOp}    函数对象。
     */
    void registerBinaryFun(@NonNull String funName, @NonNull BinaryOp op);

    /**
     * 给定函数名，获得一个三元函数.
     * Get a tertiary function (Op) by its name.
     *
     * @param funName the name of the function  函数名。
     * @return the function (Op)    函数对象。
     */
    TertiaryOp getTertiaryFun(@NonNull String funName);

    /**
     * 注册一个三元函数.
     * Register a tertiary function.
     *
     * @param funName the name of the function  函数名。
     * @param op      the corresponding {@link TertiaryOp}  函数对象（函数对应的操作对象）。
     */
    void registerTertiaryFun(@NonNull String funName, @NonNull TertiaryOp op);

    /**
     * 获得一个非定长参数函数对象.
     * Get a variadic function (Op) by its name.
     *
     * @param funName the name of the function  函数名。
     * @return the function (Op)        函数对象。
     */
    VariadicOp getVariadicFun(@NonNull String funName);

    /**
     * 注册一个非定长参数函数对象.
     * Register a variadic function.
     *
     * @param funName the name of the function  函数名。
     * @param op      the corresponding {@link VariadicOp}      函数对象。
     */
    void registerVariadicFun(@NonNull String funName, @NonNull VariadicOp op);
}
