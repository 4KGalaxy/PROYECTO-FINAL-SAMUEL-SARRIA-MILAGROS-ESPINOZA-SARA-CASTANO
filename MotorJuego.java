import java.time.LocalDate;

// Clase cerebro encargada del flujo mecánico, renderizado del tablero y control del reloj del juego.
public class MotorJuego {
    private Tablero tablero;
    private NivelDificultad nivel;
    private Historial historial;
    private long tiempoInicial;      // Almacena la hora del sistema en milisegundos al empezar.
    private boolean juegoTerminado;

    // Constructor: Inicializa e inyecta las dependencias necesarias.
    public MotorJuego(NivelDificultad nivel, Historial historial) {
        this.nivel = nivel;
        this.historial = historial;
        this.tablero = new Tablero(nivel.getFilas(), nivel.getColumnas(), nivel.getMinas());
        this.tiempoInicial = System.currentTimeMillis(); // Inicia el cronómetro de la partida.
        this.juegoTerminado = false;
    }

    // Procesa el comando central &DESCUBRIR.
    public void descubrirCoordenada(int f, int c) {
        // Validación de coordenadas para evitar salidas de rango.
        if (f < 0 || f >= tablero.getFilas() || c < 0 || c >= tablero.getColumnas()) {
            System.out.println("Coordenadas fuera del tablero.");
            return;
        }

        Celda celda = tablero.getCelda(f, c);

        // Control de estados de la celda.
        if (celda.isEstaRevelada()) {
            System.out.println("Celda inválida. Esa celda ya fue descubierta.");
            return;
        }
        if (celda.isTieneBandera()) {
            System.out.println("Celda inválida. Esa celda tiene una bandera. Usa &QUITARBANDERA antes de descubrirla.");
            return;
        }

        // Si el jugador pisa una mina, el juego finaliza inmediatamente en Derrota.
        if (celda.isEsMina()) {
            finalizarPartida(false);
            return;
        }

        // Si es segura, activa el algoritmo recursivo de cascada.
        ejecutarFlujoCascada(f, c);

        // Revisa después del movimiento si ya se limpiaron todas las casillas seguras.
        if (verificarVictoria()) {
            finalizarPartida(true);
        }
    }

    // ALGORITMO RECURSIVO: Flood Fill (Descubrimiento en Cascada)
    // Revela de forma automática sectores vacíos contiguos usando recursión.
    private void ejecutarFlujoCascada(int f, int c) {
        // Caso Base 1: Límites del tablero desbordados.
        if (f < 0 || f >= tablero.getFilas() || c < 0 || c >= tablero.getColumnas()) return;
        
        Celda celda = tablero.getCelda(f, c);
        // Caso Base 2: Si ya está abierta o tiene bandera, frena la recursión.
        if (celda.isEstaRevelada() || celda.isTieneBandera()) return;

        // Paso Recursivo: Revela la celda actual.
        celda.setEstaRevelada(true);

        // Si la celda actual tiene 0 minas a su alrededor, propaga la cascada a sus 8 vecinas.
        if (celda.getMinasAdyacentes() == 0 && !celda.isEsMina()) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    ejecutarFlujoCascada(f + x, c + y); // Llamada recursiva automática.
                }
            }
        }
    }

    // Coloca una bandera (L) para bloquear descubrimientos accidentales.
    public void ponerBandera(int f, int c) {
        if (f < 0 || f >= tablero.getFilas() || c < 0 || c >= tablero.getColumnas()) return;
        Celda celda = tablero.getCelda(f, c);
        if (celda.isEstaRevelada()) {
            System.out.println("Celda inválida. No puedes poner una bandera en una celda ya descubierta.");
            return;
        }
        if (celda.isTieneBandera()) {
            System.out.println("Celda inválida. Esa celda ya tiene una bandera.");
            return;
        }
        celda.setTieneBandera(true);
    }

    // Quita la bandera volviendo a habilitar la casilla.
    public void quitarBandera(int f, int c) {
        if (f < 0 || f >= tablero.getFilas() || c < 0 || c >= tablero.getColumnas()) return;
        Celda celda = tablero.getCelda(f, c);
        if (!celda.isTieneBandera()) {
            System.out.println("Celda inválida. Esa celda no tiene una bandera.");
            return;
        }
        celda.setTieneBandera(false);
    }

    // Condición de Victoria: Retorna true si todas las celdas que NO son minas han sido reveladas.
    private boolean verificarVictoria() {
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Celda celda = tablero.getCelda(i, j);
                if (!celda.isEstaRevelada() && !celda.isEsMina()) {
                    return false; // Quedan casillas seguras tapadas.
                }
            }
        }
        return true;
    }

    // Dibuja el tablero simétricamente en consola con formateo de 3 caracteres por ancho fijo de columna.
    public void imprimirTablero(boolean mostrarTodo) {
        System.out.println();
        System.out.print("     ");
        // Imprime el índice de las columnas formateado a dos dígitos (00, 01, 02...).
        for (int j = 0; j < tablero.getColumnas(); j++) {
            System.out.printf("%02d ", j);
        }
        System.out.println();
        
        System.out.print("     ");
        for (int j = 0; j < tablero.getColumnas(); j++) {
            System.out.print("---");
        }
        System.out.println();

        // Imprime las filas con su respectivo índice izquierdo.
        for (int i = 0; i < tablero.getFilas(); i++) {
            System.out.printf("%02d | ", i);
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Celda celda = tablero.getCelda(i, j);

                // Control de visualización según las reglas del PDF.
                if (mostrarTodo && celda.isEsMina()) {
                    System.out.print(" X "); // Revela las minas al perder.
                } else if (celda.isTieneBandera()) {
                    System.out.print(" L "); // Dibuja la bandera.
                } else if (!celda.isEstaRevelada()) {
                    System.out.print(" 0 "); // Celda tapada estándar.
                } else if (celda.getMinasAdyacentes() == 0) {
                    System.out.print("   "); // Espacio vacío si no tiene minas adyacentes.
                } else {
                    System.out.print(" " + celda.getMinasAdyacentes() + " "); // Imprime el número de proximidad.
                }
            }
            System.out.println();
        }
    }

    // Detiene el tiempo, procesa el desenlace del juego e inyecta la partida al historial.
    private void finalizarPartida(boolean victoria) {
        juegoTerminado = true;
        long tiempoFinal = System.currentTimeMillis();
        int duracionSegundos = (int) ((tiempoFinal - tiempoInicial) / 1000); // Convierte milisegundos a segundos netos.

        imprimirTablero(true); // Revela todo el tablero final (con las minas expuestas).

        String resultadoText = victoria ? "Victoria" : "Derrota";
        String fraseNivel = victoria ? nivel.getFraseVictoria() : nivel.getFraseDerrota();

        // Despliega el resumen final requerido por el enunciado.
        System.out.println("\n=================================");
        System.out.println("       RESUMEN DE PARTIDA        ");
        System.out.println("=================================");
        System.out.println("RESULTADO: " + resultadoText);
        System.out.println("DIFICULTAD: " + nivel.getNombre());
        System.out.println("DIMENSIONES: " + tablero.getColumnas() + " x " + tablero.getFilas());
        System.out.println("MINAS: " + nivel.getMinas());
        System.out.println("TIEMPO: " + duracionSegundos + "s");
        System.out.println("FECHA: " + LocalDate.now());
        System.out.println("[" + fraseNivel + "]");
        System.out.println("=================================\n");

        // Guarda el registro en memoria dinámica inyectándolo al objeto historial.
        Partida partidaJugada = new Partida(
            LocalDate.now().toString(),
            duracionSegundos,
            resultadoText,
            nivel.getNombre(),
            tablero.getColumnas(),
            tablero.getFilas(),
            nivel.getMinas()
        );
        historial.registrarPartida(partidaJugada);
    }

    public boolean isJuegoTerminado() { return juegoTerminado; }
}