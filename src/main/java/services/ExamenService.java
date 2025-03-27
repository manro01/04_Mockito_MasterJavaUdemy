/**
 * 
 */
package services;

import java.util.Optional;
import models.Examen;

public interface ExamenService
{
    Optional<Examen> findExamenPorNombre(String nombre);
    
    Examen findExamenPorNombreConPreguntas(String nombre);
    
    //se añadio para ExamenServicesImplTest_04_PruebasDelRepositorio
    Examen guardar(Examen examen);
}
