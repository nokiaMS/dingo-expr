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

package io.dingodb.expr.rel.op;

import com.fasterxml.jackson.databind.MappingIterator;
import io.dingodb.expr.json.runtime.Parser;
import io.dingodb.expr.rel.RelConfig;
import io.dingodb.expr.rel.SourceOp;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.op.collection.ArrayBuilder;
import io.dingodb.expr.runtime.type.AnyType;
import io.dingodb.expr.runtime.type.ArrayType;
import io.dingodb.expr.runtime.type.BoolType;
import io.dingodb.expr.runtime.type.BytesType;
import io.dingodb.expr.runtime.type.DateType;
import io.dingodb.expr.runtime.type.DecimalType;
import io.dingodb.expr.runtime.type.DoubleType;
import io.dingodb.expr.runtime.type.FloatType;
import io.dingodb.expr.runtime.type.IntType;
import io.dingodb.expr.runtime.type.ListType;
import io.dingodb.expr.runtime.type.LongType;
import io.dingodb.expr.runtime.type.StringType;
import io.dingodb.expr.runtime.type.TimeType;
import io.dingodb.expr.runtime.type.TimestampType;
import io.dingodb.expr.runtime.type.TupleType;
import io.dingodb.expr.runtime.type.Type;
import io.dingodb.expr.runtime.type.TypeVisitorBase;
import io.dingodb.expr.runtime.utils.CodecUtils;
import io.dingodb.expr.runtime.utils.DateTimeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class CsvValuesOp implements SourceOp {
    private final InputStream csvFile;
    @Getter
    private TupleType type;
    private transient ExprConfig exprConfig;

    public CsvValuesOp(InputStream csvFile) {
        this.csvFile = csvFile;
    }

    public CsvValuesOp(String... csvLines) {
        this.csvFile = IOUtils.toInputStream(String.join("\n", csvLines), StandardCharsets.UTF_8);
    }

    @Override
    public @NonNull Stream<Object[]> get() {
        try {
            FromStringConverter converter = new FromStringConverter();
            MappingIterator<String[]> it = Parser.CSV.readValues(csvFile, String[].class);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, 0), false)
                .map(i -> (Object[]) converter.visit(type, i));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(TupleType type, @NonNull RelConfig config) {
        this.type = type;
        exprConfig = config.getExprCompiler().getConfig();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private class FromStringConverter extends TypeVisitorBase<Object, @NonNull Object> {
        @Override
        public @NonNull Object visitIntType(@NonNull IntType type, @NonNull Object obj) {
            return Integer.parseInt((String) obj);
        }

        @Override
        public @NonNull Object visitLongType(@NonNull LongType type, @NonNull Object obj) {
            return Long.parseLong((String) obj);
        }

        @Override
        public @NonNull Object visitFloatType(@NonNull FloatType type, @NonNull Object obj) {
            return Float.parseFloat((String) obj);
        }

        @Override
        public @NonNull Object visitDoubleType(@NonNull DoubleType type, @NonNull Object obj) {
            return Double.parseDouble((String) obj);
        }

        @Override
        public @NonNull Object visitBoolType(@NonNull BoolType type, @NonNull Object obj) {
            return Boolean.getBoolean((String) obj);
        }

        @Override
        public @NonNull Object visitDecimalType(@NonNull DecimalType type, @NonNull Object obj) {
            return new BigDecimal((String) obj);
        }

        @Override
        public @NonNull Object visitStringType(@NonNull StringType type, @NonNull Object obj) {
            return obj;
        }

        @Override
        public @NonNull Object visitBytesType(@NonNull BytesType type, @NonNull Object obj) {
            return CodecUtils.hexStringToBytes((String) obj);
        }

        @Override
        public Object visitDateType(@NonNull DateType type, @NonNull Object obj) {
            return DateTimeUtils.parseDate((String) obj, exprConfig.getParseDateFormatters());
        }

        @Override
        public Object visitTimeType(@NonNull TimeType type, @NonNull Object obj) {
            return DateTimeUtils.parseTime((String) obj, exprConfig.getParseTimeFormatters());
        }

        @Override
        public Object visitTimestampType(@NonNull TimestampType type, @NonNull Object obj) {
            return DateTimeUtils.parseTimestamp((String) obj, exprConfig.getParseTimestampFormatters());
        }

        @Override
        public @NonNull Object visitAnyType(@NonNull AnyType type, @NonNull Object obj) {
            return obj;
        }

        @Override
        public @NonNull Object visitArrayType(@NonNull ArrayType type, @NonNull Object obj) {
            final String[] strings = (String[]) obj;
            final Type elementType = type.getElementType();
            Object array = ArrayBuilder.INSTANCE.visit(elementType, strings.length);
            for (int i = 0; i < strings.length; ++i) {
                Array.set(array, i, visit(elementType, strings[i]));
            }
            return array;
        }

        @Override
        public Object visitListType(@NonNull ListType type, @NonNull Object obj) {
            final String[] strings = (String[]) obj;
            final Type elementType = type.getElementType();
            return Arrays.stream(strings)
                .map(s -> visit(elementType, s))
                .collect(Collectors.toList());
        }

        @Override
        public @NonNull Object visitTupleType(@NonNull TupleType type, @NonNull Object obj) {
            final String[] strings = (String[]) obj;
            final Type[] types = type.getTypes();
            Object[] tuple = new Object[strings.length];
            for (int i = 0; i < strings.length; ++i) {
                tuple[i] = visit(types[i], strings[i]);
            }
            return tuple;
        }
    }
}
