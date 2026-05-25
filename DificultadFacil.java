// Subclase que hereda de NivelDificultad (Aplica Herencia).
public class DificultadFacil extends NivelDificultad {
    public DificultadFacil() {
        // Llama al constructor de la clase padre (super) pasándole los parámetros rígidos del PDF (El que contiene la logica ).
        super("Fácil", 8, 8, 10, "BUEN COMIENZO", "Que lastima");
    }
}