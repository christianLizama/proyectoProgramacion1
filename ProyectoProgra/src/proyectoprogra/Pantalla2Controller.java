/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;
import proyectoprogra.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.R;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
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
    private AnchorPane ventanaPDF;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    public void PDF2Imagen(File pdfFile) {
        if(pdfFile!=null){
            
            try (PDDocument documento = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(documento);
            //recorre todas las paginas
            for (int page = 0; page < documento.getNumberOfPages(); page++) {
                //Numero de pagina, escala, tipo de imagen
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 200, ImageType.RGB);
                
                //Se guarda en la carpeta del proyecto el pdf convertido en imagen
                File file = new File("imagen_" + page + ".png");
                ImageIO.write(bim, "png", file);
                File outputfile = new File("imagen_0.png");
                ImageIO.write(bim, "png", outputfile);//Se crea el archivo
                
                
                Image image = new Image(new File("imagen_0.png").toURI().toString());//Se carga la imagen
                ImageView imagenPDF = new ImageView(image);//Se crea un archivo de tipo imageView para poder visualizar
                
                //Se definen los tamaños de la imagen
                imagenPDF.setFitHeight(1800);
                imagenPDF.setFitWidth(1080);
               
                
                try {
                    
                    Parent root1 = FXMLLoader.load(getClass().getResource("Pantalla2.fxml"));//La pantalla 2
                    StackPane escenaCompleta = new StackPane();//La escena completa
                    AnchorPane ventanaImagen = new AnchorPane();//Contiene el scroll
                    
                    ScrollPane scrollPDF = new ScrollPane();//El scroll del PDF
                    scrollPDF.setContent(imagenPDF);//Se pone la imagen en scroll
                    scrollPDF.setLayoutY(55);
                    scrollPDF.setPrefSize(1080, 720);
                    scrollPDF.setStyle("fx-border-color: white;");
                 
                    
                    ventanaImagen.getChildren().add(scrollPDF);//Se carga el scroll a la ventana
                    ventanaImagen.setLayoutY(45);
                    escenaCompleta.getChildren().addAll(root1,ventanaImagen);// Se añade la pantalla de editar y la imagen del PDF
                    
                    Scene escene = new Scene(escenaCompleta);//Se carga la escena total
                    Stage stage = new Stage();//Se crea el Escenario
                    stage.setScene(escene);//Se monta la escena en el escenario

                    stage.show();//Se muestra el escenario
                    
                } catch (IOException ex) {
                    Logger.getLogger(Pantalla1FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
              
                }
                documento.close();
            
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
       
    }
}
