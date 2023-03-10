package asset;

import java.math.BigInteger;

public class Num implements Factor {
    private BigInteger num;

    public Num() {
        this.num = new BigInteger("1");
    }

    public Num(String num) {
        this.num = new BigInteger(num);
    }

    public Num(BigInteger num) {
        this.num = new BigInteger(num.toString());
    }

    public Num(Num anotherNum) {
        this.num = new BigInteger(anotherNum.num.toString());
    }

    public Num multiply(Num numfcator) {
        return new Num(this.num.multiply(numfcator.num));
    }

    public Num multiply(BigInteger numBigInteger) {
        return new Num(this.num.multiply(numBigInteger));
    }

    public void add(Num numfactor) {
        this.num = this.num.add(numfactor.num);
    }

    public void sub(Num numfactor) {
        this.num = this.num.subtract(numfactor.num);
    }

    public Num pow(String indexString) {
        int index = Integer.valueOf(indexString);
        if (index == 1) {
            return new Num(this);
        } else if (index == 0) {
            return new Num();
        }
        Num powNum = new Num(this);
        if (index <= 5) {
            index--;
            while (index != 0) {
                powNum = powNum.multiply(this);
                index--;
            }
            return powNum;
        }
        Num baseNum = new Num("1");
        while (index != 0) {
            if ((index & 1) != 0) {
                baseNum.num = baseNum.num.multiply(powNum.num);
            }
            powNum.num = powNum.num.multiply(powNum.num);
            index >>= 1;
        }
        return baseNum;
    }

    public String toString() {
        return this.num.toString();
    }

    public boolean equalsStr(String num) {
        return this.num.toString().equals(num);
    }

    @Override
    public Factor Derivation(String factorString) {
        return new Num("0");
    }

    public int compareTo(String numString) {
        return this.num.compareTo(new BigInteger(numString));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((num == null) ? 0 : num.hashCode());
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
        Num other = (Num) obj;
        if (num == null) {
            if (other.num != null) {
                return false;
            }
        } else if (!num.equals(other.num)) {
            return false;
        }
        return true;
    }
}