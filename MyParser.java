import java.util.HashMap;

import asset.Expr;
import asset.Gneral;
import asset.Num;
import asset.Term;
import asset.Tirgono;
import asset.Var;

public class MyParser {
    private final Lexer lexer;
    private HashMap<String, Expr> args;

    public MyParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public MyParser(Lexer lexer, HashMap<String, Expr> args) {
        this.lexer = lexer;
        this.args = args;
    }

    /**Expression Entrance
     * @return a string expresion
    */
    public Expr parseExpr() {
        Gneral temp = parseMulti();
        Expr expr;
        if (temp instanceof Expr) {
            expr = (Expr)temp;
        } else {
            expr = new Expr();
            expr.add((Term)temp);
        }

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("+")) {
                lexer.next();
                temp = parseMulti();
                if (temp instanceof Expr) {
                    expr.add((Expr)temp);
                } else {
                    expr.add((Term)temp);
                }
            } else {
                lexer.next();
                temp = parseMulti();
                if (temp instanceof Expr) {
                    expr.sub((Expr)temp);
                } else {
                    expr.sub((Term)temp);
                }
            }
        }

        return expr;
    }

    private Gneral parseMulti() {
        Gneral temp = parsePowAndMinus();

        while (lexer.peek().equals("*")) {
            lexer.next();
            temp = temp.multiply(parsePowAndMinus());
        }

        return temp;
    }

    /**
     * Factor Entrance
     * @return a factor
    */
    public Gneral parsePowAndMinus() {
        boolean minus = false;
        if (lexer.peek().equals("-")) {
            minus = true;
            lexer.next();
        }
        if (lexer.peek().equals("+")) {
            lexer.next();
        }
        Gneral factor = parseFactor();
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            if (lexer.peek().equals("1")) {
                lexer.next();
                return minus ? factor.multiply(new Num("-1")) : factor;
            } else {
                factor = factor.pow(lexer.peek());
                lexer.next();
                return minus ? factor.multiply(new Num("-1")) : factor;
            }
        } else {
            return minus ? factor.multiply(new Num("-1")) : factor;
        }
    }

    private Gneral parseFactor() {
        String factorString = lexer.peek();
        if (factorString.equals("sin(") || factorString.equals("cos(")) {
            lexer.next();
            Tirgono foo = new Tirgono(factorString, parseExpr());
            lexer.next();
            Term temp = new Term(foo);
            return temp;
        } else if (factorString.equals("(")) { //get expression factor
            lexer.next();
            Expr expr = parseExpr();
            lexer.next();
            return expr;
        } else if (Character.isDigit(factorString.charAt(0))) { // get num factor
            lexer.next();
            return new Term(factorString);
        } else if (factorString.charAt(0) == '@') {
            Funcall tmp = new Funcall(factorString.substring(1));
            lexer.next();
            if (tmp.fullargs()) {
                lexer.next();
            }
            while (!tmp.fullargs()) {
                tmp.loadFactor(parseExpr());
                lexer.next();
            }
            return tmp.calculate();
        } else if (factorString.charAt(0) == '&') {
            lexer.next();
            Expr temp = (Expr)parseExpr().Derivation(factorString.substring(1));
            lexer.next();
            return temp;
        } else if (args != null && args.containsKey(factorString)) {
            lexer.next();
            return new Expr(args.get(factorString));
        } else { //get var factor
            Var varFactor = new Var(factorString);
            lexer.next();
            return new Term(varFactor);
        }
    }
}
