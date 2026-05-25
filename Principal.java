// Punto de entrada único del programa (Clase Ejecutable con el método main).
public class Principal {
    public static void main(String[] args) {
        // Instancia la clase controlador y arranca el ciclo de vida de la aplicación.
        MenuPrincipal sistema = new MenuPrincipal();
        sistema.arrancar();
    }
}