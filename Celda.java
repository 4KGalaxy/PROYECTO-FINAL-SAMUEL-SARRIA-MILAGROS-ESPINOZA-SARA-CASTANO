// Clase que representa una casilla individual del tablero del Buscaminas.
// Aplica el principio de Encapsulamiento (atributos privados con métodos Getter y Setter).
public class Celda {
    private boolean esMina;            // True si la celda contiene una bomba.
    private boolean estaRevelada;      // True si el jugador ya la descubrió.
    private boolean tieneBandera;      // True si el jugador colocó una bandera (L).
    private int minasAdyacentes;       // Guarda el número de minas que tiene alrededor (0 a 8).

    // Constructor: Inicializa la celda en su estado inicial seguro y tapado.
    public Celda() {
        this.esMina = false;
        this.estaRevelada = false;
        this.tieneBandera = false;
        this.minasAdyacentes = 0;
    }

    // Métodos Getters y Setters para acceder y modificar los atributos de forma segura.
    public boolean isEsMina() { return esMina; }
    public void setEsMina(boolean esMina) { this.esMina = esMina; }

    public boolean isEstaRevelada() { return estaRevelada; }
    public void setEstaRevelada(boolean estaRevelada) { this.estaRevelada = estaRevelada; }

    public boolean isTieneBandera() { return tieneBandera; }
    public void setTieneBandera(boolean tieneBandera) { this.tieneBandera = tieneBandera; }

    public int getMinasAdyacentes() { return minasAdyacentes; }
    public void setMinasAdyacentes(int minasAdyacentes) { this.minasAdyacentes = minasAdyacentes; }
}