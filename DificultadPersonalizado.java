// Subclase de dificultad Personalizada (Aplica Herencia).
// Recibe las dimensiones y minas dinámicamente validadas desde la consola.
public class DificultadPersonalizado extends NivelDificultad {
    public DificultadPersonalizado(int filas, int columnas, int minas) {
        super("Personalizado", filas, columnas, minas, "EASY PEASY LEMON SQUEEZY", "Herido pero no derrotado");
    }
}