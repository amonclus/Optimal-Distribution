# Sistema de Gestión de Inventario

## Descripción
Este proyecto es un Sistema de Gestión de Inventario que permite a los usuarios gestionar productos, distribuciones y restricciones dentro de un sistema de almacenamiento. El sistema proporciona varios comandos para interactuar con el inventario y las distribuciones.

## Estructura del Directorio
- `DOCS`: Contiene la documentación del proyecto. Si abres el documento index.html del directorio `JavaDocs`, encontrarás una documentación más detallada del código.
- `EXE`: Directorio con los ejecutables. Contiene un archivo `.sh` para poder ejecutar el programa.
- `FONTS/Domini`: Contiene las clases de dominio que representan los objetos del sistema.
- `FONTS/Persistencia`: Contiene las clases que gestionan la persistencia de los objetos del sistema.
- `FONTS/Presentacio`: Contiene las clases que gestionan la interacción con el usuario.
- `FONTS/JUnit`: Contiene las pruebas unitarias para las clases de dominio.

## Acciones Disponibles

- `Añadir productos`
    - **Precondición**: El inventario debe estar inicializado (Por el momento se hace automáticamente al iniciar el programa).
    - **Postcondición**: Se añade un nuevo producto al inventario.

- `Eliminar productos`
    - **Precondición**: El producto debe existir en el inventario.
    - **Postcondición**: Se elimina el producto del inventario.

- `Modificar relaciones entre productos`
    - **Precondición**: Ambos productos deben existir en el inventario.
    - **Postcondición**: Se actualiza la relación de similitud entre los dos productos.

- `Consultar el inventario`
    - **Precondición**: Ninguna.
    - **Postcondición**: Se muestra el inventario completo.

- `Creación de una nueva distribución`
    - **Precondición**: El inventario y las relaciones deben estar inicializados (Automático al iniciar el programa).
    - **Postcondición**: Se crea una nueva distribución con el ID especificado.

- `Eliminación de una distribución`
    - **Precondición**: La distribución debe existir.
    - **Postcondición**: Se elimina la distribución especificada.

- `Activación de una distribución en la estantería`
    - **Precondición**: La distribución debe existir.
    - **Postcondición**: Se activa la distribución especificada.

- `Modificación de una distribución`
    - **Precondición**: Debe haber una distribución activa.
    - **Postcondición**: Se intercambian dos productos en la distribución activa.

- `Mostrar una distribución`
    - **Precondición**: La distribución debe existir.
    - **Postcondición**: Se muestra la distribución especificada.

- `Añadir restricción a un producto`
    - **Precondición**: El producto debe existir en el inventario.
    - **Postcondición**: Se añade una restricción al producto especificado en la distribución activa (Explicado en detalle en el escrito).


- `Eliminar restricción de un producto`
    - **Precondición**: La restricción debe existir.
    - **Postcondición**: Se elimina la restricción del producto especificado.

- `Añadir estantes`
    - **Precondición**: Ninguna.
    - **Postcondición**: Se añaden estantes al sistema de almacenamiento.

- `Eliminar estantes`
    - **Precondición**: Debe haber suficientes estantes para eliminar.
    - **Postcondición**: Se eliminan estantes del sistema de almacenamiento.

- `exit`
    - **Precondición**: Ninguna.
    - **Postcondición**: Se cierra el programa.
  
## Compilación y Ejecución
Para compilar y ejecutar el programa principal, sigue estos pasos:

1. Acceder al directorio del proyecto des de un terminal.
2. Compilar el programa con el comando `javac FONTS/Main.java`.
3. Ejecutar el programa con el comando `java FONTS/Main`.

Para ejecutar directamente el programa en la carpeta EXE, ejecutar `chmod +x runMain.sh` des del directorio EXE y ejecutar `./runMain.sh`.

Alternativamente, se puede usar el Makefile describido a countinuación.

## Uso del Makefile
El proyecto incluye un `Makefile` para facilitar la compilación y ejecución del código. A continuación se describen las reglas disponibles:

- `all`: Compila todos los archivos fuente (excluyendo los tests) y coloca los archivos `.class` en el directorio `EXE`.
    ```sh
    make all

- `tests`: Compila y ejecuta todos los tests, un total de 14 y coloca los archivos `.class` en el directio `EXE`.
    ```sh
    make tests

- `run`: Compila todos los archivos fuente y ejecuta la clase principal `FONTS.Main`.
    ```sh
    make run
    ```

- `clean`: Elimina el directorio `EXE` y todos sus contenidos.
    ```sh
    make clean
    ```

Para utilizar estas reglas, simplemente ejecuta `make <regla>` en el terminal desde el directorio FONTS del proyecto.