package asset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Expr implements Factor,Gneral {
    private HashMap<Term, Num> terms;

    public Expr() {
        this.terms = new HashMap<Term, Num>();
    }

    public Expr(String num) {
        this.terms = new HashMap<Term, Num>();
        Term term = new Term(num);
        this.terms.put(term, term.getCoefficient());
    }

    public Expr(Expr expr) {
        this.terms = new HashMap<Term, Num>();
        expr.terms.forEach((key, value) -> {
            Term temp = new Term(key);
            this.terms.put(temp, temp.getCoefficient());
        });
    }

    public void add(Term term) {
        if (term.getCoefficient().equalsStr("0")) {
            return;
        }
        if (this.terms.containsKey(term)) {
            this.terms.get(term).add(term.getCoefficient());
        } else {
            Term temp = new Term(term);
            this.terms.put(temp, temp.getCoefficient());
        }
        if (this.terms.get(term).equalsStr("0")) {
            this.terms.remove(term);
        }
    }

    public void add(Expr expr) {
        expr.terms.forEach((key, value) -> {
            this.add(key);
        });
    }

    public void sub(Term term) {
        if (term.getCoefficient().equalsStr("0")) {
            return;
        }
        if (this.terms.containsKey(term)) {
            this.terms.get(term).sub(term.getCoefficient());
        } else {
            Term temp = term.multiply(new Num("-1"));
            this.terms.put(temp, temp.getCoefficient());
        }
        if (this.terms.get(term).equalsStr("0")) {
            this.terms.remove(term);
        }
    }

    public void sub(Expr expr) {
        expr.terms.forEach((key, value) -> {
            this.sub(key);
        });
    }

    public Expr multiply(Num num) {
        if (num.equalsStr("0")) {
            return new Expr();
        } else if (num.equalsStr("1")) {
            return new Expr(this);
        }
        Expr temp = new Expr();
        this.terms.forEach((key, value) -> {
            Term tempTerm = key.multiply(num);
            temp.terms.put(tempTerm, tempTerm.getCoefficient());
        });
        return temp;
    }

    public Expr multiply(Term term) {
        if (term.isNum()) {
            return this.multiply(term.getCoefficient());
        }
        Expr temp = new Expr();
        this.terms.forEach((key, value) -> {
            temp.add(key.multiply(term));
        });
        return temp;
    }

    public Expr multiply(Expr expr) {
        Expr temp = new Expr();
        expr.terms.forEach((key, value) -> {
            temp.add(this.multiply(key));
        });
        return temp;
    }

    public Gneral multiply(Gneral factor) {
        if (factor instanceof Expr) {
            return this.multiply((Expr)factor);
        } else {
            return this.multiply((Term)factor);
        }
    }

    public Expr pow(String indexString) {
        int index = Integer.valueOf(indexString);
        if (index == 1) {
            return this;
        } else if (index == 0) {
            return new Expr("1");
        } // special index accelerate
        Expr powExpr = new Expr(this);
        if (index <= 5) { // 5th power or less, normal calculate
            index--;
            while (index != 0) {
                powExpr = powExpr.multiply(this);
                index--;
            }
            return powExpr;
        }
        // 6th power or more, fast exponentiation
        Expr baseExpr;
        if ((index & 1) != 0) {
            baseExpr = new Expr(this);
        } else {
            baseExpr = new Expr("1");
        }
        index >>= 1;
        while (index != 0) {
            powExpr = powExpr.multiply(powExpr);
            if ((index & 1) != 0) {
                baseExpr = powExpr.multiply(baseExpr);
            }
            index >>= 1;
        }
        return baseExpr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Term, Num>> iterator = terms.entrySet().iterator();
        Map.Entry<Term, Num> entry;
        if (iterator.hasNext()) {
            entry = iterator.next();
        } else {
            return "0";
        }
        if (entry.getValue().equalsStr("-1")) {
            sb.append("-" + (entry.getKey().toString() != "" ? entry.getKey() : "1"));
        } else if (entry.getValue().equalsStr("1")) {
            sb.append(entry.getKey().toString() != "" ? entry.getKey() : "1");
        } else {
            sb.append(entry.getValue()
                + (entry.getKey().toString() != "" ? "*" : "") + entry.getKey());
        }
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue().equalsStr("-1")) {
                sb.append("-" + (entry.getKey().toString() != "" ? entry.getKey() : "1"));
            } else if (entry.getValue().equalsStr("1")) {
                sb.append("+" + (entry.getKey().toString() != "" ? entry.getKey() : "1"));
            } else if (entry.getValue().compareTo("0") < 0) {
                sb.append(entry.getValue()
                    + (entry.getKey().toString() != "" ? "*" : "") + entry.getKey());
            } else {
                sb.append("+" + entry.getValue()
                    + (entry.getKey().toString() != "" ? "*" : "") + entry.getKey());
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((terms == null) ? 0 : terms.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Expr other = (Expr) obj;
        if (terms == null) {
            if (other.terms != null) {
                return false;
            }
        } else if (!terms.equals(other.terms)) {
            return false;
        }
        return true;
    }

    @Override
    public Factor Derivation(String factorString) {
        Expr temp = new Expr();
        this.terms.forEach((key, value) -> {
            Factor deriFactor = key.Derivation(factorString);
            if (deriFactor instanceof Term) {
                temp.add((Term)deriFactor);
            } else {
                temp.add((Expr)deriFactor);
            }
        });
        return temp;
    }

    public boolean isPowFactor() {
        if (this.terms.isEmpty()) {
            return true;
        }
        if (this.terms.size() == 1) {
            return this.terms.keySet().iterator().next().isPowFactor();
        }
        return false;
    }

    public HashMap<Term, Num> getTerms() {
        return terms;
    }
}
