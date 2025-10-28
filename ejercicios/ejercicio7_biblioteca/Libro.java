package ejercicio7_biblioteca;

/**
 * CLASE: Libro
 *
 * DESCRIPCIÓN:
 * Representa un libro de la biblioteca que puede ser prestado y devuelto.
 *
 * PROBLEMA A RESOLVER:
 * Múltiples estudiantes (hilos) intentarán tomar prestado el mismo libro simultáneamente.
 * Solo UNO debe conseguirlo. Debes usar sincronización para evitar condiciones de carrera.
 *
 * ATRIBUTOS NECESARIOS:
 * - int idLibro
 * - String titulo
 * - boolean prestado
 * - String estudianteActual (null si está disponible)
 *
 * MÉTODOS NECESARIOS:
 * - Constructor: Libro(int idLibro, String titulo)
 * - boolean prestar(String nombreEstudiante)
 * - void devolver()
 * - boolean estaDisponible()
 * - Getters necesarios
 * - String toString()
 */

// TODO: Implementar la clase Libro aquí
