// Clase POO que modela los datos de una partida finalizada para ser guardada en el historial.
public class Partida {
    private String fecha;            // Fecha de ejecución (Formato AAAA-MM-DD).
    private int tiempoSegundos;      // Duración de la partida calculada en segundos.
    private String veredicto;        // Guardará si fue "Victoria" o "Derrota".
    private String dificultad;       // Nombre de la dificultad jugada.
    private int ancho;               // Columnas del tablero.
    private int altura;              // Filas del tablero.
    private int numeroMinas;         // Cantidad de minas configuradas.

    // Constructor completo para instanciar el registro de la partida.
    public Partida(String fecha, int tiempoSegundos, String veredicto, String dificultad, int ancho, int altura, int numeroMinas) {
        this.fecha = fecha;
        this.tiempoSegundos = tiempoSegundos;
        this.veredicto = veredicto;
        this.dificultad = dificultad;
        this.ancho = ancho;
        this.altura = altura;
        this.numeroMinas = numeroMinas;
    }

    // Getters necesarios para que los algoritmos de ordenamiento e historial puedan leer la información.
    public int getTiempo() { return tiempoSegundos; }
    public String getFecha() { return fecha; }
    public String getVeredicto() { return veredicto; }
    public String getDificultad() { return dificultad; }
    public int getAncho() { return ancho; }
    public int getAltura() { return altura; }
    public int getNumeroMinas() { return numeroMinas; }
}