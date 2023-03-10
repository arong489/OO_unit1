package asset;

public class Var implements Factor {
    private String varName;

    public Var(String varName) {
        this.varName = varName;
    }

    @Override
    public int hashCode() {
        return ((varName == null) ? 0 : varName.hashCode());
    }

    /**
     * @return Num one or zero
     */
    @Override
    public Factor Derivation(String factorString) {
        return new Num(this.varName.equals(factorString) ? "1" : "0");
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
        Var other = (Var) obj;
        if (varName == null) {
            if (other.varName != null) {
                return false;
            }
        } else if (!varName.equals(other.varName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return varName;
    }
}
