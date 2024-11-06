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

package io.dingodb.expr.rel;

import io.dingodb.expr.parser.ExprParser;
import io.dingodb.expr.runtime.ExprCompiler;
import io.dingodb.expr.runtime.TupleEvalContext;
import io.dingodb.expr.runtime.TupleEvalContextImpl;

/**
 * 关系配置接口.
 */
public interface RelConfig {
    /**
     * 默认实例.
     */
    RelConfig DEFAULT = new RelConfig() {
    };

    /**
     * 获得表达式翻译器.
     * @return   表达式翻译器.
     */
    default ExprParser getExprParser() {
        return ExprParser.DEFAULT;
    }

    /**
     * 获得表达式编译器.
     * @return   表达式编译器.
     */
    default ExprCompiler getExprCompiler() {
        return ExprCompiler.ADVANCED;
    }

    /**
     * Create new {@link TupleEvalContext} for a relational operator.
     *
     * @return the new {@link TupleEvalContext}
     */
    default TupleEvalContext getEvalContext() {
        return new TupleEvalContextImpl();
    }

    default CacheSupplier getCacheSupplier() {
        return CacheSupplierImpl.INSTANCE;
    }
}
