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

package io.dingodb.expr.runtime.exception;

import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.runtime.op.Op;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class OperatorTypeNotExist extends ExprCompileException {
    private static final long serialVersionUID = 1929487647979919245L;

    public OperatorTypeNotExist(@NonNull Op op, Type... types) {
        super("Operator " + op.getName() + " not found for types " + Arrays.toString(types) + ".");
    }
}
