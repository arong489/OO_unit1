import java.util.Scanner;
import asset.Expr;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String function;
        while (n != 0) {
            do {
                function = scanner.nextLine();
            } while (function.equals(""));
            Funsave.registerFunction(inputTranslate(new StringBuilder(function.trim())));
            n--;
        }
        String expression;
        do {
            expression = scanner.nextLine();
        } while (expression.equals(""));
        expression = inputTranslate(new StringBuilder(expression.trim()));
        scanner.close();
        // build key object
        Lexer lexer = new Lexer(expression);
        MyParser parser = new MyParser(lexer);
        // begin to calculate the expression
        Expr expr = parser.parseExpr();
        System.out.println(expr);
    }

    /** handle white space and plural add/subtract char
     * @param expression : the stringbuilder of input
     */
    public static String inputTranslate(StringBuilder expression) {  
        for (int i = 0; i < expression.length(); i++) {
            while (expression.charAt(i) == ' ' || expression.charAt(i) == '\t') {
                expression.deleteCharAt(i);
            }
            boolean minus = false;
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                while (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                    if (expression.charAt(i) == '-') {
                        minus = !minus;
                    }
                    expression.deleteCharAt(i);
                    while (expression.charAt(i) == ' ' || expression.charAt(i) == '\t') {
                        expression.deleteCharAt(i);
                    }
                }
                expression.insert(i,minus ? '-' : '+');
            }
        }
        return expression.toString();
    }
}
