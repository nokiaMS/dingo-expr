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

package io.dingodb.expr.console;

import io.dingodb.expr.parser.ExprParser;
import io.dingodb.expr.runtime.ExprCompiler;
import io.dingodb.expr.runtime.expr.Expr;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

/**
 * dingo-expr的控制台程序入口。
 */
@Slf4j
public final class DingoExprConsole {
    /**
     * 默认构造函数。
     */
    private DingoExprConsole() {
    }

    /**
     * 主函数。
     * @param args
     */
    public static void main(String[] args) {
        //ResourceBundle是一个处理国际化资源的类，提供了加载不同语言版本的资源文件的能力。他使得我们在不修改代码的情况下，根据用户的语言环境切换显示内容。
        ResourceBundle config = ResourceBundle.getBundle("config");
        //从资源文件中获得hello的显示形式并打印。
        System.out.println(config.getString("hello"));
        //获得命令行提示符。
        String prompt = config.getString("prompt");
        //建立一个缓冲输入流以读取字符流。System.in表示控制台输入。
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //循环等待并处理用户输入。
        while (true) {
            System.out.print(prompt + ' ');
            try {
                //获得输入。
                String input = reader.readLine();
                //如果输入为空则结束循环，跳出控制台，程序结束。
                if (input == null || input.isEmpty()) {
                    break;
                }
                //进行表达式解析。
                Expr expr = ExprParser.DEFAULT.parse(input);
                //进行表达式编译。
                Expr expr1 = ExprCompiler.SIMPLE.visit(expr);
                //执行表达式并显示结果。
                System.out.println(expr1.eval());
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }
}
