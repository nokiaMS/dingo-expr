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

import io.dingodb.expr.common.type.BoolType;
import io.dingodb.expr.common.type.BytesType;
import io.dingodb.expr.common.type.DateType;
import io.dingodb.expr.common.type.DecimalType;
import io.dingodb.expr.common.type.DoubleType;
import io.dingodb.expr.common.type.FloatType;
import io.dingodb.expr.common.type.IntType;
import io.dingodb.expr.common.type.LongType;
import io.dingodb.expr.common.type.StringType;
import io.dingodb.expr.common.type.TimeType;
import io.dingodb.expr.common.type.TimestampType;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.expr.Exprs;
import io.dingodb.expr.runtime.op.BinaryOp;
import io.dingodb.expr.runtime.op.NullaryOp;
import io.dingodb.expr.runtime.op.TertiaryOp;
import io.dingodb.expr.runtime.op.UnaryOp;
import io.dingodb.expr.runtime.op.VariadicOp;
import io.dingodb.expr.runtime.op.aggregation.CountAgg;
import io.dingodb.expr.runtime.op.aggregation.CountAllAgg;
import io.dingodb.expr.runtime.op.aggregation.MaxAgg;
import io.dingodb.expr.runtime.op.aggregation.MinAgg;
import io.dingodb.expr.runtime.op.aggregation.SingleValueAgg;
import io.dingodb.expr.runtime.op.aggregation.Sum0Agg;
import io.dingodb.expr.runtime.op.aggregation.SumAgg;
import io.dingodb.expr.runtime.op.collection.ArrayConstructorOpFactory;
import io.dingodb.expr.runtime.op.collection.ListConstructorOpFactory;
import io.dingodb.expr.runtime.op.collection.MapConstructorOpFactory;
import io.dingodb.expr.runtime.op.collection.SliceOpFactory;
import io.dingodb.expr.runtime.op.date.DayFunFactory;
import io.dingodb.expr.runtime.op.date.DayHourFunFactory;
import io.dingodb.expr.runtime.op.date.DayMinuteFunFactory;
import io.dingodb.expr.runtime.op.date.DaySecondFunFactory;
import io.dingodb.expr.runtime.op.date.HourFunFactory;
import io.dingodb.expr.runtime.op.date.HourMinuteFunFactory;
import io.dingodb.expr.runtime.op.date.HourSecondFunFactory;
import io.dingodb.expr.runtime.op.date.MillisecondFunFactory;
import io.dingodb.expr.runtime.op.date.MinuteFunFactory;
import io.dingodb.expr.runtime.op.date.MinuteSecondFunFactory;
import io.dingodb.expr.runtime.op.date.MonthFunFactory;
import io.dingodb.expr.runtime.op.date.SecondFunFactory;
import io.dingodb.expr.runtime.op.date.WeekFunFactory;
import io.dingodb.expr.runtime.op.date.YearFunFactory;
import io.dingodb.expr.runtime.op.logical.AndFun;
import io.dingodb.expr.runtime.op.logical.OrFun;
import io.dingodb.expr.runtime.op.mathematical.AbsFunFactory;
import io.dingodb.expr.runtime.op.mathematical.AcosFunFactory;
import io.dingodb.expr.runtime.op.mathematical.AsinFunFactory;
import io.dingodb.expr.runtime.op.mathematical.AtanFunFactory;
import io.dingodb.expr.runtime.op.mathematical.CeilFunFactory;
import io.dingodb.expr.runtime.op.mathematical.CosFunFactory;
import io.dingodb.expr.runtime.op.mathematical.CoshFunFactory;
import io.dingodb.expr.runtime.op.mathematical.ExpFunFactory;
import io.dingodb.expr.runtime.op.mathematical.FloorFunFactory;
import io.dingodb.expr.runtime.op.mathematical.LogFunFactory;
import io.dingodb.expr.runtime.op.mathematical.MaxFunFactory;
import io.dingodb.expr.runtime.op.mathematical.MinFunFactory;
import io.dingodb.expr.runtime.op.mathematical.ModFunFactory;
import io.dingodb.expr.runtime.op.mathematical.PowFunFactory;
import io.dingodb.expr.runtime.op.mathematical.Round1FunFactory;
import io.dingodb.expr.runtime.op.mathematical.Round2FunFactory;
import io.dingodb.expr.runtime.op.mathematical.SinFunFactory;
import io.dingodb.expr.runtime.op.mathematical.SinhFunFactory;
import io.dingodb.expr.runtime.op.mathematical.TanFunFactory;
import io.dingodb.expr.runtime.op.mathematical.TanhFunFactory;
import io.dingodb.expr.runtime.op.special.CaseFun;
import io.dingodb.expr.runtime.op.special.IfNullFunFactory;
import io.dingodb.expr.runtime.op.special.IsFalseFunFactory;
import io.dingodb.expr.runtime.op.special.IsNullFunFactory;
import io.dingodb.expr.runtime.op.special.IsTrueFunFactory;
import io.dingodb.expr.runtime.op.string.CharLengthFunFactory;
import io.dingodb.expr.runtime.op.string.ConcatFun;
import io.dingodb.expr.runtime.op.string.ConvertPattern1FunFactory;
import io.dingodb.expr.runtime.op.string.ConvertPattern2FunFactory;
import io.dingodb.expr.runtime.op.string.ConvertTimeFormatFunFactory;
import io.dingodb.expr.runtime.op.string.HexFunFactory;
import io.dingodb.expr.runtime.op.string.LTrim1FunFactory;
import io.dingodb.expr.runtime.op.string.LTrim2FunFactory;
import io.dingodb.expr.runtime.op.string.LeftFunFactory;
import io.dingodb.expr.runtime.op.string.Locate2FunFactory;
import io.dingodb.expr.runtime.op.string.Locate3FunFactory;
import io.dingodb.expr.runtime.op.string.LowerFunFactory;
import io.dingodb.expr.runtime.op.string.MatchesFunFactory;
import io.dingodb.expr.runtime.op.string.MatchesIgnoreCaseFunFactory;
import io.dingodb.expr.runtime.op.string.Mid2FunFactory;
import io.dingodb.expr.runtime.op.string.Mid3FunFactory;
import io.dingodb.expr.runtime.op.string.NumberFormatFunFactory;
import io.dingodb.expr.runtime.op.string.RTrim1FunFactory;
import io.dingodb.expr.runtime.op.string.RTrim2FunFactory;
import io.dingodb.expr.runtime.op.string.RepeatFunFactory;
import io.dingodb.expr.runtime.op.string.ReplaceFunFactory;
import io.dingodb.expr.runtime.op.string.ReverseFunFactory;
import io.dingodb.expr.runtime.op.string.RightFunFactory;
import io.dingodb.expr.runtime.op.string.SubStringIndexFunFactory;
import io.dingodb.expr.runtime.op.string.Substr2FunFactory;
import io.dingodb.expr.runtime.op.string.Substr3FunFactory;
import io.dingodb.expr.runtime.op.string.Trim1FunFactory;
import io.dingodb.expr.runtime.op.string.Trim2FunFactory;
import io.dingodb.expr.runtime.op.string.UpperFunFactory;
import io.dingodb.expr.runtime.op.time.CurrentDateFun;
import io.dingodb.expr.runtime.op.time.CurrentTimeFun;
import io.dingodb.expr.runtime.op.time.CurrentTimestampFun;
import io.dingodb.expr.runtime.op.time.DateDiffFunFactory;
import io.dingodb.expr.runtime.op.time.DateFormat1FunFactory;
import io.dingodb.expr.runtime.op.time.DateFormat2FunFactory;
import io.dingodb.expr.runtime.op.time.FromUnixTimeFunFactory;
import io.dingodb.expr.runtime.op.time.TimeFormat1FunFactory;
import io.dingodb.expr.runtime.op.time.TimeFormat2FunFactory;
import io.dingodb.expr.runtime.op.time.TimestampFormat1FunFactory;
import io.dingodb.expr.runtime.op.time.TimestampFormat2FunFactory;
import io.dingodb.expr.runtime.op.time.UnixTimestamp0Fun;
import io.dingodb.expr.runtime.op.time.UnixTimestamp1FunFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.TreeMap;

public class DefaultFunFactory implements FunFactory {
    protected final Map<String, NullaryOp> nullaryFunMap;
    protected final Map<String, UnaryOp> unaryFunMap;
    protected final Map<String, BinaryOp> binaryFunMap;
    protected final Map<String, TertiaryOp> tertiaryFunMap;
    protected final Map<String, VariadicOp> variadicFunMap;

    public DefaultFunFactory(@NonNull ExprConfig config) {
        nullaryFunMap = new TreeMap<>(String::compareToIgnoreCase);
        unaryFunMap = new TreeMap<>(String::compareToIgnoreCase);
        binaryFunMap = new TreeMap<>(String::compareToIgnoreCase);
        tertiaryFunMap = new TreeMap<>(String::compareToIgnoreCase);
        variadicFunMap = new TreeMap<>(String::compareToIgnoreCase);

        // Castings
        registerUnaryFun("CAST" + IntType.NAME, config.withRangeCheck() ? Exprs.TO_INT_C : Exprs.TO_INT);
        registerUnaryFun("CAST" + LongType.NAME, config.withRangeCheck() ? Exprs.TO_LONG_C : Exprs.TO_LONG);
        registerUnaryFun("CAST" + FloatType.NAME, Exprs.TO_FLOAT);
        registerUnaryFun("CAST" + DoubleType.NAME, Exprs.TO_DOUBLE);
        registerUnaryFun("CAST" + BoolType.NAME, Exprs.TO_BOOL);
        registerUnaryFun("CAST" + DecimalType.NAME, Exprs.TO_DECIMAL);
        registerUnaryFun("CAST" + StringType.NAME, Exprs.TO_STRING);
        registerUnaryFun("CAST" + BytesType.NAME, Exprs.TO_BYTES);
        registerUnaryFun("CAST" + DateType.NAME, Exprs.TO_DATE);
        registerUnaryFun("CAST" + TimeType.NAME, Exprs.TO_TIME);
        registerUnaryFun("CAST" + TimestampType.NAME, Exprs.TO_TIMESTAMP);
        registerUnaryFun(IntType.NAME, config.withRangeCheck() ? Exprs.TO_INT_C : Exprs.TO_INT);
        registerUnaryFun(LongType.NAME, config.withRangeCheck() ? Exprs.TO_LONG_C : Exprs.TO_LONG);
        registerUnaryFun(FloatType.NAME, Exprs.TO_FLOAT);
        registerUnaryFun(DoubleType.NAME, Exprs.TO_DOUBLE);
        registerUnaryFun(BoolType.NAME, Exprs.TO_BOOL);
        registerUnaryFun(DecimalType.NAME, Exprs.TO_DECIMAL);
        registerUnaryFun(StringType.NAME, Exprs.TO_STRING);
        registerUnaryFun(BytesType.NAME, Exprs.TO_BYTES);
        registerUnaryFun(DateType.NAME, Exprs.TO_DATE);
        registerUnaryFun(TimeType.NAME, Exprs.TO_TIME);
        registerUnaryFun(TimestampType.NAME, Exprs.TO_TIMESTAMP);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + IntType.NAME, Exprs.TO_ARRAY_INT);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + LongType.NAME, Exprs.TO_ARRAY_LONG);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + FloatType.NAME, Exprs.TO_ARRAY_FLOAT);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + DoubleType.NAME, Exprs.TO_ARRAY_DOUBLE);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + BoolType.NAME, Exprs.TO_ARRAY_BOOL);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + DecimalType.NAME, Exprs.TO_ARRAY_DECIMAL);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + StringType.NAME, Exprs.TO_ARRAY_STRING);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + BytesType.NAME, Exprs.TO_ARRAY_BYTES);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + DateType.NAME, Exprs.TO_ARRAY_DATE);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + TimeType.NAME, Exprs.TO_ARRAY_TIME);
        registerUnaryFun(ArrayConstructorOpFactory.NAME + "_" + TimestampType.NAME, Exprs.TO_ARRAY_TIMESTAMP);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + IntType.NAME, Exprs.TO_LIST_INT);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + LongType.NAME, Exprs.TO_LIST_LONG);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + FloatType.NAME, Exprs.TO_LIST_FLOAT);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + DoubleType.NAME, Exprs.TO_LIST_DOUBLE);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + BoolType.NAME, Exprs.TO_LIST_BOOL);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + DecimalType.NAME, Exprs.TO_LIST_DECIMAL);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + StringType.NAME, Exprs.TO_LIST_STRING);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + BytesType.NAME, Exprs.TO_LIST_BYTES);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + DateType.NAME, Exprs.TO_LIST_DATE);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + TimeType.NAME, Exprs.TO_LIST_TIME);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_" + TimestampType.NAME, Exprs.TO_LIST_TIMESTAMP);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + IntType.NAME, Exprs.TO_LIST_INT);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + LongType.NAME, Exprs.TO_LIST_LONG);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + FloatType.NAME, Exprs.TO_LIST_FLOAT);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + DoubleType.NAME, Exprs.TO_LIST_DOUBLE);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + BoolType.NAME, Exprs.TO_LIST_BOOL);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + DecimalType.NAME, Exprs.TO_LIST_DECIMAL);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + StringType.NAME, Exprs.TO_LIST_STRING);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + BytesType.NAME, Exprs.TO_LIST_BYTES);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + DateType.NAME, Exprs.TO_LIST_DATE);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + TimeType.NAME, Exprs.TO_LIST_TIME);
        registerUnaryFun(ListConstructorOpFactory.NAME + "_CAST" + TimestampType.NAME, Exprs.TO_LIST_TIMESTAMP);

        // Mathematical
        registerUnaryFun(AbsFunFactory.NAME, config.withRangeCheck() ? Exprs.ABS_C : Exprs.ABS);
        registerBinaryFun(ModFunFactory.NAME, Exprs.MOD);
        registerBinaryFun(MinFunFactory.NAME, Exprs.MIN);
        registerBinaryFun(MaxFunFactory.NAME, Exprs.MAX);
        registerUnaryFun(SinFunFactory.NAME, Exprs.SIN);
        registerUnaryFun(CosFunFactory.NAME, Exprs.COS);
        registerUnaryFun(TanFunFactory.NAME, Exprs.TAN);
        registerUnaryFun(AsinFunFactory.NAME, Exprs.ASIN);
        registerUnaryFun(AcosFunFactory.NAME, Exprs.ACOS);
        registerUnaryFun(AtanFunFactory.NAME, Exprs.ATAN);
        registerUnaryFun(SinhFunFactory.NAME, Exprs.SINH);
        registerUnaryFun(CoshFunFactory.NAME, Exprs.COSH);
        registerUnaryFun(TanhFunFactory.NAME, Exprs.TANH);
        registerUnaryFun(ExpFunFactory.NAME, Exprs.EXP);
        registerUnaryFun(LogFunFactory.NAME, Exprs.LOG);
        registerUnaryFun(CeilFunFactory.NAME, Exprs.CEIL);
        registerUnaryFun(FloorFunFactory.NAME, Exprs.FLOOR);
        registerBinaryFun(PowFunFactory.NAME, Exprs.POW);
        registerBinaryFun(Round2FunFactory.NAME, Exprs.ROUND2);
        registerUnaryFun(Round1FunFactory.NAME, Exprs.ROUND1);

        // Logical and special
        registerVariadicFun(AndFun.NAME, Exprs.AND_FUN);
        registerVariadicFun(OrFun.NAME, Exprs.OR_FUN);
        registerUnaryFun(IsNullFunFactory.NAME, Exprs.IS_NULL);
        registerUnaryFun(IsTrueFunFactory.NAME, Exprs.IS_TRUE);
        registerUnaryFun(IsFalseFunFactory.NAME, Exprs.IS_FALSE);
        registerVariadicFun(CaseFun.NAME, Exprs.CASE);
        registerBinaryFun(IfNullFunFactory.NAME, Exprs.IF_NULL);

        // String functions
        registerUnaryFun(CharLengthFunFactory.NAME, Exprs.CHAR_LENGTH);
        registerBinaryFun(ConcatFun.NAME, Exprs.CONCAT);
        registerUnaryFun(LowerFunFactory.NAME, Exprs.LOWER);
        registerUnaryFun(UpperFunFactory.NAME, Exprs.UPPER);
        registerBinaryFun(LeftFunFactory.NAME, Exprs.LEFT);
        registerBinaryFun(RightFunFactory.NAME, Exprs.RIGHT);
        registerUnaryFun(Trim1FunFactory.NAME, Exprs.TRIM1);
        registerBinaryFun(Trim2FunFactory.NAME, Exprs.TRIM2);
        registerUnaryFun(LTrim1FunFactory.NAME, Exprs.LTRIM1);
        registerBinaryFun(LTrim2FunFactory.NAME, Exprs.LTRIM2);
        registerUnaryFun(RTrim1FunFactory.NAME, Exprs.RTRIM1);
        registerBinaryFun(RTrim2FunFactory.NAME, Exprs.RTRIM2);
        registerBinaryFun(Substr2FunFactory.NAME, Exprs.SUBSTR2);
        registerTertiaryFun(Substr3FunFactory.NAME, Exprs.SUBSTR3);
        registerBinaryFun(Mid2FunFactory.NAME, Exprs.MID2);
        registerTertiaryFun(Mid3FunFactory.NAME, Exprs.MID3);
        registerBinaryFun(RepeatFunFactory.NAME, Exprs.REPEAT);
        registerUnaryFun(ReverseFunFactory.NAME, Exprs.REVERSE);
        registerTertiaryFun(ReplaceFunFactory.NAME, Exprs.REPLACE);
        registerBinaryFun(Locate2FunFactory.NAME, Exprs.LOCATE2);
        registerTertiaryFun(Locate3FunFactory.NAME, Exprs.LOCATE3);
        registerUnaryFun(HexFunFactory.NAME, Exprs.HEX);
        registerBinaryFun(NumberFormatFunFactory.NAME, Exprs.FORMAT);
        registerBinaryFun(MatchesFunFactory.NAME, Exprs.MATCHES);
        registerBinaryFun(MatchesIgnoreCaseFunFactory.NAME, Exprs.MATCHES_NC);
        registerUnaryFun(ConvertTimeFormatFunFactory.NAME, Exprs._CTF);
        registerUnaryFun(ConvertPattern1FunFactory.NAME, Exprs._CP1);
        registerBinaryFun(ConvertPattern2FunFactory.NAME, Exprs._CP2);
        registerTertiaryFun(SubStringIndexFunFactory.NAME, Exprs.SUBSTRING_INDEX);

        // Time functions
        registerNullaryFun(CurrentDateFun.NAME, Exprs.CURRENT_DATE);
        registerNullaryFun(CurrentTimeFun.NAME, Exprs.CURRENT_TIME);
        registerNullaryFun(CurrentTimestampFun.NAME, Exprs.CURRENT_TIMESTAMP);
        registerUnaryFun(DateFormat1FunFactory.NAME, Exprs.DATE_FORMAT1);
        registerBinaryFun(DateFormat2FunFactory.NAME, Exprs.DATE_FORMAT2);
        registerUnaryFun(TimeFormat1FunFactory.NAME, Exprs.TIME_FORMAT1);
        registerBinaryFun(TimeFormat2FunFactory.NAME, Exprs.TIME_FORMAT2);
        registerUnaryFun(TimestampFormat1FunFactory.NAME, Exprs.TIMESTAMP_FORMAT1);
        registerBinaryFun(TimestampFormat2FunFactory.NAME, Exprs.TIMESTAMP_FORMAT2);
        registerUnaryFun(FromUnixTimeFunFactory.NAME, Exprs.FROM_UNIXTIME);
        registerUnaryFun(UnixTimestamp1FunFactory.NAME, Exprs.UNIX_TIMESTAMP1);
        registerNullaryFun(UnixTimestamp0Fun.NAME, Exprs.UNIX_TIMESTAMP0);
        registerBinaryFun(DateDiffFunFactory.NAME, Exprs.DATEDIFF);

        // Extract functions
        registerUnaryFun(YearFunFactory.NAME, Exprs.YEAR);
        registerUnaryFun(MonthFunFactory.NAME, Exprs.MONTH);
        registerUnaryFun(DayFunFactory.NAME, Exprs.DAY);
        registerUnaryFun(WeekFunFactory.NAME, Exprs.WEEK);
        registerUnaryFun(HourFunFactory.NAME, Exprs.HOUR);
        registerUnaryFun(MinuteFunFactory.NAME, Exprs.MINUTE);
        registerUnaryFun(SecondFunFactory.NAME, Exprs.SECOND);
        registerUnaryFun(MillisecondFunFactory.NAME, Exprs.MILLISECOND);
        registerUnaryFun(DayHourFunFactory.NAME, Exprs.DAY_HOUR);
        registerUnaryFun(DayMinuteFunFactory.NAME, Exprs.DAY_MINUTE);
        registerUnaryFun(DaySecondFunFactory.NAME, Exprs.DAY_SECOND);
        registerUnaryFun(HourMinuteFunFactory.NAME, Exprs.HOUR_MINUTE);
        registerUnaryFun(HourSecondFunFactory.NAME, Exprs.HOUR_SECOND);
        registerUnaryFun(MinuteSecondFunFactory.NAME, Exprs.MINUTE_SECOND);

        // Collection functions
        registerVariadicFun(ArrayConstructorOpFactory.NAME, Exprs.ARRAY);
        registerVariadicFun(ListConstructorOpFactory.NAME, Exprs.LIST);
        registerVariadicFun(MapConstructorOpFactory.NAME, Exprs.MAP);
        registerBinaryFun(SliceOpFactory.NAME, Exprs.SLICE);

        // Aggregation functions
        registerUnaryFun(CountAgg.NAME, Exprs.COUNT_AGG);
        registerNullaryFun(CountAllAgg.NAME, Exprs.COUNT_ALL_AGG);
        registerUnaryFun(MinAgg.NAME, Exprs.MIN_AGG);
        registerUnaryFun(MaxAgg.NAME, Exprs.MAX_AGG);
        registerUnaryFun(SumAgg.NAME, Exprs.SUM_AGG);
        registerUnaryFun(Sum0Agg.NAME, Exprs.SUM0_AGG);
        registerUnaryFun(SingleValueAgg.NAME, Exprs.SINGLE_VALUE_AGG);
    }

    @Override
    public NullaryOp getNullaryFun(@NonNull String funName) {
        return nullaryFunMap.get(funName);
    }

    @Override
    public void registerNullaryFun(@NonNull String funName, @NonNull NullaryOp op) {
        nullaryFunMap.put(funName, op);
    }

    @Override
    public UnaryOp getUnaryFun(@NonNull String funName) {
        return unaryFunMap.get(funName);
    }

    @Override
    public void registerUnaryFun(@NonNull String funName, @NonNull UnaryOp op) {
        unaryFunMap.put(funName, op);
    }

    @Override
    public BinaryOp getBinaryFun(@NonNull String funName) {
        return binaryFunMap.get(funName);
    }

    @Override
    public void registerBinaryFun(@NonNull String funName, @NonNull BinaryOp op) {
        binaryFunMap.put(funName, op);
    }

    @Override
    public TertiaryOp getTertiaryFun(@NonNull String funName) {
        return tertiaryFunMap.get(funName);
    }

    @Override
    public void registerTertiaryFun(@NonNull String funName, @NonNull TertiaryOp op) {
        tertiaryFunMap.put(funName, op);
    }

    @Override
    public VariadicOp getVariadicFun(@NonNull String funName) {
        return variadicFunMap.get(funName);
    }

    @Override
    public void registerVariadicFun(@NonNull String funName, @NonNull VariadicOp op) {
        variadicFunMap.put(funName, op);
    }
}
