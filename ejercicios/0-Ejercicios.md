# Lección 0 — Ejercicios: Sincrónico, Asíncrono y Paralelo

## Ejercicio 1 — Ejecución sincrónica (ordena los pasos)
Ordena los pasos para que se ejecuten de forma sincrónica. Debes empezar por limpiar con la aspiradora y solo cuando termines, pasar a los platos.

Escribe un número del 1 al 6 delante de cada línea para indicar el orden correcto.

[ ] Platos: paso #2. Cargar los platos en el lavavajillas y ponerlo en marcha.

[ ] Aspirar la alfombra: paso #3. Cuando la alfombra esté limpia, apagar la aspiradora.

[ ] Aspirar la alfombra: paso #1. Enchufar la aspiradora.

[ ] Platos: paso #3. Cuando termine el lavavajillas, guardar los platos en el armario.

[ ] Aspirar la alfombra: paso #2. Empezar a limpiar la alfombra.

[ ] Platos: paso #1. Aclarar/enjuagar los platos.

Pista: En ejecución sincrónica haces todas las acciones de una tarea, una detrás de otra, y luego pasas a la siguiente.

---

## Ejercicio 2: ¿Cuál es la mejor forma de ejecutarlo?

Elige el tipo de ejecución más adecuado para completar cada tarea. Arrastra mentalmente o relaciona cada ítem de la izquierda con su tipo de ejecución a la derecha.

- Contar un cuento o relatar una historia → [ Sincrónico | Asincrónico | Paralelo ]
- Planchar tu camisa mientras lavas la ropa → [ Sincrónico | Asincrónico | Paralelo ]
- Entregar 10 paquetes con 10 repartidores → [ Sincrónico | Asincrónico | Paralelo ]

## Ejercicio 3: Aplicación de consulta de datos

En una aplicación web, llegan múltiples peticiones para obtener datos. La aplicación dispone de un único ejecutor que gestiona las tareas. ¿Cuáles de las siguientes afirmaciones sobre su ejecución son correctas? Selecciona todas las que correspondan.

- [ ] Puede procesar las peticiones en paralelo sin requerir cambios.
- [ ] Puede procesar las peticiones secuencialmente, una por una.
- [ ] Para procesar las peticiones en paralelo se necesitarían varios ejecutores.
- [ ] Puede manejar las peticiones de forma asincrónica.

Pista: “Paralelo” implica múltiples ejecutores ejecutando al mismo tiempo; “asincrónico” permite solapar tiempos de espera aun con un único ejecutor.
