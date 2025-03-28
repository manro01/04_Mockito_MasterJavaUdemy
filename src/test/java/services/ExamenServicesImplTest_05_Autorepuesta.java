/**
 * Uso de Intección de dependencias y anotaciones de mockito (vienen de ExamenServicesImplTest_03_InyeccionYAnotaciones_02.
 *  
 * Mockito tiene la forma de responderse por ejemplo si queremos guarda un valor
 *  donde el id se autogenere (como muchas DB) entonces podemos usar esta característica
 *  Se hace con Anser()
 * 
 * Anser() sirve para hacer llamada al objeto que ya se uso, hata ahora habiamos visto
 *  que usando el when podriamos configurar un comportatmiento, para que se obtenga una respuesta
 *  que nosotros queremos pero con Answer() podemos inetractuar con esa respueta
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import repositories.ExamenRepository;
import repositories.PreguntaRepository;


@ExtendWith(MockitoExtension.class)         
public class ExamenServicesImplTest_05_Autorepuesta
{
    
    @Mock
    ExamenRepository repository;               
    @Mock
    PreguntaRepository preguntaRepository;      
    
    @InjectMocks 
    ExamenServicesImpl service;                      
    
    
    /**
     * Va a crear algunas configuraciones básicas.
     * Este método ya no se usa, pues las configuraciones inciales de los mock y 
     * del service se dejaron arriba como parte de las variables de instancia y con
     * intección de dependencias 
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
    
    /************************ MÉTODO NUEVO  *****************************************************/
    
    /**
     * Se va s simular guardar un objeto, pero con la caracterisitica de que e 
     * id se va a autoincrementar.
     * Lo que se va a hacer es guardar un Examen sin id, pero en lugar de usar thenReturn se usa un then 
     * con una nuevo objeto Answer de tipo examen que tiene una clase anonima, en esta clase se va a establecer
     * un valor inicial para el id, y se va a usar el método answer(InvocationOnMock iom) donde el 
     * InvocationOnMock tiene la respuesta como un objeto, y se puede sacar el examen de ese objeto, para trabajar en el
     */
    @Test
    void testCompruebaGuardadExamenConAutoId()
    {
        //se cambio el thenReturn por then que es que se usa en estos casos
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() 
        {
            Long secuencia= 9L; //se va iniciar desde 9 porque en Datos ya habiamos usado del 5 al 8
            
            //InvocationOnMock nos invoca al mock con el que guardamos el exeman, recuerda qye arriba ya se guardo el examen
            @Override
            public Examen answer(InvocationOnMock iom) throws Throwable
            {
                Examen examen= iom.getArgument(0); //obtenemo sel examen que se guardo
                examen.setId(secuencia++);
                return examen;
            }
        });
        
        Examen examen= service.guardar(Datos.EXAMEN);
        
        //revisa que el examen no sea nulo
        assertNotNull(examen.getId());
        
        //revisa que el id del examen sea 8, esto se especifico así en Datos.Examen
        assertEquals(8L, examen.getId());
        
        //revisa que el nombre del examen sea fisíca, esto se especifico así en Datos.Examen
        assertEquals("fisíca", examen.getNombre());
        
    }
}
