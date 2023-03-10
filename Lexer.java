public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        while (pos < input.length() 
            && Character.isDigit(input.charAt(pos)) 
            && input.charAt(pos) == '0') {
            ++pos;
        }
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        if (sb.length() == 0) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * Prioritize function name then 'd' head means derivation
     * @return String
     */
    private String getVarOrFunc() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        String name = sb.toString();
        if (pos < input.length() && input.charAt(pos) == '(' && Funsave.isFunctionName(name)) {
            pos++;
            return "@" + name;
        } else if (sb.charAt(0) == 'd' && pos < input.length() && input.charAt(pos) == '(') {
            pos++;
            return "&" + name.substring(1);
        } else {
            return name;
        }
    }

    private String getTrigono() {
        if (input.charAt(pos) == 's' &&
            input.charAt(pos + 1) == 'i' &&
            input.charAt(pos + 2) == 'n' &&
            input.charAt(pos + 3) == '(') {
            pos += 4;
            return "sin(";
        }
        else if (input.charAt(pos) == 'c' &&
            input.charAt(pos + 1) == 'o' &&
            input.charAt(pos + 2) == 's' &&
            input.charAt(pos + 3) == '(') {
            pos += 4;
            return "cos(";
        } else {
            return getVarOrFunc();
        }
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (c == 's' || c == 'c') {
            curToken = getTrigono();
        } else if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (Character.isLetter(c)) {
            curToken = getVarOrFunc();
        } else if (c == '*' && input.charAt(pos + 1) == '*') {
            pos += 2;
            curToken = String.valueOf('^');
        }
        else if ("(,)+-*".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }
}
