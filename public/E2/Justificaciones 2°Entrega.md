# Documento de Decisiones Técnicas – Proyecto MetaMapa

## Justificaciones de Diseño Generales
### Repositorios (DAOs)
- Cada entidad persistente tiene su DAO específico.

### Multiplicidad de Fuentes
- Cada tipo de fuente (estática, dinámica, proxy) se expone mediante una única API.
- Cada fuente puede contener varias sub-fuentes internas, cada una con su propio dataset, en el caso del Servicio estáticas; APIs externas en el caso del Servicio Proxy y la una única fuente dinámica en caso del Servicio Dinámico.
- Los hechos de cada sub-fuente son persistidos localmente en un repositorio, en lugar de leerse directamente del dataset en cada consulta o de la API externa.
- A cada hecho se le asigna un ID único, el cual es incremental por servicio, siempre garantizando unicidad y capacidad de recuperación.

## Justificaciones de Diseño del Agregador
### Eliminación de Hechos
- Los hechos eliminados se marcan con un flag (`eliminado: true`) en su repositorio de origen.
- Estos hechos no deben ser reenviados al Agregador bajo ninguna circunstancia.
- Esta política aplica a todas las fuentes (estática, dinámica, proxy).
- Al eliminarse un Hecho es importante avisarle a la fuente correspondiente

### Identificadores de Hechos
- Se requiere almacenar el nombre de la sub-fuente de cada hecho.
- En el Agregador los hechos se diferencian unos de los otros por id, fuente y origen

### Colecciones Compartidas
- Las colecciones no son compartidas entre instancias del sistema.
- Los Hechos se comparten entre instancias MetaMapa. 

### Agregador Push-Based y Pull-Based
- **Push-Based**: Al agregar un nuevo hecho, la fuente puede notificar al Agregador para que actualice las colecciones de forma inmediata. Esto lo hará mediante el esquema Push based y el punto de entrada del agregador en la ruta. POST - /hechos
- **Pull-Based**: Si una colección se modifica (por ejemplo, al eliminar un criterio), el Agregador debe volver a consultar hechos a todas las fuentes asociadas a dicha colección. Lo mismo sucede para el refresco de colecciones, si se refresca se hace un pull de Hechos, pero filtrando únicamente los que sean nuevos a la última fecha de refresco.

### Control de Novedades en las Fuentes
- Cada hecho debe incluir una marca temporal (fecha de carga o modificación).
- Al actualizar una colección, el Agregador puede solicitar hechos con fecha posterior a un determinado punto.
- Las fuentes deben permitir consultas por fecha como parámetro.

### SPAM y Solicitudes de Eliminación
- Las solicitudes de eliminación son creadas enviando el id del Hecho asociado y el contribuyente que está creando esa solicitud
- 

## Justificaciones de Diseño de la Fuente Dinámica

El módulo fue diseñado aplicando los patrones de diseño MVC, Cliente – Servidor y Repositorio.
Con MVC se buscó que el cliente (Agregador), solo conozca la API provista por la fuente mediante DinamicaController de, a su vez, el Controller solamente conoce la interfaz del Service, dejando la parte lógica y las verificaciones de tipos de dato a este último.

### Solicitudes y Hechos

Teniendo en cuenta que los requerimientos indican que el Agregador va a poder consultar los hechos, decidimos implementar persistencia de las solicitudes al igual que los hechos para poder almacenar los mismos, esto se apoya en el Patrón Repositorio, se implementó una interfaz IDinamicaRepostory que utilizara el Service. Esta interfaz se encargará de traer los hechos cuando el Service los solicite, además de gestionar el guardado de los mismos y las modificaciones que puedan recibir.
Como los contribuyentes podrán solicitar la modificación de los hechos en caso de estar registrados, se decidió implementar una interfaz IContribuyentesRepository que guardara la información de cada contribuyente.

### Administradores y Contribuyentes

Ambos cuentan con endpoints provistos por DinamicaController, no son considerados clases dentro del sistema debido a que sus “funciones” en realidad son casos de uso, ejemplo de esto es “Solicitar hecho”, “Gestionar hecho” y “Eliminar hecho”, por lo tanto decidimos separar la lógica de orquestación de los modelos de dominio.

```java
public class DinamicaService implements IDinamicaService{
    public SolicitudHechoDTO solicitudHecho(HechoInputDTO hechoSolicitado){
    ///...
    }
}
```


### Manejo de Errores, Validaciones y Excepciones

La lógica correspondiente a validaciones como tipos de datos, que el usuario cuente con los permisos necesarios, entre otros, se decidió implementar como métodos del Service para poder mantener la lógica correspondiente fuera del Controller (este únicamente hace llamados al Service y espera el resultado de las validaciones), a su vez, el Controller manejara excepciones personalizadas (apoyándose en códigos de error HTTP) en caso de que una validación no tenga resultado satisfactorio.

## Justificación de la elección de TF-IDF local para detección de spam

En el marco del proyecto, se evaluaron dos alternativas para la detección automática de fundamentos inválidos o "spam" en solicitudes de eliminación: el uso de un algoritmo local basado en TF-IDF frente a la integración con un servicio externo (API de moderación). A continuación, se presentan los motivos por los cuales se optó por el enfoque local.

### Adecuación funcional

TF-IDF permite adaptar la lógica de evaluación de fundamentos al **dominio específico del sistema**, donde los textos no ofensivos pueden ser considerados spam si no contienen palabras clave relevantes. A diferencia de un servicio externo generalista, el enfoque local ofrece una **alta capacidad de personalización** sobre las reglas semánticas relevantes para nuestra plataforma.

### Rendimiento

El procesamiento local mediante TF-IDF no depende de red ni de terceros, lo que garantiza un **tiempo de respuesta óptimo y constante**, incluso a gran escala o en condiciones de conectividad limitada. Esto favorece el desempeño del sistema en tiempo de ejecución y evita latencias asociadas a servicios externos.

### Seguridad

Al ejecutarse localmente, el algoritmo de TF-IDF **evita enviar información sensible fuera del sistema**, lo cual mejora la privacidad de los usuarios y elimina riesgos asociados a políticas de protección de datos o uso indebido por parte de terceros.

### Mantenibilidad

El modelo basado en TF-IDF es de **fácil mantenimiento y evolución**: se pueden ajustar las palabras clave válidas, umbrales de score y corpus de referencia sin depender de versiones externas o cambios en servicios de terceros. Esto asegura continuidad y control ante eventuales modificaciones futuras.

### Testabilidad

El algoritmo local puede ser **testeado de forma determinística** con casos específicos y reproducibles, lo cual permite validar el comportamiento del detector frente a múltiples escenarios. Esto contribuye directamente a la calidad del software y a su robustez.

---

**Conclusión**: Por su alto grado de adaptación, independencia tecnológica y control sobre el comportamiento, se seleccionó el algoritmo local TF-IDF como estrategia para detección de spam, garantizando así mayor alineación con los objetivos funcionales del sistema.

## Justificaciones de Diseño de la Fuente Estática

En la ENTREGA 1 se pensó a la Fuente Estática como una simple clase "Fuente Estática" que importaba directamente un archivo CSV cuando la entidad "Colecciones" se lo podía (el Caso de Uso de "Consultar Colecciones" para el rol "Administrador"). En esta ENTREGA 2, terminó siendo un componente complejo del sistema que se ocupa únicamente de gestionar los DATASET.

### Generalidades

El diseño divide las responsabilidades en diferentes capas y clases:

* **HechoDTO/ArchivoDTO** representa el Data Transfer Object que contiene sólo los datos que se exponen y transfieren, evitando exponer la lógica interna del dominio.
* **Archivo** actúa como una entidad encargada de representar el archivo estático con atributos propios y la funcionalidad de obtener hechos.
* **TipoArchivo** como interfaz y su implementación concreta **ArchivoCSV** encapsulan la estrategia para importar “Hechos”, aplicando el patrón Strategy para permitir extender fácilmente el soporte a nuevos formatos sin modificar la clase Archivo.
* **HechoController** y **HechoService** se encargan de la orquestación del caso de uso para que la persona que cumpla el rol “Administradora” pueda importar hechos.

    * **API**: consumir los datos estáticos.
* Los **Repositorios** abstraen la persistencia y el manejo de las colecciones en memoria, aplicando el patrón Repository, facilitando cambiar la implementación de almacenamiento sin afectar las capas superiores.

Esta separación permite que cada componente sea altamente cohesivo, con bajo acoplamiento, lo que facilita la mantenibilidad, testeabilidad y extensión futura del sistema.

### Principios SOLID

#### Open-Closed Principle (OCP)

La fuente estática está abierta a nuevas formas de archivos (XML, etc.) sin modificar las clases existentes, solo implementando nuevas estrategias para importar los hechos.
- **Patrón Strategy**: la entidad Archivo, TipoArchivo y las estrategias, que en este caso sólo tenemos ArchivoCSV que en un principio era la Fuente Estática en la primera entrega.
  **Atributo de Calidad**: extensibilidad.

#### Liskov Substitution Principle (LSP)

Se estuvo tipando de la manera más genérica posible. Cualquier clase que implemente TipoArchivo puede ser usada indistintamente por Archivo, permitiendo el polimorfismo.

#### Interface Segregation Principle (ISP)

Se trató de mantener un diseño orientado a interfaces en las capas de la Fuente Estática de tal manera que cada uno de los componentes sólo conozcan interfaces específicas y reducidas (pequeñas).
**Atributo de Calidad**: bajo acoplamiento, testeabilidad.

#### Dependency Inversion Principle (DIP)

Gracias al ISP, los módulos de alto nivel no dependen de un módulo de bajo nivel, sino a través de una interfaz para que el acoplamiento sea el mínimo posible, lo que favorece la inyección de dependencias. El HechoService se desentiende del medio persistente que nos va a permitir a futuro hacerlo con una BD, pero mientras tanto estamos persistiendo en Memoria.
**Atributos de Calidad**: testeabilidad.

## Justificaciones de Diseño de la Fuente Proxy

### Hechos en memoria en un repository

Se optó por cargar los hechos en memoria al inicio de la aplicación para reducir la dependencia en tiempo real de la API Catedra, mejorar la performance de acceso
a los datos durante la ejecución y facilitar una futura migración a una base de datos persistente. Esta estrategia permite una arquitectura más extensible
y desacoplada, manteniendo consistencia temporal y favoreciendo el desarrollo y las pruebas.

### Fuentes en memoria en un repository

Se utilizó un repositorio de fuentes para centralizar y gestionar las diferentes entidades que proveen hechos al sistema.
Esto facilita la incorporación de nuevas fuentes, promueve la organización y el acceso uniforme a las mismas, y desacopla la lógica de obtención de datos de la lógica de negocio, mejorando la mantenibilidad y escalabilidad del proyecto.

### Patrón Adapter en fuente

Se aplicó el patrón Adapter para adaptar los inputs de las APIs externas a la estructura esperada por las entidades internas de tipo “hecho” y, a su vez, para adaptar
los outputs hacia otros módulos del proyecto (el agregador). Esto permitió integrar fuentes con diferentes formatos sin modificar la lógica interna, manteniendo bajo
acoplamiento y facilitando la reutilización y extensibilidad del sistema.

### Patrón Strategy en fuente

Se implementó el patrón Strategy en la gestión de fuentes para permitir que el sistema se acople dinámicamente a múltiples tipos de fuentes, cada una con
su comportamiento específico. Esta solución promueve la extensibilidad y flexibilidad del proyecto, permitiendo incorporar nuevas fuentes con lógica distinta
sin modificar el núcleo de la aplicación.

### Entidad Filtro

Se definió una entidad de filtro para encapsular los parámetros de consulta del endpoint GET, evitando pasar múltiples query params de forma individual.
Esta solución mejora la legibilidad del código, simplifica el manejo de filtros y permite una evolución más ordenada del endpoint al agregar nuevos criterios
sin modificar la firma del controlador.

### Uso de las seeder

Se implementaron dos seeders: uno para instanciar la API externa y otro que, basado en un repositorio de fuentes, carga los hechos correspondientes a cada una.
Esta separación permite escalar fácilmente a múltiples tipos de fuentes en el futuro y mantiene una inicialización ordenada y modular, donde los hechos
quedan correctamente agrupados según su fuente de origen.

### Uso de scheduler

Se implementó un scheduler que actualiza periódicamente las colecciones cada una hora para reflejar cambios recientes en hechos, como creaciones o modificaciones.
Esta estrategia asegura que los datos en memoria estén actualizados sin necesidad de consultas constantes a la API externa, optimizando el rendimiento y
manteniendo la coherencia de la información.