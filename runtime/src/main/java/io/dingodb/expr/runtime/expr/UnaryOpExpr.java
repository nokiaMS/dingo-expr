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

import io.dingodb.expr.common.type.Type;
import io.dingodb.expr.runtime.EvalContext;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.op.OpSymbol;
import io.dingodb.expr.runtime.op.OpType;
import io.dingodb.expr.runtime.op.UnaryOp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

@EqualsAndHashCode(of = {"operand"}, callSuper = true)
public class UnaryOpExpr extends OpExpr<UnaryOp, UnaryOpExpr> {
    private static final long serialVersionUID = 7964987353969166202L;

    @Getter
    protected final Expr operand;

    public UnaryOpExpr(UnaryOp op, Expr operand) {
        super(op);
        this.operand = operand;
    }

    @Override
    public Object eval(EvalContext context, ExprConfig config) {
        return op.eval(operand, context, config);
    }

    @Override
    public Type getType() {
        return op.getType();
    }

    @Override
    public @NonNull Expr simplify(ExprConfig config) {
        if (op.isConst(this)) {
            return Exprs.val(eval(null, config), getType());
        }
        return op.simplify(this, config);
    }

    @Override
    public <R, T> R accept(@NonNull ExprVisitor<R, T> visitor, T obj) {
        return visitor.visitUnaryOpExpr(this, obj);
    }

    @Override
    public @NonNull String toDebugString() {
        return op.getClass().getSimpleName()
               + "[" + op.getName() + "]"
               + "(" + operand.toDebugString() + ")";
    }

    @Override
    public String toString() {
        OpType opType = op.getOpType();
        if (opType.getSymbol().equals(OpSymbol.FUN)) {
            return op.getName() + "(" + operand + ")";
        }
        return opType.getSymbol() + oprandToString(operand);
    }
}
