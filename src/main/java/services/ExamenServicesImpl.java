/**
 * 
 */
package services;

import java.util.Optional;
import models.Examen;
import repositories.ExamenRepository;

public class ExamenServicesImpl implements ExamenService
{

    private ExamenRepository examenRepository;
    
    public ExamenServicesImpl(ExamenRepository examenRepository)
    {
        this.examenRepository= examenRepository;
    }
    
    
    @Override
    public Optional<Examen> findExamenPorNombre(String nombre)
    {
        return examenRepository.findAll()
                               .stream()
                               .filter(e -> e.getNombre().contains(nombre))
                               .findFirst();
        
        //FindFirst regresa un Optional

    }
    
}
