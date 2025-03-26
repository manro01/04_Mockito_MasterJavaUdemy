/**
 * Primer ejemplo de uso de Mockito.
 * Un mock es un objeto que va a tomar el lugar de un objeto que necesitemos para 
 * la prueba. por ejemplo en este caso, cuando se necesite un objeto de ExamenRepository
 * va a entrar un mock que se va a comportar como una implementación de ExamenRepository,
 * con esto NO necesitamos una implmentación de ExamenRepository (no se necesita una clase ExamenRepositoryImpl)
 * porque el mock va a tomar su lugar, en caso de que exista la clase (que es lo más probable en una
 * aplicación real) NO se usa esa clase y se usa el mock.
 * Nosotros "configuramos" el mock para que se comporte como nosotros queramos
 * 
 * RECUERA que los mock son una simulación de datos obtenidos de una fuente externa por ejemplo si queremos usar
 * examenes que se crean en un servicio externo, simulamos los mock con el objeto que recibiriamos de ese
 * servicio externo.
 * 
 * No sepueden hacer mocks de clases privadas, estaticas o final, tienen que ser publicas.
 */
package services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import models.Examen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import repositories.ExamenRepository;
import repositories.PreguntaRepository;


public class ExamenServicesImplTest
{
    
    ExamenRepository repository;
    ExamenService service;
    PreguntaRepository preguntaRepository;
    
    /**
     * Va a crear algunas configuraciones básicas.
     */
    @BeforeEach
    void setup()
    {
        ////*****************  Contexto para probar ****************************////
        //Se crea el mock que se va a usar como una implementación de la clase ExamenRepository
        repository= mock(ExamenRepository.class); 
        
        //Se crea el mock para preguntasRepository
        preguntaRepository= mock(PreguntaRepository.class);
               
        //Se crea la instancia de la clase que vamos a probar
        service= new ExamenServicesImpl(repository, preguntaRepository);
        
        //creo que esa última línea es la inyección de dependencias, porque le estamos pasanso
        // las dependencias de los dos mock
    }
    
    @Test
    public void findExamenPorNombre()
    {

        //se crea el objeto con los datos que desamos obtener, este objeto reemplaza a lo que se espera
        //  recibir de un DB o un API REST
        //List<Examen> datos= Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Lenguaje"),
                //new Examen(7L, "Historia"));
        
        //cuando se invoque el método findAll, de repository, va a devolver el objeto datos
        //si existiera una implementacíon real, la bloquea y manda esto
        //when(repository.findAll()).thenReturn(datos);
        
        /***** NOTA   ********************************
         * Como se puede ver arriba en la primera versión del programa, se creo aquí mismo la lista
         * de examenes List<Examen> datos, pero como es algo que se va a repetir constantemente se 
         * decicio que crear este objeto se hiciera en otro lado, se pudo haber dejado aquí pero
         * el maestro aconsejo dejarlo en otra clase, para mejorar el código
         */
        
        
        //Obtenemos una lista de examenes
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        ////*****************  Contexto para probar ****************************////
        
        Optional<Examen> examen= service.findExamenPorNombre("Matematicas"); 
        
        
        assertTrue(examen.isPresent()); //isPresent es como verificar que no sea null
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
        
        //orElseThrow() hace mas o menos los mismo que get().getNombre(), verifican que el valor sea el mismo, pero la misma
        // API recomienda usar orElseThrow()
         
    }
    
    
    /**
     * Es lo mismo que findExamenPorNombre pero aquí se va a crear un mock con la
     * lista vacía.
     */
    @Test
    public void findExamenPorNombreListaVacia()
    {

        //se crea el objeto con los datos que desamos obtener, este objeto reemplaza a lo que se espera
        //  recibir de un DB o un API REST
        List<Examen> datos= Collections.emptyList();  //con esto regresamos una lista vacía
        
        //cuando se invoque el método findAll, de repository, va a devolver el objeto datos
        //si existiera una implementacíon real, la bloquea y manda esto
        when(repository.findAll()).thenReturn(datos);
        
        ////*****************  Contexto para probar ****************************////
        
        Optional<Examen> examen= service.findExamenPorNombre("Matematicas"); 
        
        assertFalse(examen.isPresent()); //isPresent es como verificar que no sea null
        //orElseThrow() hace mas o menos los mismo que get().getNombre(), verifican que el valor sea el mismo, pero la misma
        // API recomienda usar orElseThrow()
         
    }
    
    /**
     * Presta mucha atencion al when, when es de mockito, y son idicaciones.
     * when(repository.findAll()).thenReturn(Datos.EXAMENES); 
     *  dice que cuando se mande llamar a repository.findAll() regresa Datos.EXAMENES
     * when(preguntaRepository.findPreguntaPorExamen(5L)).thenReturn(Datos.PREGUNTAS);
     *  dice que cuando se llamme a preguntaRepository.findPreguntaPorExamen(5L) se regrese Datos.PREGUNTAS 
     *  nota que se uso como parametro el 5L que es un id, y el id 5 es el examen de Matematicas, en el Datos.EXAMENES
     * 
     * NOTA:01
     * En el test setup se creo el objeto service= new ExamenServicesImpl(repository, preguntaRepository);
     * en donde repository y preguntaRespository son mock por lo tanto cuando se 
     * use service se van a usar los mock
     *  
     */
    @Test
    void preguntasExamen()
    {
        //regresa una lista de examenes
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        //regesa un pregunta, del examen con id 5, en la clase Datos, creamos una lista de examenes donde estaban
        // los examenes con su id, por eso sabemos que existe un examen con el id 5
        when(preguntaRepository.findPreguntaPorExamen(5L)).thenReturn(Datos.PREGUNTAS);
        
        //En el test setup se creo un objeto 
        Examen examen= service.findExamenPorNombreConPreguntas("Matematicas");
        
        //esperamos que el examen tenga 5 preguntas
        assertEquals(5, examen.getPreguntas().size());
        
        /**
         * cuando hacemos examen= service.findExamenPorNombreConPreguntas("Matematicas"); 
         * lo que esta pasando por "detras es: 
         *  se llama al objeto service que dentro tiene los mock,
         *  cuando se usa findExamenPorNombreConPreguntas("Matematicas");
         *      este método dentro usa el método findExamenPorNombre(nombre); y
         *      a su vez findExamenPorNombre(nombre) llama a findExamenPorNombre(String nombre)
         *      y findExamenPorNombre(String nombre) dentro llama a findAll de examenRepository
         *          y como examenRepository es el comportamiento que definicmos del mock va a 
         *          regresar Datos.EXAMENES, por lo tanto ya tenemos esa referencia
         * 
         *  depues el mismo método service.findExamenPorNombreConPreguntas("Matematicas") 
         *      usa findPreguntaPorExamen(examen.getId()) en la linea 
         *       List<String> preguntas= preguntaRepository.findPreguntaPorExamen(examen.getId());
         *      y como usa el mock de preguntaRepository que definimos, tambien ya sabemos su comportamiento
         * 
         * ahora si juntando todo tenemos que cuando se usa examenRepository.findAll() se regresa el Datos.EXAMENES
         *  y cuando se usa preguntaRepository.findPreguntaPorExamen(examen.getId()); va a buscar en Datos.EXAMENES 
         *  uno que coincida con el id mandado en este caso 5L que sabemos es Matematicas y de ese va a regresar 
         *  la lista de preguntas preguntas que estan en Datos.PREGUNTAS 
         * 
         * Entonces si en assertEquals(5, examen.getPreguntas().size()); usamos un valor que no se a 5, no 
         * va a estar bien porque examen.getPreguntas().size() hace referencia al examen de Matematicas y es
         * el unico examen al que se le ha dado un comportamiento en mock los demas examenes estan vacion
         */
        
    }
    
}
