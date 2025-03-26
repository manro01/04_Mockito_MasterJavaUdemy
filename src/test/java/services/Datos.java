package services;

/**
 * Es una clase de ayuda para crear datos que se van a usar constatntemente en la 
 * pruena y en lugar de estar repitiendo el código a cada rato se va escribir una 
 * sola vez, el profesor menciono que se puedo haber pusto en ExamentServiceTest pero
 * era bueno separarla para la organización del programa
 * 
 */

import java.util.Arrays;
import java.util.List;
import models.Examen;

public class Datos
{
    public final static List<Examen> EXAMENES= Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));
    
    //en realidad aquí deberían haber preguntas, pero se "resumieron" y solo se pusieron la materia a la que
    //  corresponde la pregunta, pero una vez más deberian ser preguntas
    public final static List<String> PREGUNTAS= Arrays.asList("aritmética", "integrales", "derivadas"
                , "trigonometría", "geometría");
}
