/**
 * Es para implmentar un repositorio de pregunta
 */
package repositories;

import java.util.List;

public interface PreguntaRepository
{
    List<String> findPreguntaPorExamen(Long id);
}
