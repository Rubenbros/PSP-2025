package ejercicio7_biblioteca;

/**
 * CLASE: Biblioteca
 *
 * DESCRIPCIÓN:
 * Gestor central que coordina los libros y las estadísticas de préstamos.
 *
 * PROBLEMA A RESOLVER:
 * Múltiples estudiantes accederán simultáneamente para buscar y tomar libros.
 * Las estadísticas deben ser consistentes. Usa sincronización en los métodos
 * que accedan a variables compartidas.
 *
 * ATRIBUTOS NECESARIOS:
 * - List<Libro> libros
 * - int prestamosRealizados
 * - int intentosFallidos
 *
 * MÉTODOS NECESARIOS:
 * - Constructor: Biblioteca()
 * - void agregarLibro(Libro libro)
 * - Libro buscarLibroDisponible(String titulo)
 * - boolean prestarLibro(Libro libro, String estudiante)
 * - void devolverLibro(Libro libro)
 * - void mostrarEstadisticas()
 * - void mostrarEstadoLibros()
 */

// TODO: Importar java.util.List y ArrayList

// TODO: Implementar la clase Biblioteca aquí
