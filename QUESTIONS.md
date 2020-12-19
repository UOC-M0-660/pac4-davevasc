# PARTE TEORICA

### Arquitecturas de UI: MVP, MVVM y MVI

#### MVVM

##### ¿En qué consiste esta arquitectura?
MVVM (Model-View-ViewModel) es un patrón de diseño, el cual nos ayuda a separar la lógica de negocios de la interfaz de usuario, facilitando las pruebas, mantenimiento y la escalabilidad de los proyectos. Para implementarlo en Android, se estructura la aplicación en 3 capas:

View:
(Activities o Fragments) Muestra la información directamente al usuario y es la capa con la que el usuario interactúa con la aplicación a través de eventos, permitiendo la manipulación de la aplicación y sus datos. La View observa los ViewModels para obtener la información necesaria y, en consecuencia, actualizar la UI. Cuando el usuario interactúa con la interfaz, ésta capa informa a uno o más ViewModel, pero cada ViewModel nunca puede tener información sobre las View. Ésta comunicación View - ViewModel se realizará mediante observables, utilizando librerías como DataBinding, pudiendo ser también RxJava o LiveData.

ViewModel:
(ViewModel Class) Actor intermediario entre el modelo y la vista, el cual contiene toda la lógica de presentación. Obtiene los datos del Model, los procesa, y los manda a la View permitiendo así que puedan ser visualizados y/o modificados por el usuario en la UI. Para el ViewModel podemos usar una clase especial de tipo ViewModel(), en la cual los datos  almacenados pueden persistir frente a cambios de configuración espontáneos como las dichosas rotaciones de pantalla.

Model:
(Data Classes) Representa la capa de datos y/o lógica de negocio. Debe tener correctamente estructurada la información para que el ViewModel pueda obtenerla de forma fácil y cómoda. A su vez, también obtiene indicaciones de la ViewModel para interactuar con los datos que estén en el backend, por ejemplo, accediendo a base de datos, API, etc.

##### ¿Cuáles son sus ventajas?
Está recomendado por Google.
Gracias a la clase especial ViewModel, ofrece herramientas para manejar los datos en los ciclos de vida de la capa de View.
Código más organizado y muy limpio.
Permite la reutilización de código.
Mantenimiento rápido y fácil.
Mayor facilidad y comprensión del proyecto de cara a desarrollos colaborativos con más desarrolladores.
Pruebas unitarias fáciles.

##### ¿Qué inconvenientes tiene?
Es un poco más enrevesado que otros patrones.
Hay que mantener un numero mayor de ficheros que en otros patrones.
Es muy difícil migrar al patrón MVVW desde una aplicación que ya esté hecha.

#### MVP

##### ¿En qué consiste esta arquitectura?
MVP (Model View Presenter) es un patrón de diseño, el cual permite separar la capa de presentación de la lógica haciendo que el funcionamiento de la interfaz de usuario (controlador) sea independiente de la representación de los datos en pantalla (vista). Para implementarlo en Android, la aplicación se estructura en las siguientes 3 capas:

Model:
(Classes) Representa la capa de datos, lógica de negocio. Tiene la responsabilidad de administrar los datos, es decir, recuperarlos, actualizarlos o guardarlos a través de bases de datos, APIs, servicios…

View:
(Activities o Fragments) Muestra la información directamente al usuario y es la capa con la que el usuario interactúa con la aplicación a través de eventos, es decir, intercepta las acciones del usuario que realiza en la interfaz de usuario.

Presenter:
(Classes) Actor intermediario entre el modelo y la vista, el cual contiene toda la lógica de presentación. Recupera datos del Model y los devuelve formateados a la View.

##### ¿Cuáles son sus ventajas?
Hará la aplicación más testeable.
Más escalabilidad.
El mantenimiento del código será más fácil.
El código de la aplicación será más limpio y ordenado.
El presenter hará que tu código establezca independencia de la UI y las fuentes de datos.

##### ¿Qué inconvenientes tiene?
Se puede producir un leak de la actividad si ejecutamos tareas largas en segundo plano.
Ocurriría un error al intentar actualizar una Activity que ya haya muerto.
Requiere la creación de más ficheros.
La curva de aprendizaje puede entorpecer los tiempos de desarrollo.

#### MVI

##### ¿En qué consiste esta arquitectura?
MVI (Model View Intent) es uno de los patrón de diseño más modernos que existen hoy en día, el cual está inspirado en el principio unidireccional y cíclico del framework Cicle.js. Para implementarlo en Android, la aplicación se estructura en las siguientes 3 capas:

Model:
(Classes) Representa un estado que puede ir cambiando. Deben ser inmutables para garantizar un flujo de datos unidireccional entre ellos y las otras capas de la arquitectura.

View:
(Activities o Fragments) Se encarga de observar los estados y renderizar en la vista. Están representados por interfaces, las cuales se implementan en una o más actividades o fragmentos.

Intent:
(Classes) Esta capa se define como la intención o deseo del un usuario al ejecutar cierta acción. Si el usuario realiza cierta acción, esa intención será recibida en la View, entonces el observable del Presenter la interceptará y actualizará el estado del Model.

##### ¿Cuáles son sus ventajas?
Flujo de datos unidireccional y cíclico
Estados consistentes durante todo el ciclo de vida de las vistas.
Modelos inmutables los cuales proporcionan un comportamiento confiable y más seguridad en los threads en aplicaciones muy grandes.

##### ¿Qué inconvenientes tiene?
La curva de aprendizaje para este patrón suele ser un poco más alta respecto a otros patrones, ya que se necesita tener bastantes conocimientos de otros métodos de programación como la programación reactiva, Multi-hilo y RxJava.
Se acumula bastante código ya que debemos mantener un estado por cada acción del usuario.
Por cada acción o intención del usuario, creamos objetos. La creación de los objetos cuesta mucha memoria.

---

### Testing

#### ¿Qué tipo de tests se deberían incluir en cada parte de la pirámide de test? Pon ejemplos de librerías de testing para cada una de las partes. 
UI Test (10% de las pruebas de la aplicación, deben ser de este tipo, según las recomendaciones de Google)
Son pruebas grandes y costosas, denominadas pruebas de interfaz de usuario, las cuales emulan las acciones físicas que realiza el usuario en la pantalla del dispositivo. Para ello, es imprescindible que realicemos los test utilizando un emulador o dispositivo físico real. Las librerías de testing más utilizadas para estas pruebas en Android son Espresso y UI Automator.

Integration Test (20% de las pruebas de la aplicación, deben ser de este tipo, según las recomendaciones de Google)
Son pruebas medianas, denominadas pruebas de integración, las cuales comprueban cómo dos componentes diferentes se pueden comunicar o interactuar entre ellos. Normalmente se ejecutan después de completar las pruebas unitarias en sus componentes, de esa forma facilitará mucho localizar los posibles errores que se puedan detectar, y entonces se podrá comprobar que los componentes se comunican correctamente entre ellos. Una de las librerías de testing más comunes para realizar pruebas de integración en Android es Roboelectric, la cual no requiere un emulador, dispositivo físico o servidor, pero sí que es recomendable realizar estas pruebas en un servidor como el conocido Firebase Test Lab  de Google, para así poder probar la aplicación en diferentes tamaños de pantalla y configuraciones de hardware.

Unit Test (70% de las pruebas de la aplicación, deben ser de este tipo, según las recomendaciones de Google)
Son pruebas pequeñas y rápidas, denominadas pruebas unitarias, las cuales consisten en crear objetos falsos que sustituyan a los objetos reales y de esta manera poder probar un determinado requerimiento. El objetivo de utilizar objetos falsos en lugar de reales, es romper las dependencias con otros objetos y de esta manera probar requerimientos de forma independiente. Son de ejecución muy rápida y además no requieren utilizar un emulador o dispositivo físico. Las librerías de testing más utilizadas para estas pruebas en Android son JUnit y Mockito.

#### ¿Por qué los desarrolladores deben centrarse sobre todo en los Unido Tests?
Los desarrolladores deben centrarse más en los Unit Test por los siguientes tres motivos:
Rapidez: Al no requerir emulador ni dispositivo físico, estas pruebas se realizan de forma super rápida.
Independencia: Se está probando directamente código Kotlin, no código perteneciente a algún framework específico de Android. Por lo tanto, no se requiere utilizar librerías de testing, tan solo utilizando JUnit ya podríamos realizar nuestros Unit Tests.
Cimientos: Este tipo de pruebas son el cimiento de las pruebas en Android. Se espera que si todas las pruebas unitarias funcionan correctamente, es muy probable que el resto de pruebas (integración y UI) también funcionen tal y como se espera.

---

### Inyección de dependencias

#### Explica en qué consiste y por qué nos ayuda a mejorar nuestro código.
La inyección de dependencias es una técnica comunmente utilizada en programación y muy adecuada para el desarrollo de aplicaciones en Android. Consiste en que en lugar de que cada clase sea responsable de buscar sus dependencias, otra entidad será la encargada de proporcionarlas. Esto se hace para evitar que las clases estén estrechamente acopladas unas con otras, es decir, al crear una clase, se debe separar la creación de un objeto respecto al uso del objeto.

La inyección de dependencias nos ayuda a mejorar nuestro código permitiendo mayor rehusabilidad de las clases. También facilita la refactorización, facilitando la verificación de las dependencias durante el tiempo de creación de objetos o el tiempo de compilación. Por último, pero no menos importante, ayuda a que las pruebas unitarias se realicen de forma correcta y efectiva, ya que al pasar las dependencias a través del constructor de la clase en cuestión, permite que los objetos mock se pasen como parámetros a esas clases que están siendo testadas.

#### Explica cómo se hace para aplicar inyección de dependencias de forma manual a un proyecto (sin utilizar librerías externas).
Sin utilizar librerías externas, podemos inyectar dependencias a través del constructor de la clase cuando se construye y utilizando contenedores para poder compartir instancias de clases en diferentes partes de la aplicación, de esa forma, las tendremos centralizadas en un único sitio.
Primero creamos la clase contenedora de dependencias, la cual contendrá todos los objetos compartidos a través de toda la aplicación.
Dado que las dependencias se van a utilizar en toda la aplicación, hay que crear la instancia de la clase contenedora desde la clase Application.
Entonces se podrá obtener la instancia de la clase contenedora desde cualquier lugar de la aplicación, eso hará que tengamos dicha instancia compartida en toda la app.
Si necesitamos que una clase en concreto esté en más lugares de la aplicación, se puede crear una “clase fábrica” de esa clase, para crear instancias de ella, y añadirla a la clase contenedor para que podamos acceder a ella.
En caso de que la aplicación se expanda más en el futuro, se recomienda administrar el alcance y el ciclo de vida de los contenedores, para liberar memoria en caso de que sea necesario.