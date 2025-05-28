# Documento de Decisiones Técnicas – Proyecto MetaMapa

## 1. Multiplicidad de Fuentes

- Cada tipo de fuente (estática, dinámica, proxy) se expone mediante una única API.
- Cada fuente puede contener varias sub-fuentes internas, cada una con su propio dataset, en el caso del Servicio estáticas; APIs externas en el caso del Servicio Proxy y la una única fuente dinámica en caso del Servicio Dinámico.
- Los hechos de cada sub-fuente son persistidos localmente en un repositorio, en lugar de leerse directamente del dataset en cada consulta o de la API externa.
- A cada hecho se le asigna un ID único, el cual es incremental por servicio, siempre garantizando unicidad y capacidad de recuperación.

## 2. Fuente Proxy y Dinámica

- Ambas funcionan como APIs únicas que almacenan hechos persistidos localmente en sus respectivos repositorios.
- Los hechos se identifican por un ID incremental único por tipo de fuente.

## 3. Eliminación de Hechos

- Los hechos eliminados se marcan con un flag (`eliminado: true`) en su repositorio de origen.
- Estos hechos no deben ser reenviados al Agregador bajo ninguna circunstancia.
- Esta política aplica a todas las fuentes (estática, dinámica, proxy).

## 4. Identificadores de Hechos

- Se requiere almacenar el nombre de la sub-fuente de cada hecho.
- En el Agregador los hechos se diferencian unos de los otros por id, fuente y origen

## 5. Colecciones Compartidas

- Las colecciones no son compartidas entre instancias del sistema.
- Los Hechos se comparten entre instancias MetaMapa. En caso de un que un Hecho sea eliminado la fuente proxy deberá notificar a la otra instancia MetaMapa de esta eliminación para que esta instancia tampoco lo muestre
## 6. Agregador Push-Based y Pull-Based

- **Push-Based**: Al agregar un nuevo hecho, la fuente puede notificar al Agregador para que actualice las colecciones de forma inmediata.
- **Pull-Based**: Si una colección se modifica (por ejemplo, al eliminar un criterio), el Agregador debe volver a consultar hechos a todas las fuentes asociadas a dicha colección. Lo mismo sucede para el refresco de colecciones, si se refresca se hace un pull de Hechos, pero filtrando únicamente los que sean nuevos a la última fecha de refresco.

## 7. Control de Novedades en las Fuentes

- Cada hecho debe incluir una marca temporal (fecha de carga o modificación).
- Al actualizar una colección, el Agregador puede solicitar hechos con fecha posterior a un determinado punto.
- Las fuentes deben permitir consultas por fecha como parámetro.

## 8. Repositorios (DAOs)

- Cada entidad persistente tiene su DAO específico.

## 9. SPAM y Solicitudes de Eliminación
- Las solicitudes de eliminación son creadas enviando el id del Hecho asociado y el contribuyente que está creando esa solicitud

