// Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        GestorUsuaris gestor = new GestorUsuaris();

        //Import
        //Recibir un fichero (Path) -> Abrimos fichero
        //sc = new Scanner(System.in);
        //System.out.print("Introdueix la ruta del fitxer d'importació: ");
        //String rutaImport = sc.nextLine();

        //Llamar a funcion para tratar
        //Obtener datos y llamar a controlador de dominio

        //Export
        //Llamo al controlador de dominio para obtener datos
        //Abrir pipe y guardar datos en fichero
        //Guardarlo en la ruta establecida


        sc.close();
    }
}