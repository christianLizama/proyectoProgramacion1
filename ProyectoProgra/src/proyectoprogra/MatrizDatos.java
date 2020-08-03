/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import java.io.FileWriter;
import java.io.PrintWriter;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Diego Aguilera
 */
public class MatrizDatos {

    GridPane matrizCheck = new GridPane();

    ScrollPane explorer = new ScrollPane();

    Button confirmar = new Button("Crear documento");
    
    Button seleccionarTodo = new Button("Seleccionar todo");
    
    
    int apareceBoton = 0;

    int j = 1;

    public ScrollPane getExplorer() {
        return explorer;
    }

    public int getJ() {
        return j;
    }

    public GridPane getMatrizCheck() {
        return matrizCheck;
    }

    public void muestraDeMatriz(ArrayList<RectangulosMatriz> Datos) {
        explorer.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        ArrayList<String> textosSelec = new ArrayList<>();
        ArrayList<CheckBox> checkboxs = new ArrayList<>();
        
        int x = 0;

        confirmar.setDisable(true);

        for (int i = 0; i < Datos.size(); i++) {
            Label nombreTexto = new Label(Datos.get(i).getNombre());
            nombreTexto.setStyle("-fx-backgroud-color: yellow; -fx-text-fill: black;");
            nombreTexto.wrapTextProperty().set(true);
            nombreTexto.setPrefWidth(300);
            nombreTexto.setPrefHeight(10);

            TextArea areaDeExtraccion = new TextArea(Datos.get(i).getContenido());

            areaDeExtraccion.setStyle("-fx-backgroud-color: green; -fx-text-fill: black;");
            areaDeExtraccion.wrapTextProperty().set(true);
            areaDeExtraccion.setPrefWidth(500);
            areaDeExtraccion.setPrefHeight(80);

            CheckBox agregar = new CheckBox();
            agregar.setPrefSize(80, 80);
            agregar.setLayoutX(30);

            Button espacio = new Button();
            espacio.setVisible(false);
            espacio.setDisable(true);

            matrizCheck.add(nombreTexto, 0, x + i);
            x++;
            matrizCheck.add(areaDeExtraccion, 0, j);
            matrizCheck.add(espacio, 1, j);
            matrizCheck.add(agregar, 2, j);

            j++;
            j++;
            
            checkboxs.add(agregar);
            
            
            
            
            
            
            
            agregar.setOnAction((event) -> {
                if (agregar.isSelected()) { //Se añade
                    areaDeExtraccion.setDisable(true);
                    areaDeExtraccion.setOpacity(100);
                    confirmar.setDisable(false);

                    apareceBoton++;
                    textosSelec.add(nombreTexto.getText() + ": " + areaDeExtraccion.getText());

                } else {//SE borra
                    areaDeExtraccion.setDisable(false);
                    apareceBoton--;
                    if (apareceBoton == 0) {
                        confirmar.setDisable(true);
                    }

                    for (int k = 0; k < textosSelec.size(); k++) {
                        if (textosSelec.get(k).equals(nombreTexto.getText() + ": " + areaDeExtraccion.getText())) {
                            System.out.println("xd");
                            textosSelec.remove(k);
                        }
                    }
                    //System.out.println(textosSelec.get(0));

                }
            });

        }
        
        
        
        
        confirmar.setOnAction((event) -> {
            String textoFinal = "";
            for (String string : textosSelec) {
                textoFinal = textoFinal + string;
            }

            guardarEnTXT(textoFinal, "ExtraccionFinal.txt");

        });
        
        seleccionarTodo.setOnAction((event) -> {
            int k =1;
            confirmar.setDisable(false);
            for (CheckBox checkbox : checkboxs) {
                checkbox.setSelected(true);
                apareceBoton++;
            }
        });
       
        matrizCheck.add(confirmar, 0, j + 1);
        matrizCheck.add(seleccionarTodo, 0, j + 2);
    }

    public void guardarEnTXT(String texto, String nombreArchivo) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {

            //Guardamos el archivo en la carpeta del proyecto
            fichero = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fichero);

            pw.print(texto);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {

                if (null != fichero) {
                    System.out.println("Se ha creado el archivo: " + nombreArchivo);
                    fichero.close();
                }

            } catch (Exception e2) {

                e2.printStackTrace();
            }
        }
    }
}
