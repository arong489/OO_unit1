import java.util.HashMap;

public class Funsave {
    private static HashMap<String, String> content = new HashMap<String, String>();
    private static HashMap<String, String[]> fparam = new HashMap<String, String[]>();

    public static void registerFunction(String input) {
        String[] strings = input.split("=",2);
        String[] header = strings[0].split("\\(",2);
        Lexer lexer = new Lexer(strings[1]);
        MyParser parser = new MyParser(lexer);
        content.put(header[0], parser.parseExpr().toString());
        fparam.put(header[0],header[1].substring(0, header[1].length() - 1).split(","));
    }

    public static String getContent(String name) {
        return content.get(name);
    }

    public static String[] getFparam(String name) {
        return fparam.get(name);
    }

    public static boolean isFunctionName(String name) {
        return content.containsKey(name);
    }
}
