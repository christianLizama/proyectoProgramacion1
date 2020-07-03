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
    
    Button botonRectangulo = new Button();
    double x;
    double y;
    double ancho;
    double alto;
    int hacerPulsable;

    public int getHacerPulsable() {
        return hacerPulsable;
    }

    public void setHacerPulsable(int hacerVisible) {
        this.hacerPulsable = hacerVisible;
    }
   
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
    
    //Hace que el boton deje de ser pulsable mientras se dibuja
    //de esta forma evitamos el error al mover el mouse cerca
    //cuando se termina de dibujar se habilita nuevamente
    public void hacerPulsable(){
        if(hacerPulsable==0){
            botonRectangulo.setDisable(true);
            botonRectangulo.setOpacity(100);
        }
        else{
            botonRectangulo.setDisable(false);
        }
        
    }
    //Se hace un set de todo lo del rectangulo
    public void setPosicionEnPantalla(){
        
        botonRectangulo.setLayoutX(x);
        botonRectangulo.setLayoutY(y);
        botonRectangulo.setMinSize(botonRectangulo.USE_PREF_SIZE,botonRectangulo.USE_PREF_SIZE);
        botonRectangulo.setPrefSize(ancho, alto);
        botonRectangulo.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
        
    }
    
}
