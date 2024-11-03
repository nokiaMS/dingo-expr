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

import io.dingodb.expr.runtime.EvalContext;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.type.Type;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * 表示一个变量。
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = {"id", "type"})
public class Var implements Expr {
    /**
     * 用于Var变量的字符串表示中，Var的字符串表示以一个$开头。
     */
    public static final String WHOLE_VAR = "$";

    /**
     * 类版本号。
     */
    private static final long serialVersionUID = -7434384449038456900L;

    /**
     * 变量的id。
     */
    @Getter
    private final Object id;

    /**
     * 变量类型。
     */
    @Getter
    private final Type type;

    @Override
    public Object eval(EvalContext context, ExprConfig config) {
        return Objects.requireNonNull(context).get(id);
    }

    @Override
    public @NonNull Expr simplify(ExprConfig config) {
        return this;
    }

    /**
     * 调用访问者的visitVar函数。
     * @param visitor
     * @param obj
     * @return
     * @param <R>
     * @param <T>
     */
    @Override
    public <R, T> R accept(@NonNull ExprVisitor<R, T> visitor, T obj) {
        return visitor.visitVar(this, obj);
    }

    /**
     * 返回对象的字符串表示。
     * @return
     */
    @Override
    public @NonNull String toDebugString() {
        return getClass().getSimpleName() + "[" + getId() + ", " + type + "]";
    }

    /**
     * 设置变量值。
     * Set the value of this variable in a specified EvalContext.
     *
     * @param context the EvalContext
     * @param value   the new value
     */
    @SuppressWarnings("unused")
    public void set(EvalContext context, Object value) {
        Objects.requireNonNull(context).set(id, value);
    }

    /**
     * 返回对象的字符串表示。
     * @return
     */
    @Override
    public String toString() {
        if (id instanceof Integer || id instanceof Long) {
            return WHOLE_VAR + "[" + id + "]";
        }
        return id.toString();
    }
}
