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

package io.dingodb.expr.coding;

import io.dingodb.expr.rel.RelOpVisitorBase;
import io.dingodb.expr.rel.SourceOp;
import io.dingodb.expr.rel.TandemOp;
import io.dingodb.expr.rel.op.FilterOp;
import io.dingodb.expr.rel.op.GroupedAggregateOp;
import io.dingodb.expr.rel.op.ProjectOp;
import io.dingodb.expr.rel.op.UngroupedAggregateOp;
import io.dingodb.expr.runtime.expr.Expr;
import io.dingodb.expr.runtime.expr.Exprs;
import io.dingodb.expr.runtime.utils.CodecUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.OutputStream;
import java.util.List;

/**
 * 表操作编码器.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RelOpCoder extends RelOpVisitorBase<CodingFlag, @NonNull OutputStream> {
    /**
     * 默认实例.
     */
    public static final RelOpCoder INSTANCE = new RelOpCoder();

    /**
     * 关联的表达式编码器.
     */
    private static final ExprCoder EXPR_CODER = ExprCoder.INSTANCE;

    /**
     * 定义了几种表操作的编码.
     *      filter：过滤；
     *      project：投影；
     *      grouped_aggregate：分组聚合；
     *      ungrouped_aggregate：非分组聚合；
     */
    private static final byte FILTER = (byte) 0x71;                 //过滤
    private static final byte PROJECT = (byte) 0x72;                //投影
    private static final byte GROUPED_AGGREGATE = (byte) 0x73;      //分组聚合
    private static final byte UNGROUPED_AGGREGATE = (byte) 0x74;    //非分组聚合

    /**
     * 表达式结束标志.
     */
    // End of expression
    private static final byte EOE = (byte) 0x00;

    @SneakyThrows
    private static @Nullable CodingFlag visitAggList(@NonNull List<Expr> aggList, @NonNull OutputStream obj) {
        CodecUtils.encodeVarInt(obj, aggList.size());
        for (Expr expr : aggList) {
            if (EXPR_CODER.visit(expr, obj) != CodingFlag.OK) {
                return null;
            }
        }
        return CodingFlag.OK;
    }

    @Override
    public CodingFlag visitSourceOp(SourceOp op, @NonNull OutputStream obj) {
        return null;
    }

    /**
     * 过滤操作的编码函数.
     * @param op        过滤操作对象。
     * @param obj       输出流。
     * @return          成功返回OK，失败返回null。
     */
    @SneakyThrows
    @Override
    public CodingFlag visitFilterOp(@NonNull FilterOp op, @NonNull OutputStream obj) {
        obj.write(FILTER);
        if (EXPR_CODER.visit(op.getFilter(), obj) == CodingFlag.OK) {
            obj.write(EOE);
            return CodingFlag.OK;
        }
        return null;
    }

    /**
     * 投影操作的编码函数。（投影：从表的查询结果中选择相应的列。）.
     * @param op  待补充。
     * @param obj  待补充。
     * @return  编码成功返回OK，否则返回null。
     */
    @SneakyThrows
    @Override
    public CodingFlag visitProjectOp(@NonNull ProjectOp op, @NonNull OutputStream obj) {
        //写入投影操作编码。
        obj.write(PROJECT);
        //对投影列逐一进行编码。
        for (Expr expr : op.getProjects()) {
            if (EXPR_CODER.visit(expr, obj) != CodingFlag.OK) {
                return null;
            }
        }
        //添加编码结束标记。
        obj.write(EOE);
        //返回编码成功。
        return CodingFlag.OK;
    }

    @Override
    public CodingFlag visitTandemOp(@NonNull TandemOp op, @NonNull OutputStream obj) {
        if (visit(op.getInput(), obj) == CodingFlag.OK) {
            return visit(op.getOutput(), obj);
        }
        return null;
    }

    @SneakyThrows
    @Override
    public CodingFlag visitUngroupedAggregateOp(@NonNull UngroupedAggregateOp op, @NonNull OutputStream obj) {
        obj.write(UNGROUPED_AGGREGATE);
        return visitAggList(op.getAggList(), obj);
    }

    @SneakyThrows
    @Override
    public CodingFlag visitGroupedAggregateOp(@NonNull GroupedAggregateOp op, @NonNull OutputStream obj) {
        obj.write(GROUPED_AGGREGATE);
        ExprCoder.INSTANCE.visit(Exprs.val(op.getGroupIndices()), obj);
        return visitAggList(op.getAggList(), obj);
    }
}
