import java.util.Scanner;

// Clase encargada de leer los comandos del usuario y controlar el menú raíz de la aplicación.
public class MenuPrincipal {
    private Historial historial;
    private Scanner sc;

    public MenuPrincipal() {
        this.historial = new Historial();
        this.sc = new Scanner(System.in);
    }

    // Bucle de control del menú principal del juego.
    public void arrancar() {
        System.out.println("BIENVENIDO AL MEJOR BUSCAMINAS!!!");
        mostrarInstruccionesIniciales();

        while (true) {
            System.out.print("\nEscriba el comando que desea implementar: ");
            String entrada = sc.nextLine().trim();

            // Filtro de excepciones manuales de entrada por consola.
            if (entrada.isEmpty()) {
                System.out.println("Entrada vacía. Por favor escribe un comando.");
                continue;
            }
            if (!entrada.startsWith("&")) {
                System.out.println("Entrada inválida. Todos los comandos deben iniciar con &.");
                continue;
            }

            // Expresión regular \\s+ para dividir la entrada sin importar si el usuario metió múltiples espacios.
            String[] partes = entrada.split("\\s+");
            String comando = partes[0];

            // Validación rigurosa de Mayúsculas exigida por el PDF.
            if (!comando.equals(comando.toUpperCase())) {
                System.out.println("Comando no reconocido. Los comandos deben escribirse en MAYÚSCULAS.");
                continue;
            }

            // Enrutador de comandos.
            if (comando.equals("&CERRAR")) {
                System.out.println("Saliendo del sistema de juego. ¡Hasta pronto!");
                break;
            } else if (comando.equals("&HISTORIAL")) {
                desplegarHistorialCompleto();
            } else if (comando.equals("&ORDENAR")) {
                historial.ordenarPorTiempo();
            } else if (comando.equals("&BUSCAR")) {
                if (partes.length < 2) {
                    System.out.println("Valor faltante. Uso correcto: &BUSCAR [tiempo en segundos]. Ejemplo: &BUSCAR 278");
                    continue;
                }
                try {
                    int t = Integer.parseInt(partes[1]);
                    historial.buscarPorTiempo(t);
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido. El tiempo debe ser un número entero. Ejemplo: &BUSCAR 278");
                }
            } else if (comando.equals("&NUEVAPARTIDA")) {
                ejecutarFlujoDificultades();
            } else if (comando.startsWith("&DIFICULTAD")) {
                procesarComandoDificultadDirecto(comando);
            } else {
                System.out.println("Comando no reconocido o inválido en este contexto. Estás en el menú principal.");
            }
        }
    }

    private void mostrarInstruccionesIniciales() {
        System.out.println("--- MECANICAS BASICAS DE BUSCAMINAS ---");
        System.out.println("1. Descubre celdas seguras intentando evitar detonar las minas (X).");
        System.out.println("2. Los números indican cuántas minas se encuentran en los 8 vecinos inmediatos.");
        System.out.println("3. Usa banderas (L) sobre las coordenadas sospechosas para protegerlas.");
        System.out.println("\nComandos Disponibles en Menú: &NUEVAPARTIDA, &HISTORIAL, &ORDENAR, &BUSCAR [t], &CERRAR");
    }

    private void desplegarHistorialCompleto() {
        if (historial.getListaPartidas().isEmpty()) {
            System.out.println("El historial está vacío. Juega una partida primero.");
            return;
        }
        System.out.println("\n--- HISTORIAL DE PARTIDAS DE LA SESIÓN ---");
        for (Partida p : historial.getListaPartidas()) {
            System.out.println("FECHA: " + p.getFecha() + " | TIEMPO: " + p.getTiempo() + "s | VEREDICTO: " + p.getVeredicto() +
                            " | DIFICULTAD: " + p.getDificultad() + " | DIMENSIONES (ANCHO x ALTURA): " + p.getAncho() + " x " + p.getAltura() +
                            " | MINAS: " + p.getNumeroMinas());
        }
    }

    private void ejecutarFlujoDificultades() {
        System.out.println("\n---- MENÚ DE DIFICULTADES ----");
        System.out.println(" - &DIFICULTADFACIL        : Tablero 8x8 con 10 minas.");
        System.out.println(" - &DIFICULTADMEDIO        : Tablero 18x18 con 40 minas.");
        System.out.println(" - &DIFICULTADDIFICIL      : Tablero 24x24 con 99 minas.");
        System.out.println(" - &DIFICULTADIMPASABLE    : Tablero 24x32 con 248 minas.");
        System.out.println(" - &DIFICULTADPERSONALIZADO: Configuración libre de celdas.");
    }

    // Utiliza Polimorfismo al asignar cualquier instancia de subclase a la variable de tipo padre NivelDificultad.
    private void procesarComandoDificultadDirecto(String cmd) {
        NivelDificultad nivelSeleccionado = null;

        if (cmd.equals("&DIFICULTADFACIL")) nivelSeleccionado = new DificultadFacil();
        else if (cmd.equals("&DIFICULTADMEDIO")) nivelSeleccionado = new DificultadMedio();
        else if (cmd.equals("&DIFICULTADDIFICIL")) nivelSeleccionado = new DificultadDificil();
        else if (cmd.equals("&DIFICULTADIMPASABLE")) nivelSeleccionado = new DificultadImpasable();
        else if (cmd.equals("&DIFICULTADPERSONALIZADO")) {
            nivelSeleccionado = configurarModoPersonalizado();
        }

        if (nivelSeleccionado != null) {
            MotorJuego partidaActual = new MotorJuego(nivelSeleccionado, historial);
            correrBuclePartida(partidaActual);
        }
    }

    // Gestiona la lógica del modo personalizado aplicando los límites matemáticos estrictos del profesor.
    private NivelDificultad configurarModoPersonalizado() {
        System.out.println("Escribe en el siguiente orden: altura, ancho y número de minas, separados por espacio. Ejemplo: 13 12 8");
        String lectura = sc.nextLine().trim();
        String[] valores = lectura.split("\\s+");

        // Control de errores de cantidad de argumentos ingresados.
        if (valores.length < 3 || (valores.length == 1 && valores[0].isEmpty())) {
            System.out.println("Valores insuficientes. Debes ingresar exactamente tres números: altura, ancho y minas. Ejemplo: 13 12 8");
            return null;
        }
        if (valores.length > 3) {
            System.out.println("Demasiados valores. Debes ingresar exactamente tres números: altura, ancho y minas. Ejemplo: 13 12 8");
            return null;
        }

        try {
            int alt = Integer.parseInt(valores[0]);  // Fila
            int anc = Integer.parseInt(valores[1]);  // Columna
            int min = Integer.parseInt(valores[2]);  // Minas

            // Validaciones límites del PDF: Altura 8-24, Ancho 8-32, Minas >= 1
            if (alt < 8 || alt > 24) {
                System.out.println("Altura inválida. El valor debe estar entre 8 y 24. Ejemplo: 13 12 8");
                return null;
            }
            if (anc < 8 || anc > 32) {
                System.out.println("Ancho inválido. El valor debe estar entre 8 y 32. Ejemplo: 13 12 8");
                return null;
            }
            if (min < 1) {
                System.out.println("Número de minas inválido. Debe ser al menos 1.");
                return null;
            }
            
            // Regla matemática obligatoria: Las minas no pueden exceder 1/3 del total de celdas del tablero.
            int maxCeldasUnTercio = (alt * anc) / 3;
            if (min > maxCeldasUnTercio) {
                System.out.println("Número de minas inválido. No puede superar el tercio del total de celdas (" + maxCeldasUnTercio + " minas máximo para un tablero de " + anc + "x" + alt + ").");
                return null;
            }

            return new DificultadPersonalizado(alt, anc, min);

        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos. Debes ingresar números enteros, sin decimales. Ejemplo: 13 12 8");
            return null;
        }
    }

    // Bucle interno secundario cuando se está jugando la partida activa en tiempo real.
    private void correrBuclePartida(MotorJuego juego) {
        System.out.println("\n================ Partida Iniciada ================");
        System.out.println("Comandos de Juego: &DESCUBRIR f c | &PONERBANDERA f c | &QUITARBANDERA f c | &SALIR");

        while (!juego.isJuegoTerminado()) {
            juego.imprimirTablero(false); // Imprime el tablero con las celdas tapadas correspondientes.
            System.out.print("\nIngrese acción dentro de partida: ");
            String ingreso = sc.nextLine().trim();

            if (ingreso.isEmpty()) continue;

            if (!ingreso.startsWith("&")) {
                System.out.println("Entrada inválida. Todos los comandos deben iniciar con &.");
                continue;
            }

            String[] args = ingreso.split("\\s+");
            String comAct = args[0];

            // Comando voluntario para abandonar.
            if (comAct.equals("&SALIR")) {
                System.out.println("Abandonando la partida actual y regresando al menú principal.");
                break;
            }

            // Acciones válidas sobre coordenadas.
            if (comAct.equals("&DESCUBRIR") || comAct.equals("&PONERBANDERA") || comAct.equals("&QUITARBANDERA")) {
                if (args.length < 3) {
                    System.out.println("Coordenada faltante. Debes indicar fila y columna. Ejemplo: " + comAct + " 3 4");
                    continue;
                }
                try {
                    int f = Integer.parseInt(args[1]);
                    int c = Integer.parseInt(args[2]);

                    // Redirecciona al método del motor de juego respectivo.
                    if (comAct.equals("&DESCUBRIR")) juego.descubrirCoordenada(f, c);
                    else if (comAct.equals("&PONERBANDERA")) juego.ponerBandera(f, c);
                    else juego.quitarBandera(f, c);

                } catch (NumberFormatException e) {
                    System.out.println("Valores inválidos. Las coordenadas deben ser números enteros. Ejemplo: " + comAct + " 3 4");
                }
            } else {
                System.out.println("Comando inválido en este contexto. Estás dentro de una partida.");
            }
        }
    }
}