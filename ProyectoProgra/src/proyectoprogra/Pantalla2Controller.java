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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.Buffer;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jdk.nashorn.internal.objects.NativeArray;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;



/**
 * FXML Controller class
 *
 * @author Diego Aguilera
 */
public class Pantalla2Controller implements Initializable{
    
    //botones del programa
    ToggleButton botonDibujar = new ToggleButton();
    ToggleButton botonBorrar = new ToggleButton();
    Button botonIzqq = new Button();
    Button botonDerr = new Button();
    Button botonGuardar = new Button();
    Button botonLeer = new Button();
    
    
    //coordenadas de dibujo
    double xOffset = 0; 
    double yOffset = 0;
    double origenX=0;
    double origenY=0;
    int contador=0;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Stage stage = new Stage();//Se crea el Escenario
    
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
    
    
    GuardadoJson JSON = new GuardadoJson();
    OCR lectorOcr = new OCR();
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

                lectorOcr.leerImagenEntera();

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

            //Se carga el archivo fxml en un parent
            Parent root1 = FXMLLoader.load(getClass().getResource("Pantalla2.fxml"));//La pantalla 2
            
            //ProyectoProgra.dibujos.getChildren().add(imagenPDF);
            
            setImagenesBotones();
            //contenedor de imagen
            Pane imagenFull= new Pane();
            
            TextArea ocr = new TextArea();
            ocr = lectorOcr.getTextoOCR();
            ocr.setLayoutX((anchoPantalla*razon1)+10);
            ocr.setLayoutY(45);
            ocr.setPrefSize(anchoPantalla*razon1, altoPantalla*razon2);
            
            imagenFull.getChildren().add(imagenPDF);
            imagenFull.setLayoutY(45);
            dibujos = ProyectoProgra.dibujos;

            
            imagenPDF.setLayoutX(0);
            
            escenaCompleta.getChildren().addAll(root1,imagenFull,dibujos,ocr,botonDibujar,botonIzqq,botonDerr,botonBorrar,botonGuardar,botonLeer);// Se añade la pantalla de editar y la imagen del PDF
            Scene escene = new Scene(escenaCompleta,(anchoPantalla*razon1)*2, altoPantalla*razon2);//Se carga la escena completa en la escena que se mostrará
            
            imagenPDF.setFitHeight(altoPantalla*razon2);
            imagenPDF.setFitWidth(anchoPantalla*razon1);
            imagenFull.setPrefSize(imagenPDF.getFitWidth(), imagenPDF.getFitHeight());
            ProyectoProgra.dibujos.setPrefSize(imagenPDF.getFitWidth(), imagenPDF.getFitHeight());
            dibujos.setLayoutY(45);
            botonBorrar.setLayoutX(153);
            botonIzqq.setLayoutX(51);
            botonDerr.setLayoutX(102);
            botonGuardar.setLayoutX(204);
            botonLeer.setLayoutX(255);

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
                        //dibujarRectangulos();
                        
                        dibujar2(dibujos);
                        //coordenadas(dibujos);
                    }
                    //si el boton dibujar no se encuentra pulsado no se permite obtener coordenadas
                    else if(!botonDibujar.isPressed()){
                        dibujos.setOnMousePressed(null);
                    }
                }
            });
            //Cuando apretamos el boton guardar
            botonGuardar.setOnAction((event) -> {
                
                FileChooser fileChooser = new FileChooser();
 
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json"); //Filtramos por json
                fileChooser.getExtensionFilters().add(extFilter); //Añadimos el filtro 

                File file = fileChooser.showSaveDialog(stage); //Muestra la escena para guardar el archivo
                
                if (file != null) {
                    JSON.crearJson(file.getPath(),guardados.getRectangulos()); //Generamos el json
                   
                }
                
            });
            
            //Se añade la funcion del boton para leer json
            leerPlantilla();
            pulsarBotonAtras();
            pulsarBotonAdelante();
            
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
    
    //Se lee una plantilla creada
    public void leerPlantilla(){
        //Cuando apretamos el boton Leer
            botonLeer.setOnAction((event) -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Buscar JSON");

                // Agregar filtros para facilitar la busqueda
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("json", "*.json")
                );
                
                // Obtener pdf seleccionado
                File jsonFile = fileChooser.showOpenDialog(null);
               
                if(jsonFile != null){
                    JSON.leerJson(jsonFile);
                    
                    
                    System.out.println(jsonFile.getName());
                    
                    ArrayList<Button> rectAux =JSON.getRectangulosLeidos();
                    
                    System.out.println("primer aux: " + rectAux.size());
                    
                    dibujos.getChildren().clear();
                    
                    for (Button rectLeidos : rectAux) {
                        dibujos.getChildren().add(rectLeidos);
                        
                        
                    }
                    rectAux.clear();
                    
                    
                    
                    
                }
                
            });
        
        
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
    
    double ancho1;
    double alto1;
    
    public void dibujar2(Pane dibujos){
        
        dibujos.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dibujos.setVisible(true);
                dibujos.setDisable(false);
                origenX = event.getX();
                origenY = event.getY();
                
                System.out.println("origen x:"+origenX);
                System.out.println("origen y:"+origenY);
                
                contador++;
                
                String rectanguloN= "rectangulo "+contador;
                System.out.println(rectanguloN);
                Tooltip nombre = new Tooltip();
                
                nombre.setText(rectanguloN);
                
                rectangulo = new Rectangulos();
                rectangulo.setX(origenX);
                rectangulo.setY(origenY);
                
                //Creamos un boton el cual se mostrará en pantalla
                Button nuevo = new Button();
                nuevo = rectangulo.getBotonRectangulo();
                rectangulo.setHacerPulsable(0);
                rectangulo.hacerPulsable();
                nuevo.setTooltip(nombre);
                //Se agrega el rectangulo al contenedor de dibujos
                dibujos.getChildren().add(nuevo);
                
                //Hacemos un set de su posición inicial en pantalla
                rectangulo.setPosicionEnPantalla();
                dibujos.setOnMousePressed(null);
                
                dibujos.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //X e Y en movimiento
//                        System.out.println("x: "+event.getSceneX());
//                        System.out.println("y: "+event.getSceneY());
                        
                        xOffset=event.getX();
                        yOffset=event.getY();

                        ancho1=xOffset-origenX;
                        alto1=yOffset-origenY;
                        
                        //System.out.println("ancho: "+ancho);
                        //System.out.println("alto: "+alto);
//                        
//                        if(ancho<0 && alto>0){
//                            
//                            ancho=origenX-xOffset;
//                            alto= yOffset-origenY;
//                            
//                            boton.setLayoutX(xOffset);
//                            //boton.setLayoutY(yOffset);
//                            
//                        }
//                        else if(ancho<0 && alto<0){
//                            
//                            ancho=origenX-xOffset;
//                            alto=origenY-yOffset;
//                            
//                            boton.setLayoutY(yOffset);
//                            boton.setLayoutX(xOffset);
//                            
//                        }
//                        else if(alto<0 && ancho>0){
//                            
//                            ancho=xOffset-origenX;
//                            alto=origenY-yOffset;
//                            
//                            boton.setLayoutY(yOffset);
//                            
//                        }
                        
                        //System.out.println("");
                        
                        //Hacemos un set del alto y ancho que es variable hasta
                        //que se haga un click de termino
                        rectangulo.setAlto(alto1);
                        rectangulo.setAncho(ancho1);
                        rectangulo.setPosicionEnPantalla();
                        
                        dibujos.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                rectangulo.setHacerPulsable(1);
                                rectangulo.hacerPulsable();
                                dibujos.setOnMouseMoved(null);
                                dibujos.setOnMousePressed(null);
                                dibujos.setOnMouseClicked(null);
                                
                                //Reiniciamos las variables para crear otro
                                ancho1=0;
                                alto1=0;
                                xOffset=0;
                                yOffset=0;
                                
                                //Creamos un arraylist el cual servira para guardar todos los rectangulos creados
                                ArrayList<Button> rectangulosAux = new ArrayList<>();
                                
                                
                                estados.pilaY.clear();
                                //Se agregan al arraylist todos los dibujos creados
                                for (int i = 0; i < dibujos.getChildren().size(); i++) {
                                    rectangulosAux.add((Button)dibujos.getChildren().get(i));
                                }

                                guardados.setRectangulos(rectangulosAux);

                                //Se agrega el estado del programa a la pila
                                estados.agregarPila(guardados);
                                estados.imprimir();
                                
//                                System.out.println("medidas rectangulo");
//                                System.out.println("x: " + boton.getLayoutX());
//                                System.out.println("y: " + boton.getLayoutY());
//                                System.out.println("width: "+boton.getWidth());
//                                System.out.println("height: "+boton.getHeight());
                                event.consume();
                                dibujar2(dibujos);
                            }
                        });
                    }
                });
            }
        });
    }
    
    public void coordenadas(Pane root){
        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("x: "+event.getX());
                System.out.println("y: "+ event.getY());
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
        
        Image imagenGuardar = new Image(new File("botonGuardar.png").toURI().toString(),35,36,false,true);
        ImageView iconoGuardar = new ImageView(imagenGuardar);
        
        Image imagenLeer = new Image(new File("botonLeer.png").toURI().toString(),35,36,false,true);
        ImageView iconoLeer = new ImageView(imagenLeer);
        
        
        botonDibujar.setGraphic(iconoDibujar);
        botonDibujar.setStyle("-fx-base: white;");

        botonIzqq.setGraphic(iconoVolverAtras);
        botonIzqq.setStyle("-fx-base: white;");

        botonDerr.setGraphic(iconoVolverAdelante);
        botonDerr.setStyle("-fx-base: white;");

        botonBorrar.setGraphic(iconoBorrar);
        botonBorrar.setStyle("-fx-base: white;");
        
        botonGuardar.setGraphic(iconoGuardar);
        botonGuardar.setStyle("-fx-base: white;");
        
        botonLeer.setGraphic(iconoLeer);
        botonLeer.setStyle("-fx-base: white;");
        
    }
    
    public void pulsarBotonAtras(){
        
        botonIzqq.setOnAction((event) -> {
            
            funcionZ();
            
        });
        
    }
    
    public void pulsarBotonAdelante(){
        
        botonDerr.setOnAction((event) -> {
            
            funcionY();
            
        });
        
    }
    
    public void funcionZ(){
        
        System.out.println("Se ha usado control Z");
        //ArrayList que almacena los rectangulos del estado anterior
        ArrayList rectangulosAux;
        rectangulosAux = estados.controlZ();//se cargan los rectangulos del estado anterior

        if(dibujos.getChildren().size()==1){//si solo existe un rectangulo se elimina y se actualiza la ventana

            //dibujos.getChildren().clear();//se eliminan todos los rectangulos
            dibujos.getChildren().remove(0);
            Button arreglo = new Button();
            arreglo.setPrefSize(30, 40);

            dibujos.getChildren().add(arreglo);

            dibujos.getChildren().remove(0);
            //Creamos un arraylist el cual servira para guardar todos los rectangulos creados
            ArrayList<Button> rectangulosNew = new ArrayList<>();

            //Se agregan al arraylist todos los dibujos creados
            for (int i = 0; i < dibujos.getChildren().size(); i++) {
                rectangulosNew.add((Button)dibujos.getChildren().get(i));
            }
            guardados.setRectangulos(rectangulosNew);


        }
        else{

            if(!rectangulosAux.isEmpty()){

                dibujos.getChildren().clear();

                for (int i = 0; i <rectangulosAux.size(); i++){//Agregamos los rectangulos anteriores al Pane de dibujos
                   dibujos.getChildren().add((Button)rectangulosAux.get(i));
                }

                //Creamos un arraylist el cual servira para guardar todos los rectangulos creados
                ArrayList<Button> rectangulosNew = new ArrayList<>();

                //Se agregan al arraylist todos los dibujos creados
                for (int i = 0; i < dibujos.getChildren().size(); i++) {
                    rectangulosNew.add((Button)dibujos.getChildren().get(i));
                }
                guardados.setRectangulos(rectangulosNew);
            }
        }
        
    }
    public void funcionY(){
        
        System.out.println("Se ha pulsado control Y");
        
        ArrayList rectangulosAux2;//Se almacenan los rectangulos que se extraen de control z

        rectangulosAux2 = estados.controlY();//se carga el arraylist 

        if(!rectangulosAux2.isEmpty()){

            dibujos.setVisible(true);
            dibujos.getChildren().clear();

            for (int i = 0; i <rectangulosAux2.size(); i++) {
               dibujos.getChildren().add((Button)rectangulosAux2.get(i));
            }

            //Creamos un arraylist el cual servira para guardar todos los rectangulos creados
            ArrayList<Button> rectangulosNew = new ArrayList<>();

            //Se agregan al arraylist todos los dibujos creados
            for (int i = 0; i < dibujos.getChildren().size(); i++) {
                rectangulosNew.add((Button)dibujos.getChildren().get(i));
            }
            guardados.setRectangulos(rectangulosNew);
        }
        
    }
    
    
    
    
    
    
    public  void addKeyHandler(Scene scene) {
        
        scene.setOnKeyPressed((event) -> {
            //control Z
            if (controlZ.match(event)) {
                
                funcionZ();
                event.consume();
            }
            //control Y
            if(controly.match(event)){
                funcionY();
                event.consume();
            }
        });
    }
}
