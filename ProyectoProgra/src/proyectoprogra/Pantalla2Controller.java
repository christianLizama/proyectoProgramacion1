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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


/**
 * FXML Controller class
 *
 * @author Diego Aguilera
 */
public class Pantalla2Controller implements Initializable {

    @FXML
    private Button botonDibujar;
    @FXML
    private Button botonDeshacer;
    @FXML
    private Button botonRehacer;
    @FXML
    private Button botonBorrar;
    @FXML
    private ImageView ImagenPrincipal;

    public void setImagenPrincipal(ImageView ImagenPrincipal) {
        this.ImagenPrincipal = ImagenPrincipal;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    public void PDF2Imagen(File pdfFile){
        
        try (PDDocument documento = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(documento);
            //recorre todas las paginas
            for (int page = 0; page < documento.getNumberOfPages(); page++) {
                //Numero de pagina, escala, tipo de imagen
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 50, ImageType.RGB);
                //Se guarda en disco
                File file = new File("imagen_" + page + ".png");
                ImageIO.write(bim, "png", file);
                File outputfile = new File("C:\\Users\\Diego Aguilera\\Downloads\\imagen_0.png");
                ImageIO.write(bim, "png", outputfile);
                
                File imagen = new File("C:\\Users\\Diego Aguilera\\Downloads\\imagen_0.png");
                Image image = new Image(imagen.toURI().toString());
                
                ImageView iv = new ImageView(image);
                
                
                setImagenPrincipal(iv);
                
                
              
                
                
               
                        
                
                
            
                
                
                
                
                
                
                
            }
            documento.close();
            
            
        } catch (IOException ex) {
            System.err.println(ex);
        }  
    }
}
