/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import com.sun.glass.ui.Cursor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


/**
 * FXML Controller class
 *
 * @author sebas
 */
public class Pantalla1FXMLController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button examinar;
    @FXML
    private TextField nombrePDF;
    @FXML
    private Button cargarPDF;
    
    
    
    Pantalla2Controller pantalla2 = new Pantalla2Controller();
    
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        nombrePDF.setEditable(false);
        nombrePDF.setDisable(true);
        nombrePDF.setOpacity(100);
        
        examinar.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar PDF");

            // Agregar filtros para facilitar la busqueda
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF", "*.pdf")
            );

            // Obtener pdf seleccionado
            File pdfFile = fileChooser.showOpenDialog(null);
            
            if(pdfFile != null){
                nombrePDF.setText(pdfFile.getName());
                botonCargar(pantalla2, pdfFile);
            }
        });
    }


    public void botonCargar(Pantalla2Controller pantalla2,File pdfFile){
        
        cargarPDF.setOnAction((event) -> {
            
            ((Node) (event.getSource())).getScene().getWindow().hide();//cerrar ventana
            pantalla2.PDF2Imagen(pdfFile);
            
            
        });
            
    }
    
}
