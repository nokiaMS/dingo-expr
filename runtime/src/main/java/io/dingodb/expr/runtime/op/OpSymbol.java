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

package io.dingodb.expr.runtime.op;

/**
 * 操作符。
 */
public final class OpSymbol {
    public static final String ARRAY = "[]";    //数组操作符。
    public static final String FUN = "()";      //函数操作符。
    public static final String POS = "+";       //正数操作符。
    public static final String NEG = "-";       //负数操作符。
    public static final String ADD = " + ";     //加法操作符。
    public static final String SUB = " - ";     //减法操作符。
    public static final String MUL = "*";       //乘法操作符。
    public static final String DIV = "/";       //除法操作符。
    public static final String LT = " < ";      //小于。
    public static final String LE = " <= ";     //小于等于。
    public static final String EQ = " == ";     //等于
    public static final String GT = " > ";      //大于
    public static final String GE = " >= ";     //大于等于
    public static final String NE = " != ";     //不等于
    public static final String NOT = "!";       //逻辑非
    public static final String AND = " && ";    //逻辑与
    public static final String OR = " || ";     //逻辑或

    /**
     * 默认构造函数为私有，表示这个类不能创建实例对象。
     */
    private OpSymbol() {
    }
}
