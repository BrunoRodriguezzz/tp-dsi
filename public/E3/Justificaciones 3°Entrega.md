# Documento de Decisiones Técnicas – Proyecto MetaMapa

## Refactor fuentes
En la entrega pasada gran parte de la lógica de las fuentes se encontraba en el fuentes service.

En esta entrega bajamos esa lógica al dominio creando la clase `Fuente` que tiene la responsabilidad de pedir hechos/colecciones.

Estas fuentes cuentan con dos atributos muy importantes:
- El hechos adapter para que las fuentes puedan pedirles sus hechos a los modulos de Proxy, Dinámica y Estática.
- El propio WebClient para que sepan a donde pedir.

De esta forma podemos tratar de forma polimorfica a las fuentes sin tener que tener en cuenta el tipo de la misma al momento de hacer pedidos.

Esto tambíen provocó que ahora conozcamos las fuentes internas de los módulos y que ahora podamos pedir directamente los hechos de esa fuente en particular (sigue siendo una consulta al módulo que tenga esa fuente).

### Como conocemos las fuentes
Al momento de agregar una fuente en un módulo, el mismo tendrá la responsabilidad de avisarnos que se agrego una nueva fuente (`Push based`). De esta forma el agregador siempre sabe las fuentes que existen sin necesidad de preguntarle a los módulos.

### Persistencia de las fuentes
Se agrego un `Fuente repository` para persistir las fuentes. Como de momento los repositorios son en memoria los módulos nos envían siempre al incia

## Algoritmos de consenso
Para realizar la implementación de el requerimiento mencionado se utilizará un Scheduler que llamará al service de Hechos. 
Este método del service de Hechos es el encargado de Consensuar a los Hechos. Este evento sucederá todos los días a las 4 de la mañana.

### Consensuador
Como se ve en el diagrama de clases, se decidió implementar una clase `Consensuador`, quien es el encargado de, en base a todos los hechos que se tienen en el 
repositorio, traer los hechos de las fuentes que tengan el mismo título que ese Hecho. De esta forma luego se pasan los Hechos a los algoritmos de consenso.

### Algoritmos de Consenso
Mediante el patrón Strategy, cada `Algoritmo` se encarga de, utilizando la lista de Hechos del agregador (lista que tiene todos los hechos con el mismo título),
comparar entre esos hechos dependiendo de si coinciden en atributos o no y utilizando la cantidad de fuentes total existentes en el sistema.
Los algoritmos reciben la lista de fuentes solo para saber cuántas existen y realizar alguna otra comparación necesaria.
Es importante destacar que los hechos se van a ir pasando de un algoritmo a otro, no los 3  a la vez ya que el algoritmo siguiente debe marcar los hechos en base 
a los hechos ya marcados por los algoritmos anteriores.

### Consenso
Se decidió "marcar" los hechos utilizando un Enum `Consenso`. El Hecho ahora tienen una lista de Consensos, y cada algoritmos se encargará de marcar el Hecho
en caso de ser necesario con su respectivo Enum. Cada Hecho puede ser marcado con múltiples tipos de Consensos.
Para las Colecciones suecede lo mismo. Estas tendrán los Hechos ya marcados con sus respectivos consensos y además tendrán un atributo que también será una lista
de consensos. Por lo que al solicitar los Hechos Curados, mostrará todos los hechos que cumplan con todos sus estrategias de consenso.


## Exposición de API MetaMapa
En cuanto a la exposivión de la API se implementaron endpoints para los siguentes casos:
- CRUD sobre colecciones
- Obtencion de todos los hechos de una Coleccion
- Modificacion del algoritmo de consenso
- Agregar o quitar fuentes de una colecciones:
En este caso particular, al agregar una fuente es necesario que se refresque la Colección ya que tendrá nuevos Hechos. Y en caso de que se quite una fuente se
debe recalcular.
- Agregar o quitar filtros de una colección:
En este caso particular, al agregar un filtro es necesario recalcular los Hechos, ya que no tendrá Hechos nuevos, sino que los Hechos que tenía previamente
deben ser filtrados en base a este filtro. Y en el caso de que se quite un filtro sí será necesario refrescar la Colección ya que podría tener nuevos Hechos.
## API Publica para otras instancias metamapa
- Generar una solicitud de eliminacion
- Navegacion filtrada sobre colección
- Navegacion curada o irrestricta sobre una colección

