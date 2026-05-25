import java.util.Random;

// Clase encargada de la lógica estructural del juego.
// Modela la matriz bidimensional estática exigida por el profesor.
public class Tablero {
    private final int filas;
    private final int columnas;
    private final int totalMinas;
    private final Celda[][] matriz; // Uso de arreglo bidimensional estático Celda[][] por eficiencia O(1)

    // Constructor: Crea la matriz, inicializa las celdas y distribuye las minas.
    public Tablero(int filas, int columnas, int totalMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.totalMinas = totalMinas;
        this.matriz = new Celda[filas][columnas];

        inicializarCeldas();       // Instancia cada objeto Celda en la matriz.
        colocarMinasAleatorias();  // Distribuye las minas usando números al azar.
        calcularMinasAdyacentes(); // Calcula los números de proximidad de minas de cada celda.
    }

    // Llena la matriz con objetos del tipo Celda para evitar errores de NullPointerException.
    private void inicializarCeldas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new Celda();
            }
        }
    }

    // Coloca las minas de forma aleatoria usando la clase Random.
    private void colocarMinasAleatorias() {
        Random rand = new Random();
        int minasColocadas = 0;

        // Bucle que asegura colocar la cantidad exacta de minas sin repetir casillas.
        while (minasColocadas < totalMinas) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);

            // Si la celda elegida al azar no es ya una mina, la convertimos en mina.
            if (!matriz[f][c].isEsMina()) {
                matriz[f][c].setEsMina(true);
                minasColocadas++;
            }
        }
    }

    // Algoritmo de vecindad: Revisa las 8 casillas adyacentes de cada celda para contar cuántas minas tiene cerca.
    private void calcularMinasAdyacentes() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Si la celda actual es una mina, no necesita calcular número.
                if (matriz[i][j].isEsMina()) continue;

                int contador = 0;
                // Bucles anidados de -1 a 1 para recorrer la matriz de 3x3 que rodea a la celda actual.
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        int vecinaFila = i + x;
                        int vecinaCol = j + y;

                        // Control de desbordamiento: Verifica que la vecina esté dentro de los límites del tablero.
                        if (vecinaFila >= 0 && vecinaFila < filas && vecinaCol >= 0 && vecinaCol < columnas) {
                            if (matriz[vecinaFila][vecinaCol].isEsMina()) {
                                contador++;
                            }
                        }
                    }
                }
                matriz[i][j].setMinasAdyacentes(contador);
            }
        }
    }

    // Métodos Getter de acceso.
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public Celda getCelda(int f, int c) { return matriz[f][c]; }
}