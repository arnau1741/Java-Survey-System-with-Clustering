package prop.enquestes.persistencia;

import java.util.*;

public class JsonUtil {

    // --- Serialització ---

    /**
     * Converteix un objecte Java (String, Number, Boolean, List, Map) a una cadena JSON.
     * @param o
     * @return cadena JSON
     */
    public static String toJson(Object o) {
        if (o == null)
            return "null";
        if (o instanceof String) {
            return "\"" + escape((String) o) + "\"";
        }
        if (o instanceof Number || o instanceof Boolean) {
            return o.toString();
        }
        if (o instanceof List) {
            List<?> list = (List<?>) o;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                sb.append(toJson(list.get(i)));
                if (i < list.size() - 1)
                    sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }
        if (o instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) o;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            int i = 0;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sb.append(toJson(entry.getKey().toString())); 
                sb.append(":");
                sb.append(toJson(entry.getValue()));
                if (i < map.size() - 1)
                    sb.append(",");
                i++;
            }
            sb.append("}");
            return sb.toString();
        }
        throw new RuntimeException("Unsupported type: " + o.getClass());
    }

    /**
     * Escapa els caràcters especials en una cadena per a JSON.
     * @param s
     * @return cadena escapada
     */
    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // --- Deserialització  ---


    private static int pos;
    private static String json;

    /**
     * Converteix una cadena JSON a un objecte Java (String, Number, Boolean, List, Map).
     * @param jsonString
     * @return objecte Java
     */
    public static Object parse(String jsonString) {
        json = jsonString.trim();
        pos = 0;
        return parseValue();
    }

    /**
     * Analitza un valor JSON (objecte, array, cadena, número, boolean, null).
     * @return objecte Java
     */
    private static Object parseValue() {
        skipWhitespace();
        if (pos >= json.length())
            return null;

        char c = json.charAt(pos);
        if (c == '{')
            return parseObject();
        if (c == '[')
            return parseArray();
        if (c == '"')
            return parseString();
        if (Character.isDigit(c) || c == '-' || c == '.')
            return parseNumber();
        if (json.startsWith("true", pos)) {
            pos += 4;
            return true;
        }
        if (json.startsWith("false", pos)) {
            pos += 5;
            return false;
        }
        if (json.startsWith("null", pos)) {
            pos += 4;
            return null;
        }

        throw new RuntimeException("Unexpected char at " + pos + ": " + c);
    }

    /**
     * Analitza un objecte JSON i el converteix a un Map.
     * @return Map amb les claus i valors de l'objecte JSON
     */
    private static Map<String, Object> parseObject() {
        Map<String, Object> map = new HashMap<>();
        pos++; // skip '{'
        skipWhitespace();
        if (peek() == '}') {
            pos++;
            return map;
        }

        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            if (json.charAt(pos) != ':')
                throw new RuntimeException("Expected ':' at " + pos);
            pos++;
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            if (peek() == '}') {
                pos++;
                break;
            }
            if (json.charAt(pos) != ',')
                throw new RuntimeException("Expected ',' or '}' at " + pos);
            pos++;
        }
        return map;
    }

    /**
     * Analitza un array JSON i el converteix a una llista.
     * @return Llista amb els valors de l'array JSON
     */
    private static List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        pos++; // skip '['
        skipWhitespace();
        if (peek() == ']') {
            pos++;
            return list;
        }

        while (true) {
            Object value = parseValue();
            list.add(value);
            skipWhitespace();
            if (peek() == ']') {
                pos++;
                break;
            }
            if (json.charAt(pos) != ',')
                throw new RuntimeException("Expected ',' or ']' at " + pos);
            pos++;
        }
        return list;
    }

    /**
     * Analitza una cadena JSON i la converteix a una cadena Java.
     * @return cadena Java
     */
    private static String parseString() {
        StringBuilder sb = new StringBuilder();
        pos++; // skip opening "
        while (pos < json.length()) {
            char c = json.charAt(pos);
            if (c == '"') {
                pos++;
                return sb.toString();
            }
            if (c == '\\') {
                pos++;
                c = json.charAt(pos);
                switch (c) {
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    default:
                        sb.append(c);
                }
            } else {
                sb.append(c);
            }
            pos++;
        }
        throw new RuntimeException("Unterminated string");
    }

    /**
     * Analitza un número JSON i el converteix a un Number Java.
     * @return Number Java
     */
    private static Number parseNumber() {
        int start = pos;
        while (pos < json.length()
                && (Character.isDigit(json.charAt(pos)) || json.charAt(pos) == '-' || json.charAt(pos) == '.')) {
            pos++;
        }
        String numStr = json.substring(start, pos);
        if (numStr.contains(".")) {
            return Double.parseDouble(numStr);
        }
        return Integer.parseInt(numStr);
    }

    /**
     * Salta els caràcters d'espai en blanc.
     */
    private static void skipWhitespace() {
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
            pos++;
        }
    }

    /**
     * Retorna el caràcter actual sense avançar la posició.
     * @return caràcter actual
     */
    private static char peek() {
        if (pos < json.length())
            return json.charAt(pos);
        return 0;
    }
}
