# Documento de Diseño — Buscaminas en la Terminal de Java

---

## Diagrama de clases

![Diagrama de Clases](DIAGRAMA_DE_CLASES_BUSCAMINAS_V2.jpg)

---

## 1. Descomposición del problema

El problema central es implementar un juego de Buscaminas funcional que opere
exclusivamente en la terminal. Para abordarlo, se descompuso en cinco subproblemas
independientes:

**Representación del tablero:** El juego necesita una estructura que modele cada celda
del tablero con su estado individual. Esto se resolvió con la clase `Celda`, que encapsula
si una celda es mina, si fue revelada, si tiene bandera y cuántas minas tiene adyacentes.
El tablero completo se modela en la clase `Tablero` como una matriz bidimensional
estática `Celda[][]`.

**Lógica del juego:** Se necesita un motor que procese las acciones del jugador, controle
el tiempo, detecte victoria o derrota y registre el resultado. Esto se delegó a la clase
`MotorJuego`.

**Niveles de dificultad:** El juego ofrece cuatro niveles predefinidos y un modo
personalizado. Cada nivel define dimensiones y cantidad de minas. Esto se modeló como
una jerarquía de clases encabezada por `NivelDificultad`.

**Historial de partidas:** Al finalizar cada partida se registra su resultado en memoria.
El historial debe poder ordenarse y consultarse. Esto se resolvió con las clases
`Historial` y `Partida`, aplicando BubbleSort y BinarySearch manualmente.

**Interfaz de comandos:** El jugador interactúa con el juego mediante comandos de
texto prefijados con `&`. El control del flujo de menús y la lectura de entradas se
concentró en `MenuPrincipal`.

---

## 2. Decisiones de diseño orientado a objetos

### Encapsulamiento

Todos los atributos de todas las clases son privados (`private`). El acceso y la
modificación se realiza exclusivamente a través de métodos públicos getters y setters.
Por ejemplo, en `Celda` los atributos `esMina`, `estaRevelada`, `tieneBandera` y
`minasAdyacentes` son privados, y `MotorJuego` los consulta y modifica únicamente
a través de sus métodos públicos como `isEsMina()`, `setEstaRevelada()` y
`getMinasAdyacentes()`. Esto garantiza la integridad del estado del juego.

### Herencia

Se aplicó herencia en la jerarquía de niveles de dificultad. La clase base
`NivelDificultad` define los atributos comunes a todos los niveles: `nombre`, `filas`,
`columnas`, `minas`, `fraseVictoria` y `fraseDerrota`. Las cinco subclases
(`DificultadFacil`, `DificultadMedio`, `DificultadDificil`, `DificultadImpasable` y
`DificultadPersonalizado`) heredan de ella y simplemente invocan al constructor padre
con sus valores específicos.

Se eligió herencia aquí y no composición porque la relación entre un nivel de dificultad
y sus subtipos es una relación **es-un**: una `DificultadFacil` **es un** `NivelDificultad`.
No tiene sentido modelarlo como composición porque una dificultad fácil no *tiene* un
nivel de dificultad, sino que *es* uno. Adicionalmente, el polimorfismo resultante permite
que `MotorJuego` y `MenuPrincipal` trabajen con cualquier subclase a través de la
referencia de tipo `NivelDificultad`, sin necesidad de conocer el subtipo concreto.

### Composición

Se aplicó composición en dos relaciones:

`MenuPrincipal` compone a `Historial`: el historial es creado y poseído por
`MenuPrincipal`. Si el menú deja de existir, el historial también. No tiene sentido que
el historial exista de forma independiente fuera del ciclo de vida de la sesión de juego.

`Tablero` compone a `Celda[][]`: el tablero crea y posee todas sus celdas. Una celda
no existe por fuera del tablero que la contiene. Esta es la relación de composición más
clara del sistema.

### Agregación

Se aplicó agregación en la relación entre `Historial` y `Partida`. El historial agrega
referencias a objetos `Partida` que fueron creados por `MotorJuego` al finalizar cada
partida. Las partidas son creadas externamente y entregadas al historial mediante el
método `registrarPartida()`. Si el historial desapareciera, los objetos `Partida` podrían
seguir existiendo conceptualmente, lo que caracteriza una agregación y no una
composición.

---

## 3. Justificación de relaciones entre clases

| Relación | Tipo | Justificación |
|---|---|---|
| `Tablero` → `Celda` | Composición | El tablero crea y destruye sus celdas. Sin tablero no hay celda. |
| `MenuPrincipal` → `Historial` | Composición | El historial vive dentro del ciclo de vida del menú. |
| `Historial` → `Partida` | Agregación | Las partidas son creadas externamente e inyectadas al historial. |
| `MotorJuego` → `NivelDificultad` | Dependencia | El motor recibe el nivel por inyección en el constructor, no lo crea. |
| `MotorJuego` → `Tablero` | Composición | El motor crea el tablero al iniciar la partida y lo destruye al terminar. |
| `Principal` → `MenuPrincipal` | Dependencia | `Principal` solo instancia y arranca el menú, no lo posee. |
| Subclases → `NivelDificultad` | Herencia | Relación es-un. Cada dificultad es un tipo específico de nivel. |

---

## 4. Estructuras de datos

### Arreglo bidimensional estático `Celda[][]`

Para el tablero se eligió un arreglo bidimensional estático en lugar de
`ArrayList<ArrayList<Celda>>`. La justificación es que las dimensiones del tablero se
definen una única vez al iniciar la partida y no cambian durante el juego. El Buscaminas
no añade ni elimina celdas en medio de una partida. Por lo tanto, la flexibilidad de un
`ArrayList` es innecesaria y añadiría complejidad sintáctica con el doble `.get().get()`.
El arreglo estático garantiza acceso directo a cualquier coordenada en tiempo constante
O(1), lo cual es crítico en los niveles más grandes como Impasable (24×32 = 768 celdas).

### ArrayList dinámico `ArrayList<Partida>`

Para el historial se eligió `ArrayList<Partida>` porque la cantidad de partidas jugadas
en una sesión es completamente variable e impredecible en tiempo de compilación. A
diferencia del tablero, el historial crece dinámicamente con cada partida jugada. El
`ArrayList` permite agregar elementos al final en tiempo amortizado O(1) y acceder a
cualquier elemento por índice en O(1), lo que lo hace compatible con los algoritmos de
BubbleSort y BinarySearch implementados manualmente.

---

## 5. Algoritmos implementados

### BubbleSort

Se implementó manualmente en `Historial.ordenarPorTiempo()`. El algoritmo recorre
la lista con dos ciclos anidados, comparando pares adyacentes mediante
`getTiempo()` e intercambiándolos con `.get()` y `.set()` si están en orden incorrecto.
El resultado es la lista ordenada de menor a mayor tiempo. La complejidad es O(n²).

### BinarySearch con expansión lateral

Se implementó en `Historial.buscarPorTiempo()`. Requiere que el historial esté
previamente ordenado. El algoritmo divide repetidamente el rango de búsqueda a la
mitad hasta encontrar una coincidencia. Dado que pueden existir múltiples partidas con
el mismo tiempo, al encontrar el índice central se activan dos exploradores que se
expanden hacia la izquierda y hacia la derecha para recolectar todos los duplicados.
La complejidad de la búsqueda binaria es O(log n).

---

## 6. Flujo principal del juego

Al ejecutar el programa, `Principal` instancia `MenuPrincipal` y llama a `arrancar()`,
que muestra las instrucciones básicas e inicia el bucle principal de comandos.

Desde el menú el jugador puede acceder al historial, ordenarlo, buscar partidas o iniciar
una nueva. Al elegir una dificultad, `MenuPrincipal` instancia la subclase correspondiente
de `NivelDificultad` y crea un `MotorJuego` inyectándole el nivel y el historial compartido
de la sesión.

`MotorJuego` inicializa el `Tablero`, que crea la matriz `Celda[][]`, distribuye las minas
aleatoriamente con `Random` y calcula los números de proximidad de cada celda
recorriendo los ocho vecinos de cada posición.

Durante la partida, el jugador ejecuta comandos. Al descubrir una celda segura,
`MotorJuego` activa el algoritmo recursivo de Flood Fill (`ejecutarFlujoCascada`), que
revela automáticamente todas las celdas vacías conectadas y los números que las
bordean, deteniéndose ante minas, banderas o los bordes del tablero.

Si el jugador descubre una mina, la partida termina en derrota. Si descubre todas las
celdas seguras, termina en victoria. En ambos casos `MotorJuego` calcula el tiempo
transcurrido usando `System.currentTimeMillis()`, revela el tablero completo, muestra
el resumen de la partida con la frase correspondiente al nivel y registra la partida en el
historial mediante `historial.registrarPartida()`.

El control regresa a `MenuPrincipal`, donde el jugador puede iniciar una nueva partida,
consultar el historial o cerrar el programa con `&CERRAR`.
