package asset;

public class Tirgono implements Factor {
    private String prefixName;
    private Expr content;

    public Tirgono(String prefixName, Expr content) {
        this.prefixName = prefixName.replaceAll("\\(", "");
        this.content = content;
    }

    @Override
    public Factor Derivation(String factorString) {
        Term conver = new Term(new Tirgono(prefixName.equals("sin") ? "cos" : "sin", content));
        if (prefixName.equals("cos")) {
            conver = conver.multiply(new Num("-1"));
        }
        Expr expr = (Expr) content.Derivation(factorString);
        return expr.multiply(conver);
    }

    public Factor simplify() {
        if (content.toString().equals("0")) {
            return new Num(prefixName.equals("sin") ? "0" : "1");
        } else {
            return this;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((prefixName == null) ? 0 : prefixName.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    /**
     * to check if the head term coefficient is minus
     * if it is, replace content
     * @return boolean
     */
    public boolean minusHead() {
        if (this.content.getTerms().values().iterator().next().compareTo("0") < 0) {
            this.content = this.content.multiply(new Num("-1"));
            return prefixName.equals("sin");
        } else {
            return false;
        }
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
        Tirgono other = (Tirgono) obj;
        if (prefixName == null) {
            if (other.prefixName != null) {
                return false;
            }
        } else if (!prefixName.equals(other.prefixName)) {
            return false;
        }
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (this.content.isPowFactor()) {
            return this.prefixName + "(" + this.content + ")";
        } else {
            return this.prefixName + "((" + this.content + "))";
        }
    }
}
