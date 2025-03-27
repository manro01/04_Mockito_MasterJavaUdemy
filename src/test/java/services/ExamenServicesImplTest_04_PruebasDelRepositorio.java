/**
 * Uso de Intección de dependencias y anotaciones de mockito (vienen de ExamenServicesImplTest_03_InyeccionYAnotaciones_02.
 *  
 * Se va a interactuar más con ExamenRepository
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
public class ExamenServicesImplTest_04_PruebasDelRepositorio
{
    
    @Mock
    ExamenRepository repository;               
    @Mock
    PreguntaRepository preguntaRepository;      
    
    @InjectMocks 
    ExamenServicesImpl service;                      
    
    
    /**
     * Va a crear algunas configuraciones básicas.
     */
    @BeforeEach
    void setup()
    {
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

    /**
     * Se prueba que se guarde bien un examen.
     * Se va a guardar el examen que esta en Datos.Examen
     * y es con ese mismo examen que se van a hacer las assert para saber si lo guardo bien
     */
    @Test
    void testCompruebaGuardadExamen()
    {
        when(repository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);
        
        Examen examen= service.guardar(Datos.EXAMEN);
        
        //revisa que el examen no sea nulo
        assertNotNull(examen.getId());
        
        //revisa que el id del examen sea 8, esto se especifico así en Datos.Examen
        assertEquals(8L, examen.getId());
        
        //revisa que el nombre del examen sea fisíca, esto se especifico así en Datos.Examen
        assertEquals("fisíca", examen.getNombre());
        
    }
    
}
