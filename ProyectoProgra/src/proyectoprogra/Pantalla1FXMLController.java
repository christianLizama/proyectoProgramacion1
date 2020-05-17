/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Pantalla2Controller pantalla2 = new Pantalla2Controller();
        examinar.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar PDF");

            // Agregar filtros para facilitar la busqueda
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF", "*.pdf")
            );

            // Obtener pdf seleccionado
            File pdfFile = fileChooser.showOpenDialog(null);

            // Mostar la imagen
            if (pdfFile != null) {
                ((Node) (event.getSource())).getScene().getWindow().hide();//cerrar ventana
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Pantalla2.fxml"));
                    
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);

                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(Pantalla1FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                pantalla2.PDF2Imagen(pdfFile);
                    
                
                
                

            }
        });
    }    
    
}
