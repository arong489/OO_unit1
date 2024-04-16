import java.util.HashMap;

import asset.Expr;

public class Funcall {
    private String name;
    private int cur = 0;
    private HashMap<String, Expr> args;
    private boolean argsfull;

    public Funcall(String name) {
        this.name = name;
        args = new HashMap<String, Expr>();
        this.argsfull = Funsave.getFparam(this.name)[0].equals("");
    }
    
    public void loadFactor(Expr...exprs) {
        int tot = 0;
        String[] fparas = Funsave.getFparam(this.name);
        for (;cur < fparas.length && tot < exprs.length; cur++, tot++) {
            args.put(fparas[cur], exprs[tot]);
        }
        if (cur == fparas.length) {
            argsfull = true;
        }
    }

    public Expr calculate() {
        Lexer lexer = new Lexer(Funsave.getContent(name));
        MyParser parser = new MyParser(lexer,args);
        return parser.parseExpr();
    }

    public boolean fullargs() {
        return this.argsfull;
    }
}
