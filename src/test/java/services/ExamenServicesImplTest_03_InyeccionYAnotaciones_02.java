/**
 * Uso de Intección de dependencias y anotaciones de mockito (vienen de ExamenServicesImplTest_03_InyeccionYAnotaciones_01.
 *  
 * Se va mostrar la segunda forma habilitar las anotaciones y la inyeccion de dependecia pero en 
 * ExamenServicesImplTest_03_InyeccionYAnotaciones_01 se mostro otra
 */
package services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import models.Examen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.ExamenRepository;
import repositories.PreguntaRepository;


///Con esto se habilito el uso de anotaciones e unyección de dependencias
//es importante que este esta dependencia ccon ell artifactid mockito-junit-jupiter
@ExtendWith(MockitoExtension.class)         
public class ExamenServicesImplTest_03_InyeccionYAnotaciones_02
{
    
    /**         Se modificaron las declaraciones de variables de instancias */
    
    //Se utilizo la etiqueta @Mock para señalar que son mock, ya eran mock antes pero
    //  se inicilizaban como un objeto cualquiera, ahora se va a inicilizar como mock
    @Mock
    ExamenRepository repository;               
    @Mock
    PreguntaRepository preguntaRepository;      
    
    //Con esto indicamos que vamos a usar inyección de dependecias en este service
    //NOTA que se cambio el ExamenServices por ExamenServicesImpl, es porque service tiene que ser
    //  una implementación de un objeto y ExamenService es una interfaz, que no puede tener implementación
    //  esto se hacía entes en el setup con la linea service= new ExamenServicesImpl(repository, preguntaRepository);
    @InjectMocks 
    ExamenServicesImpl service;                      
    
    
    /****           Se modifico  el setup **************************************************/
    /**
     * Va a crear algunas configuraciones básicas.
     */
    @BeforeEach
    void setup()
    {
        ////*****************  Contexto para probar ****************************////
//        repository= mock(ExamenRepository.class); 
//        preguntaRepository= mock(PreguntaRepository.class);
//        service= new ExamenServicesImpl(repository, preguntaRepository);
        
        //MockitoAnnotations.openMocks(this);  //esto es para habilitar el uso de inyeccion de dependencias y etiquetas
        //creo que este ya no se necesita por que ya se activo con @ExtendWith(MockitoExtension.class) 

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
    
    @Test
    void testPreguntasExamen()
    {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        
        when(preguntaRepository.findPreguntaPorExamen(5L)).thenReturn(Datos.PREGUNTAS);
        
        Examen examen= service.findExamenPorNombreConPreguntas("Matematicas");
        
        assertEquals(5, examen.getPreguntas().size());
        
        assertTrue(examen.getPreguntas().contains("aritmética"));
        
        
    }
    
    
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
    
    /************************ MÉTODO NUEVO  *****************************************************/

    
}
