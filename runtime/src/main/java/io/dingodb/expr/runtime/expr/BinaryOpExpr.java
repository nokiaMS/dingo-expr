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
import io.dingodb.expr.runtime.op.BinaryOp;
import io.dingodb.expr.runtime.op.OpSymbol;
import io.dingodb.expr.runtime.op.OpType;
import io.dingodb.expr.runtime.type.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * 二元操作表达式。
 */
@EqualsAndHashCode(of = {"operand0", "operand1"}, callSuper = true)
public class BinaryOpExpr extends OpExpr<BinaryOp, BinaryOpExpr> {
    private static final long serialVersionUID = -4980001325586095328L;

    @Getter
    protected final Expr operand0;
    @Getter
    protected final Expr operand1;

    public BinaryOpExpr(BinaryOp op, Expr operand0, Expr operand1) {
        super(op);
        this.operand0 = operand0;
        this.operand1 = operand1;
    }

    @Override
    public Object eval(EvalContext context, ExprConfig config) {
        return op.eval(operand0, operand1, context, config);
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
        return visitor.visitBinaryOpExpr(this, obj);
    }

    @Override
    public @NonNull String toDebugString() {
        return op.getClass().getSimpleName()
               + "[" + op.getName() + "]"
               + "(" + operand0.toDebugString()
               + ", " + operand1.toDebugString() + ")";
    }

    @Override
    public String toString() {
        OpType opType = op.getOpType();
        if (opType.getSymbol().equals(OpSymbol.FUN)) {
            return op.getName() + "(" + operand0 + ", " + operand1 + ")";
        }
        return oprandToString(operand0) + opType.getSymbol() + oprandToString(operand1);
    }
}
