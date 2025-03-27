/**
 * Uso del método verify de mockito (vienen de ExamenServicesImplTest_01_When.
 * 
 * Sirve para verificar si se invoco un método del repositorio.
 * Puede ser útil para saber si en un método se invoco al método del repositoio, por ejemplo
 * si tenemos un if y dentro se hace la invocación al repositorio, pero si no entra
 * al if no se hace la invocación, para eso podemos usar verify
 * 
 * 
 */
package services;

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


public class ExamenServicesImplTest_02_Verify
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
        repository= mock(ExamenRepository.class); 
        
        preguntaRepository= mock(PreguntaRepository.class);
               
        service= new ExamenServicesImpl(repository, preguntaRepository);

    }
    
    @Test
    public void testFindExamenPorNombre()
    {

        
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        Optional<Examen> examen= service.findExamenPorNombre("Matematicas"); 
        
        
        assertTrue(examen.isPresent()); //isPresent es como verificar que no sea null
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
        
    }
    
    
    /**
     * Es lo mismo que findExamenPorNombre pero aquí se va a crear un mock con la
     * lista vacía.
     */
    @Test
    public void testFindExamenPorNombreListaVacia()
    {

        List<Examen> datos= Collections.emptyList();  
        
        when(repository.findAll()).thenReturn(datos);
        
        
        Optional<Examen> examen= service.findExamenPorNombre("Matematicas"); 
        
        assertFalse(examen.isPresent()); 
         
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
    void testPreguntasExamen()
    {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        when(preguntaRepository.findPreguntaPorExamen(5L)).thenReturn(Datos.PREGUNTAS);
        
        Examen examen= service.findExamenPorNombreConPreguntas("Matematicas");
        
        assertEquals(5, examen.getPreguntas().size());
        
        assertTrue(examen.getPreguntas().contains("aritmética"));
        
        
    }
    
    
    /************************ MÉTODO NUEVO  *****************************************************/
    
    /**
     * Se va a verificar que se hicieron las invocaciones a 
     * verify(repository).findAll()
     * 
     *  verify(preguntaRepository).findPreguntaPorExamen(5l).
     * 
     *  Se van a pasar las pruebas porque, si se invocaron los métodos de los repositorios
     */
    @Test
    void TestPreguntasExamenVerify()
    {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        when(preguntaRepository.findPreguntaPorExamen(5L)).thenReturn(Datos.PREGUNTAS);
        
        Examen examen= service.findExamenPorNombreConPreguntas("Matematicas");
        
        assertEquals(5, examen.getPreguntas().size());
        
        assertTrue(examen.getPreguntas().contains("aritmética"));
        
        verify(repository).findAll();
        
        verify(preguntaRepository).findPreguntaPorExamen(5l);
    }
    
    /**
     * Se va a verificar que se hicieron las invocaciones a 
     * verify(repository).findAll();
     * 
     *  verify(preguntaRepository).findPreguntaPorExamen(5l).
     * 
     *  Esta prueba no se va a pasar porque no se invoco a preguntaRepository esto es 
     *  porque en examen=service.findExamenPorNombreConPreguntas("Matematicas2"); se cambio a Matematicas2
     *  entonces si se usa el repository para buscar el examen por Matematicas2, pero como no lo encuentra
     *  regresa un null, y entonces en findExamenPorNombreConPreguntas(String nombre) de ExamenServicesImpl
     *  como hay un null NO entra al if y por lo tanto no invoca a preguntaRepository.findPreguntaPorExamen(examen.getId());
     *  por eso el verify de este repositorio no funciona
     */
    @Test
    void testNoExistePreguntaDeExamenVerify()
    {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        when(preguntaRepository.findPreguntaPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        
        Examen examen= service.findExamenPorNombreConPreguntas("Matematicas2");
        
        assertNull(examen);
        
        verify(repository).findAll();
        
        verify(preguntaRepository).findPreguntaPorExamen(5l);
    }
    
}
