import org.graalvm.polyglot.*;

import java.io.*;
import java.util.*;

class Polyglot {
    private static Integer[] randomList(){
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        Integer[] a = new Integer[20];
        for (int i = 0; i < 20; i++) {
            a[i] = polyglot.eval("python", "import random; int(random.random() * 10 + 10)").asInt();
        }
        polyglot.close();
        return a;
    }

    private static void printJS(Integer[] vect) {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        polyglot.eval("js", "console.log('" + Arrays.toString(vect) + "')");
        polyglot.close();
    }

    private static Double changeListR(Integer[] vect) {
        double d;
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        StringBuilder v = new StringBuilder();
        for (int i = 0; i < vect.length; i++) {
            v.append(vect[i]);
            if (i != vect.length - 1) {
                v.append(",");
            }
        }

        Value result = polyglot.eval("R", "v <- c(" + v + ");" + """
        v <- v[order(v)];
        v <- v[ceiling(length(v) * 0.2):(floor(length(v) * 0.8))];
        mean(v)
        """
        );
        d = result.asDouble();
        polyglot.close();
        return d;
    }

    private static void regressionR(String file, String image_name, String output_path, String color) {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        String bigR = String.format("""
                dataset <- read.csv("%s")
                linear_model <- lm(y ~ x, data = dataset)
                
                output_file <- "%s";
                output_path <- "%s";
                library(lattice);
                png(file.path(output_path, paste0(output_file, ".png")))
                plot(dataset$x, dataset$y, main = "Linear Regression", xlab = "x", ylab = "y")
                abline(linear_model, col = "%s")
                dev.off()  # Inchidem thread-ul de imagini
                
                system(paste("open", file.path(output_path, paste0(output_file, ".png"))))
                """, file, image_name, output_path, color);
        polyglot.eval("R", bigR);
        polyglot.close();
    }

    private static void binomial() {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        int aruncari = polyglot.eval("python", "int(input('Dati nr. de aruncari a unei monede: '))").asInt();
        int x = polyglot.eval("python", "int(input('Dati nr. de pajure masurate: '))").asInt();
        double prob = polyglot.eval("R", String.format(
                """
                n <- %d
                k <- %d
                prob <- pbinom(k - 1, n, 0.5, lower.tail = FALSE)
        """, aruncari, x)).asDouble();
        polyglot.close();
        System.out.println("Probabilitatea de a obține cel puțin " + x + " pajure din " + aruncari + " aruncări este: " + prob);
    }

    public static void main(String[] args) throws IOException {
//      Aplicatie 3
//        Integer[] vect = randomList();
//        printJS(vect);
//        Double media = changeListR(vect);
//        System.out.println("Media a 60% din numere: " + media);

        // Tema 2
//        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Dati numele imaginii fara extensie: ");
//        String img_name = b.readLine();
//        System.out.println("Dati calea de salvare: ");
//        String img_path = b.readLine();
//        System.out.println("Dati culoarea regresiei: ");
//        String color = b.readLine();
//        regressionR("/home/student/PP/lab2/myGraal/src/dataset.txt", img_name, img_path, color);
//
        // Tema 3
        binomial();
    }
}