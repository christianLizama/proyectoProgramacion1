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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.R;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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
import javafx.scene.text.Text;
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
    private AnchorPane ventanaPDF;
    
    double xOffset = 0; 
    double yOffset = 0;
   
    
    
    int bandera=0;
    
    double origenX=0;
    double origenY=0;
    
    double ancho=0;
    double alto=0;
    
    int indi=0;
    
    
     
    ToggleButton botonDibujarr = new ToggleButton();
    ToggleButton botonBorrarr = new ToggleButton();
    Button botonIzqq = new Button();
    Button botonDerr = new Button();
    
    @FXML
    private ImageView imagenBotones;
    int largoBtn;

    final KeyCombination controlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
    
    final KeyCombination controly = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);    

    
    Pane imagenfull = new Pane();
    Pane escenaCompleta = new Pane();//La escena completa
    
    
    Stack<Pane> pilaControlZ = new Stack<>();
    Pane dibujos = new Pane();
    
    
    @FXML
    private ImageView ImagenPrincipal;
    
    int opcion=0;
    
    
    //Imagenes botones
    
    
    
    
    
    private void addKeyHandler(Scene scene) {
        escenaCompleta.setOnKeyPressed((event) -> {
            
            if (controlZ.match(event)) {
                 event.consume();    
                 System.out.println("pene");
                 controlMasZ();
             }       
             if(controly.match(event)){
                 event.consume();
                 //controlMasY();
             }          

        });
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
      
        
      
    }    
    public int o=0;
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
                    
                    

                    
                    Image botonDi = new Image(new File("botonDib.png").toURI().toString(),35,40,false,true);
                    ImageView botonDib = new ImageView(botonDi);

                    Image botonBo = new Image(new File("botonBorrar.png").toURI().toString(),35,40,false,true);
                    ImageView botonBorrar2 = new ImageView(botonBo);

                    Image botonIzq = new Image(new File("botonIzq.png").toURI().toString(),35,40,false,true);
                    ImageView botonIzquieda = new ImageView(botonIzq);
                    
                    
                    
                    Image botonDer = new Image(new File("botonDer.png").toURI().toString(),35,40,false,true);
                    ImageView botonDerecha = new ImageView(botonDer);
                    
                    botonDibujarr.setGraphic(botonDib);
                    botonDibujarr.setStyle("-fx-base: white;");

                    botonIzqq.setGraphic(botonIzquieda);
                    botonIzqq.setStyle("-fx-base: white;");

                    botonDerr.setGraphic(botonDerecha);
                    botonDerr.setStyle("-fx-base: white;");

                    botonBorrarr.setGraphic(botonBorrar2);
                    botonBorrarr.setStyle("-fx-base: white;");
        
                    
                    imagenfull.getChildren().add(imagenPDF);
                    imagenfull.setLayoutY(45);
                    imagenfull.setLayoutX(0);
                   
                    //imagenPDF.setLayoutY(45);
                    imagenPDF.setLayoutX(0);
                    //imagenPDF.setLayoutY(45);
                    
                    
                    
                    Image botonDis = new Image(new File("botonDib.png").toURI().toString(),35,40,false,true);
                    ImageView botonDibs = new ImageView(botonDis);
                  
                    Button pene = new Button();
                    pene.setLayoutY(100);
                    
                     
                    
                    
                    
                    escenaCompleta.getChildren().addAll(root1,imagenfull,dibujos,botonDibujarr,botonIzqq,botonDerr,botonBorrarr);// Se añade la pantalla de editar y la imagen del PDF
                    Scene escene = new Scene(escenaCompleta,700, 900);//Se carga la escena total
                    imagenPDF.setFitHeight(escenaCompleta.getHeight());
                    imagenPDF.setFitWidth(escenaCompleta.getWidth());
                    
                    botonBorrarr.setLayoutX(153);
                    botonIzqq.setLayoutX(51);
                    botonDerr.setLayoutX(102);
                   
                    
                    
                   
                    final ToggleGroup Grupito = new ToggleGroup();
                    Grupito.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
                        @Override
                        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                            if(newValue == null){
                                
                                System.out.println("chao");
                                
                                
                                escenaCompleta.setOnMouseClicked((event) -> { //PARA QUE NO HAGA NADA JAJAJAJAJ                  
                                
                                });
                            }
                            if(botonBorrarr.isSelected()){
                                eliminar();
                            }
                            
                            if(botonDibujarr.isSelected()){
                                
                                System.out.println("holi");
                               
                                
                                escenaCompleta.setOnMousePressed(new EventHandler<MouseEvent>() {
//                               
                                        @Override
                                        public void handle(MouseEvent event) {

                                            //if(event.getSceneY()>=45){
                                            double xAux,yAux;

                                            xAux=event.getSceneX();
                                            yAux=event.getSceneY();

                                            if (!(xAux>1 && xAux<=700   && yAux>0 && yAux<=45)) {
                                                xOffset = xAux;
                                                yOffset = yAux;

                                            }
                                            else{
                                                System.out.println(bandera);
                                                xOffset=0;
                                                yOffset=0;
                                                bandera=0;
                                            }



                                            //}
                                            System.out.println(bandera);
                                            System.out.println(xOffset+"  "+yOffset);
                                            System.out.println(ancho);
                                            System.out.println(alto);
                                        }
                                
                                
                                    });
                                    
                                    //Dibuja el rectangulo
                                    escenaCompleta.setOnMouseClicked((events)->{
                                    

                                        if(xOffset>1 && xOffset<=700 && yOffset>45 && yOffset<900){
                                            bandera++;
                                        }


                                        if (bandera==1) {
                                            origenX=xOffset;
                                            origenY=yOffset;
                                        }



                                        if (bandera==2 ) {

                                            ancho=xOffset-origenX;
                                            alto=yOffset-origenY;

                                            System.out.println("Bandera"+bandera);


                                            if(ancho<=0 || alto<=0 || origenX==0 || origenY==0 || xOffset==0 || yOffset==0 ){
                                                xOffset=0;
                                                yOffset=0;
                                                alto=0;
                                                ancho=0;

                                                bandera=0;

                                            }
                                            else{
                                                //Pane cuadrado = new Pane();
                                                Button botonEliminar = new Button(); //se crea el boton para cada rectangulo
                                                botonEliminar.setLayoutX(origenX);
                                                botonEliminar.setLayoutY(origenY);
                                                botonEliminar.setPrefSize(ancho, alto); 
                                                botonEliminar.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
                                                botonEliminar.setText(String.valueOf(indi));//se le asigna un numero

                                                indi++;

                                                //cuadrado.getChildren().add(botonEliminar);
                                                dibujos.getChildren().add(botonEliminar);


                                                //pilaControlZ.push(dibujos);

                                                //System.out.println("Cantidad de rectangulos dibujados : " + dibujos.getChildren().size());
                                                //System.out.println("hijos de primer anchor: "+ stackControlZ.peek().getChildren().size() );
                                                //System.out.println("Cantidad de hijos de paren pila: "+ pilaControlZ.peek().getChildren().size());
                                                //System.out.println(dibujos.getChildren().size());
                                                
                                                xOffset=0;
                                                yOffset=0;

                                                bandera=0;
                                                
                                                

                                            }






                                        }          
                                    });
                                    
                                    
                                
                            }
                        }
                        
                    });
                    
                    botonDibujarr.setToggleGroup(Grupito);
                    botonBorrarr.setToggleGroup(Grupito);
//                  
                    
                    addKeyHandler(escene);
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
   
    
    public void eliminar (){
        for (int i = 0; i < dibujos.getChildren().size(); i++) { //se recorre la lista de botones
                    
            Button boton = (Button)dibujos.getChildren().get(i); //obtiene el boton que esta en la poscision i

            boton.setOnAction((ActionEvent events) -> { //sucede la accion
                int num =  Integer.parseInt(boton.getText()); //obtengo el numero del boton
                

                for (int j = 0; j < dibujos.getChildren().size(); j++) { //se recorre los hijos del panel
                    Button boton2 = (Button)dibujos.getChildren().get(j); //obtengo los botones

                    if(boton.getText().equals(boton2.getText())){
                        dibujos.getChildren().remove(j); //se elimina del gridPane en la pos j
                        //stackControlZ.push(dibujos);
                    }
                }                            
            });          
        }
    }
   
    private void controlMasZ(){  
         
           try {
               if(!pilaControlZ.isEmpty()){
                    //stackControlZ.pop();
                    System.out.println("#pila: "+pilaControlZ.size());
                    //anchor=anchor2;



                    //System.out.println("Hijos de anchor1 :" + stackControlZ.peek().getChildren().size());
                    System.out.println("#pila segundo tamaño: "+pilaControlZ.size());
                    dibujos.getChildren().remove(pilaControlZ.size()-1);
                    pilaControlZ.pop();
                }
            } catch (Exception e) {
            }
           
    }
    
    
    
    
    
    
    
        
    
}
