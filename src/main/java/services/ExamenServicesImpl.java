/**
 * 
 */
package services;

import java.util.List;
import java.util.Optional;
import models.Examen;
import repositories.ExamenRepository;
import repositories.PreguntaRepository;

public class ExamenServicesImpl implements ExamenService
{

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;
    
    public ExamenServicesImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository)
    {
        this.examenRepository= examenRepository;
        this.preguntaRepository= preguntaRepository;
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

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre)
    {
        Optional<Examen> examenOptional= findExamenPorNombre(nombre);
        Examen examen= null;
        if(examenOptional.isPresent())
        {
            examen= examenOptional.orElseThrow();
            List<String> preguntas= preguntaRepository.findPreguntaPorExamen(examen.getId());
            examen.setPreguntas(preguntas);
        }
        
        return examen;
    }

    //se añadio para ExamenServicesImplTest_04_PruebasDelRepositorio
    @Override
    public Examen guardar(Examen examen)
    {
        if(examen.getPreguntas().isEmpty())
        {
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        
        return examenRepository.guardar(examen);
    }
    
}
