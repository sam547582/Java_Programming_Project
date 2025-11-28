package util;

import java.util.*;

public class ExpressionEvaluator {

    // 연산자 우선순위 지정
    private static int precedence(char op) {
        switch (op) {
            case '^': return 3;
            case '*':
            case '/': return 2;
            case '+':
            case '-': return 1;
            default: return -1;
        }
    }

    // 중위표기법 → 후위표기법 변환
    private static List<String> toPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        StringBuilder number = new StringBuilder();

        for (int i=0; i<expr.length(); i++) {
            char c = expr.charAt(i);

            // 숫자 혹은 소수점
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            }
            else {
                // 숫자가 끝나면 output에 추가
                if (number.length() > 0) {
                    output.add(number.toString());
                    number.setLength(0);
                }

                // 괄호 처리
                if (c == '(') {
                    stack.push(c);
                } 
                else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(')
                        output.add(String.valueOf(stack.pop()));
                    stack.pop(); // '(' 제거
                }
                else {
                    // 연산자
                    while (!stack.isEmpty() && 
                        precedence(stack.peek()) >= precedence(c)) {
                        output.add(String.valueOf(stack.pop()));
                    }
                    stack.push(c);
                }
            }
        }

        // 마지막 남은 숫자 추가
        if (number.length() > 0) {
            output.add(number.toString());
        }

        // 스택 나머지 모두 pop
        while (!stack.isEmpty()) {
            output.add(String.valueOf(stack.pop()));
        }

        return output;
    }

    // 후위표기법 → 계산
    private static double evalPostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (token.matches("-?\\d+(\\.\\d+)?")) { // 숫자
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();

                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                    case "^": stack.push(Math.pow(a, b)); break;
                }
            }
        }

        return stack.pop();
    }
    
    public static double evaluateWithX(String expr, double xValue) {
        expr = expr.replaceAll("\\s+", ""); // 공백 제거

        // 1) x 대입: x → (xValue)
        // x가 문자 단독이기 때문에 정규식으로 처리
        expr = expr.replaceAll("x", "(" + xValue + ")");

        // 2) 기존 수식 계산기로 넘김
        return evaluate(expr);
    }

    // 최종 evaluate
    public static double evaluate(String expr) {
        expr = expr.replaceAll("\\s+", ""); // 공백 제거
        List<String> postfix = toPostfix(expr);
        return evalPostfix(postfix);
    }
}
