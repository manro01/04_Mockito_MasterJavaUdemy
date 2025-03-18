/**
 * Primer ejemplo de uso de Mockito.
 * Un mock es un objeto que va a tomar el lugar de un objeto que necesitemos para 
 * la prueba. por ejemplo en este caso, cuando se necesite un objeto de ExamenRepository
 * va a entrar un mock que se va a comportar como una implementación de ExamenRepository,
 * con esto NO necesitamos una implmentación de ExamenRepository (no se necesita una clase ExamenRepositoryImpl)
 * porque el mock va a tomar su lugar, en caso de que exista la clase (que es lo más probable en una
 * aplicación real) NO se usa esa clase y se usa el mock.
 * Nosotros "configuramos" el mock para que se comporte como nosotros queramos
 */
package services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import models.Examen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import repositories.ExamenRepository;


public class ExamenServicesImplTest
{
    
    @Test
    public void findExamenPorNombre()
    {
        ////*****************  Contexto para probar ****************************////
        //Se crea el mock que se va a usar como una implementación de la clase ExamenRepository
        ExamenRepository repository= mock(ExamenRepository.class); 
               
        //Se crea la instancia de la clase que vamos a probar
        ExamenService service= new ExamenServicesImpl(repository);
        
        //se crea el objeto con los datos que desamos obtener, este objeto reemplaza a lo que se espera
        //  recibir de un DB o un API REST
        List<Examen> datos= Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));
        
        //cuando se invoque el método findAll, de repository, va a devolver el objeto datos
        //si existiera una implementacíon real, la bloquea y manda esto
        when(repository.findAll()).thenReturn(datos);
        
        ////*****************  Contexto para probar ****************************////
        
        Optional<Examen> examen= service.findExamenPorNombre("Matematicas"); 
        
        
        assertTrue(examen.isPresent()); //isPresent es como verificar que no sea null
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
        
        //orElseThrow() hace mas o menos los mismo que get().getNombre(), verifican que el valor sea el mismo, pero la misma
        // API recomienda usar orElseThrow()
        
        
        
        
    }
    
}
