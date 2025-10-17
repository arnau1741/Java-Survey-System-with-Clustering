// Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GestorUsuaris gestor = new GestorUsuaris();

        //PerfilEnquestador
        System.out.print("Introdueix id: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Introdueix nom: ");
        String nom = sc.nextLine().trim();
        System.out.print("Introdueix email: ");
        String email = sc.nextLine().trim();
        PerfilEnquestat usuari = new PerfilEnquestat(id, nom, email);
        //set especialitat
        System.out.print("Introdueix especialitat (numero): ");
        String especialitat = sc.nextLine().trim();
        usuari.setEspecialitat(especialitat);
        gestor.afegirUsuari(usuari);

        //PerfilEnquestat
        System.out.print("Introdueix id: ");
        id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Introdueix nom: ");
        nom = sc.nextLine().trim();
        System.out.print("Introdueix email: ");
        email = sc.nextLine().trim();
        PerfilEnquestat usuari2 = new PerfilEnquestat(id, nom, email);
        //set especialitat
        System.out.print("Introdueix especialitat (numero): ");
        especialitat = sc.nextLine().trim();
        usuari2.setEspecialitat(especialitat);
        gestor.afegirUsuari(usuari2);
        System.out.println("Usuari afegit:");

        gestor.llistarUsuaris();

        sc.close();
    }
}