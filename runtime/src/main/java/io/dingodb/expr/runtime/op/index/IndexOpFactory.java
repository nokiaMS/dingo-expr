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

package io.dingodb.expr.runtime.op.index;

import io.dingodb.expr.common.type.ArrayType;
import io.dingodb.expr.common.type.CollectionType;
import io.dingodb.expr.common.type.ListType;
import io.dingodb.expr.common.type.MapType;
import io.dingodb.expr.common.type.TupleType;
import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.common.type.Types;
import io.dingodb.expr.runtime.expr.Expr;
import io.dingodb.expr.runtime.expr.IndexOpExpr;
import io.dingodb.expr.runtime.op.BinaryOp;
import io.dingodb.expr.runtime.op.OpKey;
import io.dingodb.expr.runtime.op.OpType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexOpFactory extends BinaryOp {
    public static final IndexOpFactory INSTANCE = new IndexOpFactory();

    private static final long serialVersionUID = 4166202157298784594L;

    @Override
    public IndexOpFactory getOp(OpKey key) {
        if (key != null) {
            if (key instanceof ArrayType) {
                return IndexArrayOp.of((ArrayType) key);
            } else if (key instanceof ListType) {
                return IndexListOp.of((ListType) key);
            } else if (key instanceof MapType) {
                return IndexMapOp.of((MapType) key);
            } else if (key instanceof TupleType) {
                return IndexTupleOp.of((TupleType) key);
            }
        }
        return null;
    }

    @Override
    public IndexOpExpr createExpr(@NonNull Expr operand0, @NonNull Expr operand1) {
        return new IndexOpExpr(this, operand0, operand1);
    }

    @Override
    public final @NonNull OpType getOpType() {
        return OpType.INDEX;
    }

    @Override
    public OpKey keyOf(@NonNull Type type0, @NonNull Type type1) {
        if ((type0 instanceof CollectionType || type0 instanceof TupleType) && type1.equals(Types.INT)) {
            return type0;
        } else if (type0 instanceof MapType && type1 == ((MapType) type0).getKeyType()) {
            return type0;
        }
        return null;
    }

    @Override
    public OpKey bestKeyOf(@NonNull Type @NonNull [] types) {
        if (types[0] instanceof CollectionType || types[0] instanceof TupleType) {
            types[1] = Types.INT;
            return types[0];
        } else if (types[0] instanceof MapType) {
            types[1] = ((MapType) types[0]).getKeyType();
            return types[0];
        }
        return null;
    }
}
