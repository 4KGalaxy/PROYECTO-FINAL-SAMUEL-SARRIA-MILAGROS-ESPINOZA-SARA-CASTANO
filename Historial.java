import java.util.ArrayList;

// Clase que administra las partidas dinámicamente usando un ArrayList.
// Aquí se implementan los algoritmos de BubbleSort y BinarySearch.
public class Historial {
    private ArrayList<Partida> listaPartidas; // Estructura de datos dinámica.
    private boolean estaOrdenado;             // Controla si el historial fue ordenado antes de permitir búsquedas.

    public Historial() {
        this.listaPartidas = new ArrayList<>();
        this.estaOrdenado = false; // Inicia en false porque las partidas entran desordenadas por tiempo.
    }

    // Registra una nueva partida al finalizar el juego.
    public void registrarPartida(Partida p) {
        listaPartidas.add(p);
        estaOrdenado = false; // Al agregar una nueva partida al final, el historial vuelve a considerarse desordenado.
    }

    public ArrayList<Partida> getListaPartidas() { return listaPartidas; }
    public boolean isEstaOrdenado() { return estaOrdenado; }

    // ALGORITMO CLÁSICO: BubbleSort (Ordenamiento de Burbuja Manual)
    // Ordena la lista de menor a mayor basándose en la duración de tiempo en segundos.
    public void ordenarPorTiempo() {
        if (listaPartidas.isEmpty()) {
            System.out.println("El historial está vacío. No hay partidas para ordenar.");
            return;
        }

        int n = listaPartidas.size();
        // Ciclos anidados del método de la burbuja.
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compara el elemento actual con el siguiente.
                if (listaPartidas.get(j).getTiempo() > listaPartidas.get(j + 1).getTiempo()) {
                    // Intercambio clásico de posiciones en el ArrayList usando una variable temporal.
                    Partida temp = listaPartidas.get(j);
                    listaPartidas.set(j, listaPartidas.get(j + 1));
                    listaPartidas.set(j + 1, temp);
                }
            }
        }
        estaOrdenado = true; // Cambia la bandera de control a verdadero.
        System.out.println("Historial ordenado correctamente por tiempo de menor a mayor.");
    }

    // ALGORITMO CLÁSICO: BinarySearch (Búsqueda Binaria Manual)
    // Encuentra de forma ultraeficiente O(log n) las partidas que coincidan con el tiempo ingresado.
    public void buscarPorTiempo(int tiempoBuscado) {
        // Restricción del PDF: Validar que el historial haya sido ordenado primero.
        if (!estaOrdenado) {
            System.out.println("Debes ordenar el historial primero antes de buscar. Usa: &ORDENAR");
            return;
        }

        int inicio = 0;
        int fin = listaPartidas.size() - 1;
        int indiceCoincidencia = -1;

        // Bucle de división binaria.
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2; // Calcula el punto medio del segmento actual.
            int tiempoMedio = listaPartidas.get(medio).getTiempo();

            if (tiempoMedio == tiempoBuscado) {
                indiceCoincidencia = medio; // Se encontró el elemento.
                break;
            } else if (tiempoMedio < tiempoBuscado) {
                inicio = medio + 1; // Ajusta el límite inferior.
            } else {
                fin = medio - 1;    // Ajusta el límite superior.
            }
        }

        // Si terminó el bucle y sigue en -1, significa que el tiempo no existe.
        if (indiceCoincidencia == -1) {
            System.out.println("No se encontró ninguna partida con ese tiempo en el historial.");
            return;
        }

        // Como el tiempo se puede repetir en varias partidas, recolectamos todas las adyacentes al índice hallado.
        ArrayList<Partida> coincidentes = new ArrayList<>();
        coincidentes.add(listaPartidas.get(indiceCoincidencia));

        // Buscar hacia la izquierda del índice por si hay duplicados.
        int izq = indiceCoincidencia - 1;
        while (izq >= 0 && listaPartidas.get(izq).getTiempo() == tiempoBuscado) {
            coincidentes.add(listaPartidas.get(izq));
            izq--;
        }

        // Buscar hacia la derecha del índice por si hay duplicados.
        int der = indiceCoincidencia + 1;
        while (der < listaPartidas.size() && listaPartidas.get(der).getTiempo() == tiempoBuscado) {
            coincidentes.add(listaPartidas.get(der));
            der++;
        }

        // Imprime en consola los resultados encontrados.
        System.out.println("\n--- RESULTADOS DE LA BÚSQUEDA ---");
        for (Partida p : coincidentes) {
            System.out.println("FECHA: " + p.getFecha() + " | TIEMPO: " + p.getTiempo() + "s | VEREDICTO: " + p.getVeredicto() +
                            " | DIFICULTAD: " + p.getDificultad() + " | DIMENSIONES: " + p.getAncho() + "x" + p.getAltura() +
                            " | MINAS: " + p.getNumeroMinas());
        }
    }
}