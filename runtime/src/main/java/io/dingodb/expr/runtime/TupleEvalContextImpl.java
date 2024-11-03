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

package io.dingodb.expr.runtime;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * 行计算上下文实现类。
 */
public class TupleEvalContextImpl implements TupleEvalContext {
    /**
     * 序列化版本号。
     */
    private static final long serialVersionUID = -1735756800219588237L;

    /**
     * 线程的局部变量，每个线程中ThreadLocal变量都是一个独立的副本，各个线程互不影响。
     */
    private final ThreadLocal<Object[]> threadLocalTuple = new ThreadLocal<>();

    /**
     * 默认构造函数。
     * Create a {@link TupleEvalContextImpl}.
     */
    public TupleEvalContextImpl() {
    }

    /**
     * tuple的字符串表示。
     * @return
     */
    @Override
    public String toString() {
        Object[] tuple = threadLocalTuple.get();
        if (tuple == null) {
            return "(null)";
        }
        if (tuple.length == 0) {
            return "(empty)";
        }
        if (tuple.length == 1) {
            return tuple[0].toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tuple.length; i++) {
            sb.append(String.format("%03d", i));
            sb.append(": ");
            sb.append(tuple[i]);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 返回id对应的值。
     * @param id the id of the variable
     * @return
     */
    @Override
    public Object get(Object id) {
        return threadLocalTuple.get()[(int) id];
    }

    /**
     * 设置id对应的值。
     * @param id    the id of the variable
     * @param value the new value of the variable
     */
    @Override
    public void set(Object id, Object value) {
        threadLocalTuple.get()[(int) id] = value;
    }

    /**
     * 设置tuple值。
     * @param tuple
     */
    @Override
    public void setTuple(Object @NonNull [] tuple) {
        threadLocalTuple.set(tuple);
    }
}
