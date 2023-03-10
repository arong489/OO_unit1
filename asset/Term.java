package asset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Term implements Factor,Gneral {
    private Num coefficient;
    private HashMap<Var, Integer> vars;
    private HashMap<Tirgono, Integer> trigos;

    public Term() {
        this.coefficient = new Num("1");
        this.vars = new HashMap<Var, Integer>();
        this.trigos = new HashMap<Tirgono, Integer>();
    }

    public Term(String num) {
        this.coefficient = new Num(num);
        this.vars = new HashMap<Var, Integer>();
        this.trigos = new HashMap<Tirgono, Integer>();
    }

    public Term(Var var) {
        this.coefficient = new Num("1");
        this.vars = new HashMap<Var, Integer>();
        this.trigos = new HashMap<Tirgono, Integer>();
        this.vars.put(var, new Integer(1));
    }

    public Term(Tirgono trigo) {
        Factor simp = trigo.simplify();
        if (simp instanceof Num) {
            this.coefficient = new Num(simp.toString());
            this.vars = new HashMap<Var, Integer>();
            this.trigos = new HashMap<Tirgono, Integer>();
        } else {
            this.coefficient = new Num(trigo.minusHead() ? "-1" : "1");
            this.vars = new HashMap<Var, Integer>();
            this.trigos = new HashMap<Tirgono, Integer>();
            this.trigos.put(trigo, new Integer(1));
        }
    }

    public Term(Term term) {
        this.coefficient = new Num(term.coefficient);
        // this.vars = new HashMap<String, Pow>(term.vars); //? 深拷贝
        this.vars = new HashMap<Var, Integer>();
        term.vars.forEach((key, value) -> {
            this.vars.put(key, new Integer(value));
        });
        this.trigos = new HashMap<Tirgono, Integer>();
        term.trigos.forEach((key, value) -> {
            this.trigos.put(key, new Integer(value));
        });
    }

    public boolean isNum() {
        return this.coefficient.equalsStr("0") || (this.vars.isEmpty() && this.trigos.isEmpty());
    }

    public boolean isPowFactor() {
        return this.isNum() ||
                (this.coefficient.equalsStr("1") &&
                        (this.vars.size() + this.trigos.size() == 1));
    }

    public Num getCoefficient() {
        return this.coefficient;
    }

    public Term multiply(Num num) {
        if (num.equalsStr("0") || this.coefficient.equalsStr("0")) {
            return new Term("0");
        }
        Term temp = new Term(this);
        temp.coefficient = this.coefficient.multiply(num);
        return temp;
    }

    // public Term multiply(Var varFactor) {
    //     Term temp = new Term(this);
    //     if (temp.vars.containsKey(varFactor)) {
    //         temp.vars.put(varFactor, temp.vars.get(varFactor) + 1);
    //     } else {
    //         temp.vars.put(varFactor, new Integer(1));
    //     }
    //     if (temp.vars.get(varFactor) == 0) {
    //         temp.vars.remove(varFactor);
    //     }
    //     return temp;
    // }

    // public Term multiply(Tirgono triFactor) {
    //     Term temp = new Term(this);
    //     if (temp.trigos.containsKey(triFactor)) {
    //         temp.trigos.put(triFactor, temp.trigos.get(triFactor) + 1);
    //     } else {
    //         temp.trigos.put(triFactor, new Integer(1));
    //     }
    //     if (temp.trigos.get(triFactor) == 0) {
    //         temp.trigos.remove(triFactor);
    //     }
    //     return temp;
    // }

    public Term multiply(Term term) {
        Term temp = this.multiply(term.coefficient);
        if (temp.coefficient.equalsStr("0")) {
            return temp;
        }
        term.vars.forEach((key, value) -> {
            if (temp.vars.containsKey(key)) {
                temp.vars.put(key, temp.vars.get(key) + value);
            } else {
                temp.vars.put(key, new Integer(value));
            }
            if (temp.vars.get(key) == 0) {
                temp.vars.remove(key);
            }
        });
        term.trigos.forEach((key, value) -> {
            if (temp.trigos.containsKey(key)) {
                temp.trigos.put(key, temp.trigos.get(key) + value);
            } else {
                temp.trigos.put(key, new Integer(value));
            }
            if (temp.trigos.get(key) == 0) {
                temp.trigos.remove(key);
            }
        });
        return temp;
    }

    public Gneral multiply(Gneral factor) {
        if (factor instanceof Expr) {
            return ((Expr)factor).multiply(this);
        } else {
            return this.multiply((Term)factor);
        }
    }

    public Term pow(String indexString) {
        int index = Integer.valueOf(indexString);
        if (index == 1) {
            return new Term(this);
        } else if (index == 0) {
            return new Term();
        }
        Term temp = new Term(this);
        temp.coefficient = temp.coefficient.pow(indexString);
        for (Var varFactor : temp.vars.keySet()) {
            temp.vars.put(varFactor, temp.vars.get(varFactor) * index);
        }
        for (Tirgono triFactor : temp.trigos.keySet()) {
            temp.trigos.put(triFactor, temp.trigos.get(triFactor) * index);
        }
        return temp;
    }

    @Override
    public Factor Derivation(String factorString) {
        Expr tempExpr = new Expr();
        //Vars. derivation multiply principle
        for (Map.Entry<Var, Integer> entry : this.vars.entrySet()) {
            Term tempTerm = new Term(this.getCoefficient().toString());
            for (Map.Entry<Tirgono, Integer> copy : this.trigos.entrySet()) {
                tempTerm.trigos.put(copy.getKey(), new Integer(copy.getValue()));
            }
            for (Map.Entry<Var, Integer> inner : this.vars.entrySet()) {
                if (inner == entry) {
                    if (inner.getKey().toString().equals(factorString)) {
                        if (inner.getValue() > 1) {
                            tempTerm.vars.put(inner.getKey(), inner.getValue() - 1);
                            tempTerm = tempTerm.multiply(new Num(inner.getValue().toString()));
                        }
                    }
                    else {
                        tempTerm = tempTerm.multiply(new Num("0"));
                        break;
                    }
                } else {
                    tempTerm.vars.put(inner.getKey(), new Integer(inner.getValue()));
                }
            }
            tempExpr.add(tempTerm);
        }
        //Trigono. derivation multiply principle
        for (Map.Entry<Tirgono, Integer> entry : this.trigos.entrySet()) {
            Term tempTerm = new Term(this.getCoefficient().toString());
            Expr devExpr = new Expr();
            for (Map.Entry<Var, Integer> copy : this.vars.entrySet()) {
                tempTerm.vars.put(copy.getKey(), new Integer(copy.getValue()));
            }
            for (Map.Entry<Tirgono, Integer> inner : this.trigos.entrySet()) {
                if (inner == entry) {
                    devExpr = (Expr) inner.getKey().Derivation(factorString);
                    if (inner.getValue() > 1) {
                        tempTerm.trigos.put(inner.getKey(), inner.getValue() - 1);
                        tempTerm = tempTerm.multiply(new Num(inner.getValue().toString()));
                    }
                } else {
                    tempTerm.trigos.put(inner.getKey(), new Integer(inner.getValue()));
                }
            }
            tempExpr.add(devExpr.multiply(tempTerm));
        }
        return tempExpr;
    }

    @Override
    public String toString() {
        if (this.isNum()) {
            return "";
        }
        StringBuilder sbuilder = new StringBuilder();
        Iterator<Map.Entry<Var, Integer>> varsIter = this.vars.entrySet().iterator();
        Map.Entry<Var, Integer> varFactor;
        Iterator<Map.Entry<Tirgono, Integer>> triIter = this.trigos.entrySet().iterator();
        Map.Entry<Tirgono, Integer> tirFactor;
        if (!varsIter.hasNext()) {
            tirFactor = triIter.next();
            if (tirFactor.getValue().intValue() == 1) {
                sbuilder.append(tirFactor.getKey().toString());
            } else {
                sbuilder.append(tirFactor.getKey() + "**" + tirFactor.getValue());
            }
            while (triIter.hasNext()) {
                tirFactor = triIter.next();
                if (tirFactor.getValue().intValue() == 1) {
                    sbuilder.append("*" + tirFactor.getKey().toString());
                } else {
                    sbuilder.append("*" + tirFactor.getKey() + "**" + tirFactor.getValue());
                }
            }
        } else {
            varFactor = varsIter.next();
            if (varFactor.getValue().intValue() == 1) {
                sbuilder.append(varFactor.getKey().toString());
            } else {
                sbuilder.append(varFactor.getKey() + "**" + varFactor.getValue());
            }
            while (varsIter.hasNext()) {
                varFactor = varsIter.next();
                if (varFactor.getValue().intValue() == 1) {
                    sbuilder.append("*" + varFactor.getKey().toString());
                } else {
                    sbuilder.append("*" + varFactor.getKey() + "**" + varFactor.getValue());
                }
            }
            while (triIter.hasNext()) {
                tirFactor = triIter.next();
                if (tirFactor.getValue().intValue() == 1) {
                    sbuilder.append("*" + tirFactor.getKey().toString());
                } else {
                    sbuilder.append("*" + tirFactor.getKey() + "**" + tirFactor.getValue());
                }
            }
        }
        return sbuilder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vars == null) ? 0 : vars.hashCode());
        result = prime * result + ((trigos == null) ? 0 : trigos.hashCode());
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
        Term other = (Term) obj;
        if (vars == null) {
            if (other.vars != null) {
                return false;
            }
        } else if (!vars.equals(other.vars)) {
            return false;
        }
        if (trigos == null) {
            if (other.trigos != null) {
                return false;
            }
        } else if (!trigos.equals(other.trigos)) {
            return false;
        }
        return true;
    }
}