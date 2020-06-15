/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import javafx.scene.control.Button;

/**
 *
 * @author 56945
 */
public class Rectangulos {
    
    
    Button botonRectangulo;
    double x;
    double y;
    double ancho;
    double alto;
   

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }
    
    public double isX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double isY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public Button getBotonRectangulo() {
        return botonRectangulo;
    }
    
    public void setBotonRectangulo(Button botonRectangulo) {
        this.botonRectangulo = botonRectangulo;
    }
    
    //Se hace un set de todo lo del rectangulo
    public void setPosicionEnPantalla(){
        Button nuevoRectangulo = new Button();
        setBotonRectangulo(nuevoRectangulo);
        
        botonRectangulo.setLayoutX(x);
        botonRectangulo.setLayoutY(y);
        botonRectangulo.setMinSize(botonRectangulo.USE_PREF_SIZE,botonRectangulo.USE_PREF_SIZE);
        botonRectangulo.setPrefSize(ancho, alto);
        botonRectangulo.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
        
    }
    
}
