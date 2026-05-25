# BUSCAMINAS EN LA TERMINAL DE JAVA

**Materia:** PENSAMIENTO COMPUTACIONAL  
**Integrantes:** Samuel Sarria · Milagros Espinosa · Sara Castaño

---

## Descripción

Implementación del juego clásico Buscaminas que se ejecuta completamente en la terminal de Java. 
El jugador debe descubrir todas las celdas seguras de un tablero sin detonar ninguna mina, usando comandos de texto. 
El proyecto aplica principios de programación orientada a objetos, estructuras de datos estáticas y dinámicas, y algoritmos clásicos de ordenamiento y búsqueda implementados manualmente.

---

## Requisitos

- Java JDK 8 o superior instalado (Versión de Java mínima para que pueda correr el programa sin problemas)
- Terminal o consola del sistema operativo

---

## Cómo compilar

Desde la terminal manualmente, ubicarse en la carpeta donde están los archivos `.java` y ejecutar:

```bash
javac *.java
```

---

Sin embargo, se recomienda usar VS code para mayor comodidad.

## Cómo ejecutar

Una vez compilado, ejecutar:

```bash
java Principal
```

---

## Comandos disponibles

### Desde el menú principal

| Comando | Descripción |
|---|---|
| `&NUEVAPARTIDA` | Inicia el flujo de selección de dificultad |
| `&DIFICULTADFACIL` | Partida en nivel Fácil (8×8, 10 minas) |
| `&DIFICULTADMEDIO` | Partida en nivel Medio (18×18, 40 minas) |
| `&DIFICULTADDIFICIL` | Partida en nivel Difícil (24×24, 99 minas) |
| `&DIFICULTADIMPASABLE` | Partida en nivel Impasable (24×32, 248 minas) |
| `&DIFICULTADPERSONALIZADO` | Configuración libre de dimensiones y minas |
| `&HISTORIAL` | Muestra el historial de partidas de la sesión |
| `&ORDENAR` | Ordena el historial de menor a mayor tiempo (BubbleSort) |
| `&BUSCAR [tiempo]` | Busca partidas por tiempo en segundos (BinarySearch) |
| `&CERRAR` | Cierra el programa |

### Durante una partida

| Comando | Descripción |
|---|---|
| `&DESCUBRIR [fila] [columna]` | Descubre la celda indicada |
| `&PONERBANDERA [fila] [columna]` | Coloca una bandera en la celda |
| `&QUITARBANDERA [fila] [columna]` | Retira la bandera de la celda |
| `&SALIR` | Abandona la partida y regresa al menú |

---

## Estructura del proyecto

```
├── Celda.java
├── Tablero.java
├── NivelDificultad.java
├── DificultadFacil.java
├── DificultadMedio.java
├── DificultadDificil.java
├── DificultadImpasable.java
├── DificultadPersonalizado.java
├── Partida.java
├── Historial.java
├── MotorJuego.java
├── MenuPrincipal.java
├── Principal.java
├── README.md
└── diseno.md
```
