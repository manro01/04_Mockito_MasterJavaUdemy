/**g
 * 
 */
package repositories;

import java.util.List;
import models.Examen;

public interface ExamenRepository
{
    List<Examen> findAll();
    
    //se añadio para ExamenServicesImplTest_04_PruebasDelRepositorio
    Examen guardar(Examen examen);
}
