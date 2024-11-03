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

import io.dingodb.expr.parser.antlr.DingoExprErrorListener;
import io.dingodb.expr.parser.antlr.DingoExprParserVisitorImpl;
import io.dingodb.expr.parser.exception.ExprParseException;
import io.dingodb.expr.parser.exception.ExprSyntaxError;
import io.dingodb.expr.runtime.ExprConfig;
import io.dingodb.expr.runtime.expr.Expr;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

/**
 * 表达式编译器。
 */
public final class ExprParser {
    /**
     * 默认的表达式解析器实例。
     */
    public static final ExprParser DEFAULT = new ExprParser(new DefaultFunFactory(ExprConfig.SIMPLE));

    /**
     * 表达式访问者。
     */
    private final DingoExprParserVisitorImpl visitor;

    public ExprParser(FunFactory funFactory) {
        visitor = new DingoExprParserVisitorImpl(funFactory);
    }

    /**
     * 把一个表达式字符串解析成未编译的Expr表达式。
     * Parse a given string input into an un-compiled {@link Expr}.
     *
     * @param input the given string    表达式字符串。
     * @return the {@link Expr}         解析后的Expr表达式。
     * @throws ExprParseException if errors occurred in parsing
     */
    public Expr parse(String input) throws ExprParseException {
        //字符串转换为字符流。
        CharStream stream = CharStreams.fromString(input);
        //构建表达式词法解析器。
        DingoExprLexer lexer = new DingoExprLexer(stream);
        //获得表达式token流。
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //构建表达式的语法解析器。
        DingoExprParser parser = new DingoExprParser(tokens);

        //移除antlr自带的错误监听器，添加dingo自带的错误监听器。
        parser.removeErrorListeners();
        DingoExprErrorListener errorListener = new DingoExprErrorListener();
        parser.addErrorListener(errorListener);

        //语法解析，形成解析树。
        ParseTree tree = parser.expr();

        //如果解析过程中有错误发生，那么抛出异常，中断执行。
        List<String> errorMessages = errorListener.getErrorMessages();
        if (!errorMessages.isEmpty()) {
            throw new ExprSyntaxError(errorMessages);
        }
        try {
            //执行解析后的表达式并返回执行结果。
            return visitor.visit(tree);
        } catch (ParseCancellationException e) {
            throw new ExprParseException(e);
        }
    }

    public FunFactory getFunFactory() {
        return visitor.getFunFactory();
    }
}
