/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;
import com.google.gson.GsonBuilder;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;



/**
 * FXML Controller class
 *
 * @author Diego Aguilera
 */
public class Pantalla2Controller implements Initializable {
    
    //botones del programa
    ToggleButton botonDibujar = new ToggleButton();
    ToggleButton botonBorrar = new ToggleButton();
    Button botonIzqq = new Button();
    Button botonDerr = new Button();
    
    //coordenadas de dibujo
    double xOffset = 0; 
    double yOffset = 0;
    double origenX=0;
    double origenY=0;
    
    GsonBuilder gsonBuilder = new GsonBuilder();
    
    
    @FXML
    private AnchorPane ventanaPDF;
    
    @FXML
    private ImageView imagenBotones;
    
    @FXML
    private ImageView ImagenPrincipal;
    
    
    //la escena completa que carga todos los elementos
    Pane escenaCompleta = new Pane();
    
    //contenedor de rectangulos dibujados
    Pane dibujos = new Pane();
    
    GuardarContenidoPane pane;
    
    
    //bandera que se utiliza para verificar que se hizo un click permitido
    int bandera=0;
    int contadorPizarra=0;

    //atajos de teclado (aun no fucionales)
    final KeyCombination controlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);

    final KeyCombination controly = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);    

    //Stack<Pane> pilaControlZ = new Stack<>();
    
    //Proporciones de pantalla
    double anchoPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    double altoPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
    double razon1 = 0.4;
    double razon2 = 0.9;
            
   
    //Rectangulos
    Rectangulos rectangulo;
    UndoAndRedo estados = new UndoAndRedo();
    GuardarContenidoPane guardados = new GuardarContenidoPane();
    
    
    
    
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
        
        
        if(pdfFile!=null){
            
            try (PDDocument documento = PDDocument.load(pdfFile)) {
                
                PDFRenderer pdfRenderer = new PDFRenderer(documento);
                //recorre todas las paginas que posea un pdf (en este caso se usaran pdf de una sola pag)
                
                    //Numero de pagina, escala, tipo de imagen
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

                    //Se guarda en la carpeta del proyecto el pdf convertido en imagen
                    File file = new File("imagen.png");
                    ImageIO.write(bim, "png", file);
                    File outputfile = new File("imagen.png");
                    ImageIO.write(bim, "png", outputfile);//Se crea el archivo

                    Image image = new Image(new File("imagen.png").toURI().toString());//Se carga la imagen
                    ImageView imagenPDF = new ImageView(image);//Se crea un archivo de tipo imageView para poder visualizar

                    //Se definen los tamaños de la imagen

                    cargadorDeEscena(imagenPDF);

                
            
                documento.close();
            
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
    
    public void cargadorDeEscena (ImageView imagenPDF) {
        
        try {
            Stage stage = new Stage();//Se crea el Escenario

            //Se carga el archivo fxml en un parent
            Parent root1 = FXMLLoader.load(getClass().getResource("Pantalla2.fxml"));//La pantalla 2

            setImagenesBotones();
            //contenedor de imagen
            Pane imagenfull = new Pane();
            
            imagenfull.getChildren().add(imagenPDF);
            imagenfull.setLayoutY(45);
            imagenfull.setLayoutX(0);

            imagenPDF.setLayoutX(0);
            
            
            
            escenaCompleta.getChildren().addAll(root1,imagenfull,dibujos,botonDibujar,botonIzqq,botonDerr,botonBorrar);// Se añade la pantalla de editar y la imagen del PDF
            Scene escene = new Scene(escenaCompleta,anchoPantalla*razon1, altoPantalla*razon2);//Se carga la escena completa en la escena que se mostrará
            
            
            imagenPDF.setFitHeight(escene.getHeight()-45);
            imagenPDF.setFitWidth(escene.getWidth());

            botonBorrar.setLayoutX(153);
            botonIzqq.setLayoutX(51);
            botonDerr.setLayoutX(102);

            final ToggleGroup GrupoBotones = new ToggleGroup();
           
            GrupoBotones.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
                @Override
                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                    if(newValue == null){
                        //System.out.println("No hay boton pulsado");
                    }
                    //al tener el boton borrar pulsado se permite borrar
                    if(botonBorrar.isSelected()){
                        //System.out.println("Se ha pulsado el boton borrar");
                        eliminarRectangulo();
                    }
                    //al tener el boton dibujar pulsado se permite dibujar
                    if(botonDibujar.isSelected()){
                        //System.out.println("Se ha pulsado el boton dibujar");
                        dibujarRectangulos();
                    }
                    //si el boton dibujar no se encuentra pulsado no se permite obtener coordenadas
                    else if(!botonDibujar.isPressed()){
                        escenaCompleta.setOnMouseClicked(null);
                        escenaCompleta.setOnMousePressed(null);
                    }
                }
            });
            //se añaden los botones al grupo que los contiene
            botonDibujar.setToggleGroup(GrupoBotones);
            botonBorrar.setToggleGroup(GrupoBotones);
            
            addKeyHandler(escene);
            
            stage.setResizable(false);
            stage.setScene(escene);//Se monta la escena en el escenario
            stage.show();//Se muestra el escenario
            
            
        } catch (IOException ex) {
            Logger.getLogger(Pantalla1FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    //Funcion que permite dibujar rectangulos
    public void dibujarRectangulos(){
        
        escenaCompleta.setOnMousePressed(new EventHandler<MouseEvent>() {
            
            //Se obtienen las primeras cordenadas para hacer un dibujo                     
            @Override
            public void handle(MouseEvent event) {
                
                double xAux,yAux;
                
                xAux=event.getSceneX();
                yAux=event.getSceneY();
                
                if (!(xAux>1 && xAux<=anchoPantalla*razon1   && yAux>0 && yAux<=45)) {
                    xOffset = xAux;
                    yOffset = yAux;

                }
                else{
                    //System.out.println(bandera);
                    xOffset=0;
                    yOffset=0;
                    bandera=0;
                }

//                System.out.println("Bandera: "+bandera);
//                System.out.println("Coordenadas: " + "x: "+ xOffset + " y: "+yOffset);
//                System.out.println("ancho: " + ancho);
//                System.out.println("alto: " + alto);
            }


        });
        
        //Se dibuja el rectangulo
        escenaCompleta.setOnMouseClicked((events)->{
            double ancho=0;
            double alto=0;
            //Se limita el margen donde se puede hacer click para dibujar
            if(xOffset>1 && xOffset<=anchoPantalla*razon1 && yOffset>45 && yOffset<(altoPantalla*razon2)){
                bandera++;
            }


            if (bandera==1) {
                origenX=xOffset;
                origenY=yOffset;
            }
            
            if (bandera==2 ) {

                ancho=xOffset-origenX;
                alto=yOffset-origenY;

                //System.out.println("Bandera"+bandera);


                if(ancho<=0 || alto<=0 || origenX==0 || origenY==0 || xOffset==0 || yOffset==0 ){
                    xOffset=0;
                    yOffset=0;
                    alto=0;
                    ancho=0;

                    bandera=0;

                }
                else{
                    rectangulo = new Rectangulos();
                    rectangulo.setX(origenX);
                    rectangulo.setY(origenY);
                    rectangulo.setAlto(alto);
                    rectangulo.setAncho(ancho);
                    rectangulo.setPosicionEnPantalla();
                  
                    ArrayList<Button> rectangulosAux = new ArrayList<>();
                    
                    //Se agrega el rectangulo al contenedor de dibujos
                    dibujos.getChildren().add(rectangulo.getBotonRectangulo());
                    dibujos.setVisible(true);
                    estados.pilaY.clear();
                    //Se agregan al arraylist todos los dibujos creados
                    for (int i = 0; i < dibujos.getChildren().size(); i++) {
                        rectangulosAux.add((Button)dibujos.getChildren().get(i));
                    }
                    
                    guardados.setRectangulos(rectangulosAux);
                    
                    //Se agrega el estado del programa a la pila
                    estados.agregarPila(guardados);
                    estados.imprimir();

                    xOffset=0;
                    yOffset=0;
                    bandera=0;

                }
            }          
        });
    }
    
    //metodo que elimina rectangulos
    public void eliminarRectangulo (){
        for (int i = 0; i < dibujos.getChildren().size(); i++) { //se recorre la lista de botones
                    
            Button boton = (Button)dibujos.getChildren().get(i); //obtiene el boton que esta en la poscision i

            boton.setOnAction((ActionEvent events) -> { //sucede la accion
                
                for (int j = 0; j < dibujos.getChildren().size(); j++) { //se recorre los hijos del panel
                    Button boton2 = (Button)dibujos.getChildren().get(j); //obtengo los botones

                    if(boton.equals(boton2)){
                        dibujos.getChildren().remove(j); //se elimina del gridPane en la pos j
                        ArrayList<Button> rectangulosAux = new ArrayList<>();
                    
                        for (int k = 0; k < dibujos.getChildren().size(); k++) {
                            rectangulosAux.add((Button)dibujos.getChildren().get(k));
                        }
                        
                        guardados.setRectangulos(rectangulosAux);
                        //Se agrega el cambio efectuado a la pila de control Z
                        estados.agregarPila(guardados);
                        estados.imprimir();
                    }
                }                            
            });          
        }
    }
    //metodo que añade las imagenes a los botones
    public void setImagenesBotones(){
        
        //Iconos de los botones
        Image imagenDibujar = new Image(new File("botonDib.png").toURI().toString(),35,36,false,true);
        ImageView iconoDibujar = new ImageView(imagenDibujar);

        Image imagenBorrar = new Image(new File("botonBorrar.png").toURI().toString(),35,36,false,true);
        ImageView iconoBorrar = new ImageView(imagenBorrar);

        Image imagenAtras = new Image(new File("botonIzq.png").toURI().toString(),35,36,false,true);
        ImageView iconoVolverAtras = new ImageView(imagenAtras);

        Image imagenAdelante = new Image(new File("botonDer.png").toURI().toString(),35,36,false,true);
        ImageView iconoVolverAdelante = new ImageView(imagenAdelante);
        
        botonDibujar.setGraphic(iconoDibujar);
        botonDibujar.setStyle("-fx-base: white;");

        botonIzqq.setGraphic(iconoVolverAtras);
        botonIzqq.setStyle("-fx-base: white;");

        botonDerr.setGraphic(iconoVolverAdelante);
        botonDerr.setStyle("-fx-base: white;");

        botonBorrar.setGraphic(iconoBorrar);
        botonBorrar.setStyle("-fx-base: white;");
        
    }

    
    public  void addKeyHandler(Scene scene) {
        
        scene.setOnKeyPressed((event) -> {
            //control Z
            if (controlZ.match(event)) {
                event.consume(); 
                
                System.out.println("Se ha usado control Z");
                //ArrayList que almacena los rectangulos del estado anterior
                ArrayList rectangulosAux;
                rectangulosAux = estados.controlZ();//se cargan los rectangulos del estado anterior
                
                if(dibujos.getChildren().size()==1){//si solo existe un rectangulo se elimina y se actualiza la ventana
                    
                    dibujos.getChildren().clear();//se eliminan todos los rectangulos
                    dibujos.setVisible(false);
                    
                }
                else{
                    
                    if(!rectangulosAux.isEmpty()){
                        
                        dibujos.getChildren().clear();
                        
                        for (int i = 0; i <rectangulosAux.size(); i++){//Agregamos los rectangulos anteriores al Pane de dibujos
                           dibujos.getChildren().add((Button)rectangulosAux.get(i));
                        }
                    }
                }
            }
            //control Y
            if(controly.match(event)){
                event.consume();
                System.out.println("Se ha pulsado control Y");
                 
                
             }          
        });
    }    
}
