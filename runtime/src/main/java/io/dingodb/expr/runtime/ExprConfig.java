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

import io.dingodb.expr.runtime.utils.DateTimeUtils;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * 表达式配置类。
 */
public interface ExprConfig {
    /**
     * 简单表达式配置。
     */
    ExprConfig SIMPLE = new ExprConfig() {
    };

    /**
     * 高级表达式配置。
     */
    ExprConfig ADVANCED = new ExprConfig() {
        @Override
        public boolean withSimplification() {
            return true;
        }

        @Override
        public boolean withRangeCheck() {
            return true;
        }
    };

    default boolean withSimplification() {
        return false;
    }

    /**
     * 是否进行溢出检测，默认不进行溢出检测。
     * @return  true:进行溢出检测;false：不进行溢出检测;
     */
    default boolean withRangeCheck() {
        return false;
    }

    default boolean withGeneralOp() {
        return true;
    }

    /**
     * 返回默认时区。
     * @return
     */
    default TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    /**
     * 返回默认date格式。
     * @return
     */
    default DateTimeFormatter[] getParseDateFormatters() {
        return DateTimeUtils.DEFAULT_PARSE_DATE_FORMATTERS;
    }

    /**
     * 返回默认time格式。
     * @return
     */
    default DateTimeFormatter[] getParseTimeFormatters() {
        return DateTimeUtils.DEFAULT_PARSE_TIME_FORMATTERS;
    }

    default DateTimeFormatter[] getParseTimestampFormatters() {
        return DateTimeUtils.DEFAULT_PARSE_TIMESTAMP_FORMATTERS;
    }

    default DateTimeFormatter getOutputDateFormatter() {
        return DateTimeUtils.DEFAULT_OUTPUT_DATE_FORMATTER;
    }

    default DateTimeFormatter getOutputTimeFormatter() {
        return DateTimeUtils.DEFAULT_OUTPUT_TIME_FORMATTER;
    }

    default DateTimeFormatter getOutputTimestampFormatter() {
        return DateTimeUtils.DEFAULT_OUTPUT_TIMESTAMP_FORMATTER;
    }
}
