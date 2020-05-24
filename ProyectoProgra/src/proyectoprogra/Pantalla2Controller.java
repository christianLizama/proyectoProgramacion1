/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import proyectoprogra.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.R;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
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
    
    double xOffset = 0; 
    double yOffset = 0;
    private Canvas canvas = new Canvas();
    
    int bandera=0;
    
    double origenX=0;
    double origenY=0;
    
    double ancho=0;
    double alto=0;
    
    int indi=0;
    
    GraphicsContext gc;
    @FXML
    private ImageView imagenBotones;
    int largoBtn;

   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        
        
    }    
    
    public void PDF2Imagen(File pdfFile) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        largoBtn=width/19;
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
                
               
                
                try {
                    Stage stage = new Stage();//Se crea el Escenario
                    
                    Parent root1 = FXMLLoader.load(getClass().getResource("Pantalla2.fxml"));//La pantalla 2
                    
                    
                    Pane escenaCompleta = new Pane();//La escena completa
                    
                    Pane imagenfull = new Pane();
                    imagenfull.getChildren().add(imagenPDF);
                    imagenfull.setLayoutY(45);
                    imagenfull.setLayoutX(0);
                   
                    
                    
                    
                    //imagenPDF.setLayoutY(45);
                    imagenPDF.setLayoutX(0);
                    //imagenPDF.setLayoutY(45);
                    
                    escenaCompleta.getChildren().addAll(root1,imagenfull);// Se añade la pantalla de editar y la imagen del PDF
                    Scene escene = new Scene(escenaCompleta,700, 900);//Se carga la escena total
                    imagenPDF.setFitHeight(escenaCompleta.getHeight());
                    imagenPDF.setFitWidth(escenaCompleta.getWidth());
                    
                    
                   
                    imagenfull.setOnMouseClicked((events)->{
                        
                        bandera++;
                        if (bandera==1) {
                            origenX=xOffset;
                            origenY=yOffset;
                        }
                        if (bandera==2) {
                            System.out.println("pene");
                            ancho=xOffset-origenX;
                            alto=yOffset-origenY;
                            Button botonEliminar = new Button(); //se crea el boton para cada rectangulo
                            botonEliminar.setLayoutX(origenX);
                            botonEliminar.setLayoutY(origenY);
                            botonEliminar.setPrefSize(ancho, alto); 
                            botonEliminar.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
                            botonEliminar.setText(String.valueOf(indi));//se le asigna un numero
                            indi++;
                            imagenfull.getChildren().addAll(botonEliminar);
                            bandera=0;
                        }          
                    }); 
                    
             
                    
                 
                    
                    //stage.setFullScreen(true);
                    //stage.setResizable(false);
                    imagenfull.setOnMousePressed(new EventHandler<MouseEvent>() { 
                        @Override
                        public void handle(MouseEvent event) {
                            
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY()-45;
                            System.out.println(xOffset+"  "+yOffset);
                        }
                    });
                    
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
