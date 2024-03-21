//import libraria principala polyglot din graalvm
import org.graalvm.polyglot.*;

import java.util.HashMap;
import java.util.LinkedList;

//clasa principala - aplicatie JAVA
class Polyglot {
    //metoda privata pentru conversie low-case -> up-case folosind functia toupper() din R
    private static String RToUpper(String token){
        //construim un context care ne permite sa folosim elemente din R
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei funcitiei R, toupper(String)
        //pentru aexecuta instructiunea I din limbajul X, folosim functia graalvm polyglot.eval("X", "I");
        Value result = polyglot.eval("R", "toupper(\""+token+"\");");
        //utilizam metoda asString() din variabila incarcata cu output-ul executiei pentru a mapa valoarea generica la un String
        String resultString = result.asString();
        // inchidem contextul Polyglot
        polyglot.close();

        return resultString;
    }

    //metoda privata pentru evaluarea unei sume de control simple a literelor unui text ASCII, folosind PYTHON
    private static int SumCRC(String token){

        // Aplicatie 1 si 2

        //construim un context care ne permite sa folosim elemente din PYTHON
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei functiei PYTHON, sum()
        //avem voie sa inlocuim anumite elemente din scriptul pe care il construim spre evaluare, aici token provine din JAVA, dar va fi interpretat de PYTHON
        Value result = polyglot.eval("python",
                        """
                        a = lambda x: x*x*x + 2*x*x + 7*x + 3
                        a(sum(ord(ch) for ch in '""" + token + "'[1:-1]))");
        //utilizam metoda asInt() din variabila incarcata cu output-ul executiei, pentru a mapa valoarea generica la un Int
        int resultInt = result.asInt();
        // inchidem contextul Polyglot
        polyglot.close();

        return resultInt;
    }

    //functia MAIN
    public static void main(String[] args) {

        HashMap<Integer, LinkedList<String>> map = new HashMap<>();
        //construim un context pentru evaluare elemente JS
        Context polyglot = Context.create();
        //construim un array de string-uri, folosind cuvinte din pagina web:  https://chrisseaton.com/truffleruby/tenthings/
        Value array = polyglot.eval("js", "[\"Kotlin\",\"is\",\"known\",\"as\",\"good\"];");
        //pentru fiecare cuvant, convertim la upcase folosind R si calculam suma de control folosind PYTHON
        String[] upper = new String[(int)array.getArraySize()];
        int[] crc = new int[(int)array.getArraySize()];
        int sum;

        for (int i = 0; i < array.getArraySize();i++) {
            String element = array.getArrayElement(i).asString();
            upper[i] = RToUpper(element);
            crc[i] = SumCRC(upper[i]);
            // System.out.println(upper[i] + " -> " + crc[i]);
        }

        // Tema 1
        for(int i = 0; i < crc.length; i++) {
            sum = crc[i];
            if (map.containsKey(sum)) {
                map.get(sum).add(upper[i]);
            } else {
                // map doesn't have this key yet
                map.put(sum, new LinkedList<>());
                map.get(sum).add(upper[i]);
            }
        }
        for (int element: map.keySet()) {
            System.out.print(element + " -> ");
            for (String e: map.get(element)) {
                System.out.print(e + ", ");
            }
            System.out.print("\b\b\n");
        }
        // inchidem contextul Polyglot
        polyglot.close();
    }
}

