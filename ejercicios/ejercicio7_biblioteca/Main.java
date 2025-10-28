package ejercicio7_biblioteca;

/**
 * EJERCICIO 7: SISTEMA DE BIBLIOTECA CON PRÉSTAMO CONCURRENTE DE LIBROS
 *
 * ESCENARIO:
 * - Biblioteca con 5 libros (algunas copias repetidas)
 * - 8 estudiantes intentan tomar libros prestados simultáneamente
 * - Hay competencia por los libros populares
 *
 * OBJETIVO:
 * Implementar las clases Libro, Biblioteca y Estudiante con sincronización
 * adecuada para evitar condiciones de carrera y garantizar consistencia.
 */
public class Main {

/*    public static void main(String[] args) {
        System.out.println("=== SIMULACIÓN DE BIBLIOTECA ===\n");

        // Crear la biblioteca
        Biblioteca biblioteca = new Biblioteca();

        // Agregar libros (2 copias de Don Quijote, 1 de Cien Años, 2 de Principito)
        biblioteca.agregarLibro(new Libro(1, "Don Quijote"));
        biblioteca.agregarLibro(new Libro(2, "Don Quijote"));
        biblioteca.agregarLibro(new Libro(3, "Cien Años de Soledad"));
        biblioteca.agregarLibro(new Libro(4, "El Principito"));
        biblioteca.agregarLibro(new Libro(5, "El Principito"));

        System.out.println("Biblioteca con 5 libros creada\n");

        // Crear estudiantes (4 quieren Don Quijote, 2 Cien Años, 2 Principito)
        Estudiante[] estudiantes = {
            new Estudiante("Ana", "Don Quijote", biblioteca),
            new Estudiante("Luis", "Don Quijote", biblioteca),
            new Estudiante("María", "Cien Años de Soledad", biblioteca),
            new Estudiante("Pedro", "El Principito", biblioteca),
            new Estudiante("Carmen", "Don Quijote", biblioteca),
            new Estudiante("José", "El Principito", biblioteca),
            new Estudiante("Laura", "Cien Años de Soledad", biblioteca),
            new Estudiante("Miguel", "Don Quijote", biblioteca)
        };

        // Iniciar todos los estudiantes con pequeño delay
        for (Estudiante estudiante : estudiantes) {
            estudiante.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Esperar a que todos terminen
        for (Estudiante estudiante : estudiantes) {
            try {
                estudiante.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mostrar resultados finales
        System.out.println("\n========================================");
        biblioteca.mostrarEstadisticas();
        System.out.println();
        biblioteca.mostrarEstadoLibros();
    }*/
}
