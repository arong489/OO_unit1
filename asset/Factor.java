package asset;

public interface Factor {
    public String toString();

    public int hashCode();

    public Factor Derivation(String factorString);
}
