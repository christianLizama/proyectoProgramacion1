/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

/**
 *
 * @author Diego Aguilera
 */
public class RectangulosMatriz {
    private String Nombre; 
    private String contenido; 

    public RectangulosMatriz(String Nombre, String contenido) {
        this.Nombre = Nombre;
        this.contenido = contenido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
