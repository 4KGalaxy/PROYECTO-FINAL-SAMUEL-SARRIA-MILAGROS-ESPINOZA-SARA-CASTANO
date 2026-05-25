// Clase base de la jerarquía de Herencia. Modela las propiedades comunes de los niveles.
public class NivelDificultad {
    protected String nombre;
    protected int filas;
    protected int columnas;
    protected int minas;
    protected String fraseVictoria; // Exigido por el PDF según el nivel.
    protected String fraseDerrota;  // Exigido por el PDF según el nivel.

    // Constructor general de la dificultad.
    public NivelDificultad(String nombre, int filas, int columnas, int minas, String fraseVictoria, String fraseDerrota) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.minas = minas;
        this.fraseVictoria = fraseVictoria;
        this.fraseDerrota = fraseDerrota;
    }

    // Getters de acceso.
    public String getNombre() { return nombre; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int getMinas() { return minas; }
    public String getFraseVictoria() { return fraseVictoria; }
    public String getFraseDerrota() { return fraseDerrota; }
}