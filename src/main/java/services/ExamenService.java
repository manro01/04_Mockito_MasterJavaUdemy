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
}
