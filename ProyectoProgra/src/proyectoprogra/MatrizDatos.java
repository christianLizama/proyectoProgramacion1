/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Diego Aguilera
 */
public class MatrizDatos {
    
    
    
    GridPane matrizCheck = new GridPane(); 
    
    Button agregar = new Button("Agregar");
    
    Pane paneMatriz = new Pane();
    
    ScrollPane explorer =  new ScrollPane();
   
    int j=1;

    
    public ScrollPane getExplorer() {
        return explorer;
    }

    public int getJ() {
        return j;
    }

    public GridPane getMatrizCheck() {
        return matrizCheck;
    }
    
    
    
    
    
    public void  muestraDeMatriz(ArrayList<RectangulosMatriz> Datos){
        explorer.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        paneMatriz.getChildren().add(agregar);
        
        agregar.setLayoutY(500);
        
        ArrayList<String> textosSelec = new ArrayList<>();
        
        int x=0;
        for (int i = 0; i < Datos.size(); i++) {
            Label nombreTexto = new Label (Datos.get(i).getNombre());
            nombreTexto.setStyle("-fx-backgroud-color: yellow; -fx-text-fill: black;");
            nombreTexto.wrapTextProperty().set(true);
            nombreTexto.setPrefWidth(300);
            nombreTexto.setPrefHeight(10);

            
            TextArea areaDeExtraccion = new TextArea (Datos.get(i).getContenido());
            
            areaDeExtraccion.setStyle("-fx-backgroud-color: green; -fx-text-fill: black;");
            areaDeExtraccion.wrapTextProperty().set(true);
            areaDeExtraccion.setPrefWidth(300);
            areaDeExtraccion.setPrefHeight(50);
            
            CheckBox agregar = new CheckBox();
            agregar.setPrefSize(50, 50);
            agregar.setLayoutX(30);
            
            
           
            
            matrizCheck.add(nombreTexto,0,x+i);
            x++;
            matrizCheck.add(areaDeExtraccion,0,j);
            matrizCheck.add(agregar,5,j);
            j++;
            j++;
            
            
            agregar.setOnAction((event) -> {
                if(agregar.isSelected()){ //Se añade
                    areaDeExtraccion.setDisable(true);
                    areaDeExtraccion.setOpacity(100);
                    
                    textosSelec.add(areaDeExtraccion.getText());
                    
                    
                }
                else{//SE borra
                    areaDeExtraccion.setDisable(false);
                    
                    for (int k = 0; k < textosSelec.size(); k++) {
                        if(textosSelec.get(k).equals(areaDeExtraccion.getText())){
                            textosSelec.remove(k);          
                        }
                    }
                }   
            });
        }
        
        System.out.println("");
        agregar.setOnAction((event) -> {
           
            for (int i = 0; i < textosSelec.size(); i++) {
                
                System.out.println(textosSelec.get(i));
            }
            
        });
    }
}
