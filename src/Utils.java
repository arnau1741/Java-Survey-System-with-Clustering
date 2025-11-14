import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Utils {
    public static void mostrarMenu(inout io) throws Exception {
        io.writeln("======================================");
        io.writeln("              ENQUESTES");
        io.writeln("======================================");
        io.writeln(" 1) Crear enquesta manualment");
        io.writeln(" 2) Crear enquesta des de fitxer");
        io.writeln(" 3) Mostrar enquestes");
        io.writeln(" 4) Mostrar enquestes amb preguntes");
        io.writeln(" 5) Respon enquesta");
        io.writeln(" 6) Mostrar enquestes amb preguntes i respostes");
        io.writeln(" 7) Exportar enquesta");
        io.writeln(" 8) Importar respostes");
        /*
        io.writeln(" 9) Exportar respostes");
        io.writeln(" 10) Crear usuari");
        io.writeln(" 11) Consultar usuari");
        io.writeln(" 12) Consultar respostes"); ????
        io.writeln(" 13) Modificar enquesta");
        io.writeln(" 14) Esborrar enquesta");
         */
        io.writeln(" 0) Sortir");
        io.writeln("======================================");
        io.write("Selecciona una opció: ");
    }
}
