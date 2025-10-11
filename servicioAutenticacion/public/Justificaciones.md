# Justificaciones
El fin de este archivo es dar justificaciones de las decisiones tomadas en el respectivo módulo.

## Modelo de Datos
Para el modelo de datos, se decidió tomar como referencia un tipo de esquema denominado "Esquema de Estrella", el cual resulta sumamente útil en un dominio de Data Warehousing, como es el caso de este servicio.

La idea principal es tener dos **fact table** principales:
- Hecho
- Solicitud

El fin de las mismas es, utilizando como PK una combinación de las dimensiones (categoria, coleccion, hora, provincia, etc.), mostrar datos para la inteligencia de negocios (generación de estadísticas) mediante consultas sencillas.

Cada **dimension** será un criterio de clasificación que podrá utilizarse para conocer la cantidad de Hechos relacionados a ese dominio en particular. 

### Ejemplo práctico
Supongamos que se quiere conocer cuántos Hechos fueron reportados para una coleccion y una provincia en particular. 

El modelado realizado actualmente permite que simplemente agrupando por coleccion y provincia, sin importar la categoriam y sumando las cantidades de Hechos particulares, poder conocer para cada combinación de coleccion-provincia la cantidad de hechos, sin importar la categoria.

## Modelado de Dominio
Para el modelado de dominio se implementaron clases que resultan ser una representación bastante similar a las entidades que se encuentran persistidas en la BD. 

### Enums
Aprovechando la poderosa herramienta de los enums implementada por JAVA, se han agregado métodos y atributos a los enums a fines de realizar una migración de datos de forma más sencilla.

### Generación de CSVs
El modelado actual simplifica considerablemente la creación de archivos CSVs para estadísticas ya que, en caso de querer conocer todas las estadísticas, se podría devolver una tabla sumamente atómica con los datos requeridos.

Cada fila de esta tabla resultaría ser, ni más ni menos, que los datos asociados a una instancia de la clase Hecho. 