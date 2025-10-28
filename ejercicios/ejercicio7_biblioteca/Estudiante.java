package ejercicio7_biblioteca;

/**
 * CLASE: Estudiante (Thread)
 *
 * DESCRIPCIÓN:
 * Representa un estudiante que quiere tomar prestado un libro.
 * Es un Thread que ejecuta de forma concurrente con otros estudiantes.
 *
 * COMPORTAMIENTO:
 * 1. Buscar su libro favorito en la biblioteca
 * 2. Si está disponible, tomarlo prestado
 * 3. Leer (simular con Thread.sleep de 2-4 segundos)
 * 4. Devolverlo
 * 5. Si no está disponible, reintentar (máximo 3 intentos)
 *
 * ATRIBUTOS NECESARIOS:
 * - String nombre
 * - String libroFavorito
 * - Biblioteca biblioteca
 * - int maxIntentos (ej: 3)
 *
 * MÉTODOS NECESARIOS:
 * - Constructor: Estudiante(String nombre, String libroFavorito, Biblioteca biblioteca)
 * - void run() - implementa el comportamiento del Thread
 */

// TODO: Importar java.util.Random

// TODO: Implementar la clase Estudiante que extiende Thread
