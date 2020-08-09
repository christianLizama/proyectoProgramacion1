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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
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
public class Pantalla2Controller implements Initializable{
    
    //botones del programa
    ToggleButton botonDibujar = new ToggleButton();
    ToggleButton botonBorrar = new ToggleButton();
    Button botonIzqq = new Button();
    Button botonDerr = new Button();
    Button botonGuardar = new Button();
    Button botonLeer = new Button();
    Button estaSeguro = new Button();
    Button botonAlternar = new Button();
    
    //coordenadas de dibujo
    double xOffset = 0; 
    double yOffset = 0;
    double origenX = 0;
    double origenY = 0;
    int contador = 0;
    
    GsonBuilder gsonBuilder = new GsonBuilder();
    Stage stage = new Stage();//Se crea el Escenario
    
    //la escena completa que carga todos los elementos
    Pane escenaCompleta = new Pane();
    
    
    //Contenedor de rectangulos dibujados
    Pane dibujos = new Pane();

    //Atajos de teclado 
    final KeyCombination controlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);

    final KeyCombination controly = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);    

    //Proporciones de pantalla
    double anchoPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    double altoPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
    double razon1 = 0.4;
    double razon2 = 0.9;
   
    //Rectangulos
    Rectangulos rectangulo;
    //Creación del objeto de la clase que contiene las funciones de undo and redo
    UndoAndRedo estados = new UndoAndRedo();
    //Creación del objeto de la clase que permite guardar los rectangulos
    GuardarContenidoPane guardados = new GuardarContenidoPane();
    
    //Creación del objeto de la clase que permite guardar los rectangulos como Json
    GuardadoJson JSON = new GuardadoJson();
    //Creaci+on del objeto de la clase que permite leer el texto del pdf
    OCR lectorOcr = new OCR();
    
    
    //Scroll que contiene el nombre del rectangulo con el texto extraido
    ScrollPane scrollContenidos = new ScrollPane();
    int cambiadorDeEscena=0;
    
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
                //recorre todas las paginas que posea un pdf (en este caso se usaran pdf de una sola pag)
                
                //Numero de pagina, escala, tipo de imagen
                //BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
                BufferedImage bim = pdfRenderer.renderImage(0, 5/2,ImageType.GRAY);
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
    
    public void activarBotonAlternar(){
        
        if(dibujos.getChildren().size()>=1){
            botonAlternar.setDisable(false);
        }
        else{
            botonAlternar.setDisable(true);
        }
    }
    
    
    public void cargadorDeEscena (ImageView imagenPDF) {
        
        //Definimos un contenedor total que contiene el scroll
        Pane contenedorTotal = new Pane();
        
        //Definimmos el scroll que contiene la imagen y la pizarra de dibujos
        ScrollPane dibujosScroll = new ScrollPane();
        
        //Contenedor de la imagen del PDF y de el contenedor de los rectangulos
        Pane union = new Pane();
        
        //Le agregamos las imagenes a los botones
        setImagenesBotones();
        
        //Agregamos la imagen y los dibujos a un pane que contendra ambos objetos
        union.getChildren().addAll(imagenPDF,dibujos);
        
        //Creamos el TextArea que contendra la lectura del documento completo
        TextArea ocr = new TextArea();
        
        //Obtenemos el texto del documento
        ocr = lectorOcr.getTextoOCR();
        
        //Asignamos propiedades del scroll con matriz
        scrollContenidos.setLayoutX((anchoPantalla*0.65)+10);
        scrollContenidos.setLayoutY(45);
        scrollContenidos.setPrefSize((anchoPantalla*razon1)-100, (altoPantalla*razon2)-45);
        scrollContenidos.setVisible(false);
        
        //Asignamos sus propiedades
        ocr.setLayoutX((anchoPantalla*0.65));
        ocr.setLayoutY(45);
        ocr.setPrefSize((anchoPantalla*0.35), (altoPantalla*razon2)-45);
        
        //Asignamos todas las propiedas al scroll
        setPropiedadesScroll(dibujosScroll,union);
        imagenPDF.setLayoutX(0);
        
        //Añadimos el scroll al contenedortotal
        contenedorTotal.getChildren().add(dibujosScroll);
        contenedorTotal.setLayoutY(45);
        estaSeguro.setLayoutX(306);
        
        //Añadimos todo a la escena completa
        escenaCompleta.getChildren().addAll(contenedorTotal,ocr,scrollContenidos,botonDibujar,botonIzqq,botonDerr,botonBorrar,botonGuardar,botonLeer,estaSeguro,botonAlternar);// Se añade la pantalla de editar y la imagen del PDF
        Scene escene = new Scene(escenaCompleta,anchoPantalla, altoPantalla);//Se carga la escena completa en la escena que se mostrará
        
        //Asignamos la posición de los botones
        botonIzqq.setLayoutX(51);
        botonDerr.setLayoutX(102);
        botonBorrar.setLayoutX(153);
        botonGuardar.setLayoutX(204);
        botonLeer.setLayoutX(255);
        botonAlternar.setLayoutX(357);
        
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
                    dibujar2(union,escene);
                    union.setCursor(Cursor.CROSSHAIR);
                    dibujosScroll.setPannable(false);
                    //coordenadas(dibujos);
                }
                //si el boton dibujar no se encuentra pulsado no se permite obtener coordenadas
                else if(!botonDibujar.isPressed()){
                    union.setOnMousePressed(null);
                    union.setCursor(Cursor.DEFAULT);
                    dibujosScroll.setPannable(true);
                }
            }
        });
        //Se añade la funcion de guardar una plantilla
        guardarPlantillaJson();
        
        //Se añade la funcion del boton para leer json
        leerPlantilla();
        
        //Se añade la funcion de control z en el boton que contiene la flecha hacia atras
        pulsarBotonAtras();
        
        //Se añade la funcion de control y en el boton que contiene la flecha hacia adelante
        pulsarBotonAdelante();
        
        botonAlternarAccion(ocr);
        
        //se añaden los botones al grupo que los contiene
        botonDibujar.setToggleGroup(GrupoBotones);
        botonBorrar.setToggleGroup(GrupoBotones);
        
        addKeyHandler(escene);
        stage.setMaximized(true);
        stage.setScene(escene);//Se monta la escena en el escenario
        stage.show();//Se muestra el escenario
 
    }
    
    public void botonAlternarAccion(TextArea ocr){
        
        botonAlternar.setOnAction((event) -> {
            
            if(cambiadorDeEscena==0){
                
                ocr.setVisible(false);

                lectorOcr.leerPorRectangulo(guardados.getRectangulos());
                MatrizDatos m = lectorOcr.getMatriz();

                GridPane grid = m.getMatrizCheck();

                scrollContenidos.setContent(grid);
                scrollContenidos.setVisible(true);
                cambiadorDeEscena=1;
            }
            else{
                
                ocr.setVisible(true);
                scrollContenidos.setVisible(false);
                cambiadorDeEscena=0;
                
                
            }
            
            
            
        });
    }
    
    //Le damos las propiedades al scroll
    public void setPropiedadesScroll(ScrollPane dibujosScroll,Pane union){
        dibujosScroll.setPannable(true);
        dibujosScroll.setContent(union);
        dibujosScroll.setPrefSize(anchoPantalla*0.65, (altoPantalla*razon2)-45);
        dibujosScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        dibujosScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }
    
    //Metodo que permite guardar la plantilla Json
    public void guardarPlantillaJson(){
        
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
    }
    
    
    //Metodo que muestra una alerta si se pulsa el boton eliminar plantilla
    public void eliminarPlantilla(File jsonFile){
        
        ButtonType yes = new ButtonType("Si");
        ButtonType no = new ButtonType("No");
        
        Alert eliminar = new Alert(AlertType.CONFIRMATION, "", yes,no);
        
        eliminar.setHeaderText("Está seguro que desea eliminar la plantilla");
       
        eliminar.setResizable(false);
        
        eliminar.showAndWait();
        
        ButtonType resultado = eliminar.getResult();
        
        if(resultado.getText().equals("Si")){
            jsonFile.delete();
            System.out.println("Se ha eliminado el archivo: "+jsonFile.getPath());
            //se deshabilita el boton eliminar ya que no hay plantilla guardada
            estaSeguro.setDisable(true);
            dibujos.getChildren().clear(); //Para limpiar la pantalla
            activarBotonAlternar();
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
                //Se habilita el boton eliminar ya que se cargó una plantilla en el programa
                estaSeguro.setDisable(false);
                JSON.leerJson(jsonFile);


                System.out.println(jsonFile.getName());

                ArrayList<Button> rectAux =JSON.getRectangulosLeidos();

                System.out.println("primer aux: " + rectAux.size());

                dibujos.getChildren().clear();

                for (Button rectLeidos : rectAux) {
                    dibujos.getChildren().add(rectLeidos);
                    ArrayList<Button> rectangulosAux = new ArrayList<>();
                    dibujos.setVisible(true);
                    estados.pilaY.clear();
                    //Se agregan al arraylist todos los dibujos creados
                    for (int i = 0; i < dibujos.getChildren().size(); i++) {
                        rectangulosAux.add((Button)dibujos.getChildren().get(i));
                    }

                    guardados.setRectangulos(rectangulosAux);

                    //Se agrega el estado del programa a la pila
                    estados.agregarPila(guardados);
                    System.out.println(dibujos.getChildren().size());


                }
                rectAux.clear();
                //Funcion eliminar plantilla cargada
                estaSeguro.setOnAction((event2) -> {

                    eliminarPlantilla(jsonFile);

                });
            }
        });
    }
    
    double ancho1;
    double alto1;
    
    public void dibujar2(Pane root3,Scene scene){
        
        root3.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dibujos.setVisible(true);
                dibujos.setDisable(false);
                origenX = event.getX();
                origenY = event.getY();
                
                System.out.println("origen x:"+origenX);
                System.out.println("origen y:"+origenY);
                
                rectangulo = new Rectangulos();
                rectangulo.setX(origenX);
                rectangulo.setY(origenY);
                
                //Creamos un boton el cual se mostrará en pantalla
                Button nuevo = new Button();
                
                nuevo = rectangulo.getBotonRectangulo();
                rectangulo.setHacerPulsable(0);
                rectangulo.hacerPulsable();
                
                //Se agrega el rectangulo al contenedor de dibujos
                dibujos.getChildren().add(nuevo);
                
                //Hacemos un set de su posición inicial en pantalla
                rectangulo.setPosicionEnPantalla();
                root3.setOnMousePressed(null);
                
                //Se permite que el rectangulo tenga un tamaño cambiante mientras el usuario mueve el mouse
                root3.setOnMouseMoved(new EventHandler<MouseEvent>() {
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
                        
                        //Ultimo click que define donde termina el rectangulo
                        root3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                rectangulo.setHacerPulsable(1);
                                rectangulo.hacerPulsable();
                                root3.setOnMouseMoved(null);
                                root3.setOnMousePressed(null);
                                root3.setOnMouseClicked(null);
                                
                                TextInputDialog dialog = new TextInputDialog();
                                dialog.setTitle("Nuevo Rectangulo");
                                dialog.setHeaderText("Agregue nombre a nuevo rectangulo");
                                dialog.setContentText("Nombre:");
                                quitarKeyHandler(scene);
                                
                                // Creamos una variable de tipo opcional por si el usuario se sale y no ingresa un nombre
                                // o simplemente pulsa cancelar
                                Optional<String> nombreRectangulo = dialog.showAndWait();
                                if (nombreRectangulo.isPresent()){
                                    System.out.println("Nuevo rectangulo: " + nombreRectangulo.get());
                                    
                                    Tooltip nombreRec = new Tooltip(nombreRectangulo.get());
                                    //Obtenemos el ultimo rectangulo dibujado
                                    int posicion = (dibujos.getChildren().size())-1;
                                    //Creamos un boton auxiliar para guardar el ultimo rectangulo dibujado y asi ponerle el nombre ingresado
                                    Button botonAux = (Button) dibujos.getChildren().get(posicion);
                                    //Le damos el nombre al rectangulo
                                    botonAux.setTooltip(nombreRec);
                                    dibujos.getChildren().remove(posicion);
                                    dibujos.getChildren().add(botonAux);
                                  
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
                                    activarBotonAlternar();
                                }
                                else{
                                    System.out.println("Se ha borrado el rectangulo por no ingresar su nombre");
                                    ancho1=0;
                                    alto1=0;
                                    xOffset=0;
                                    yOffset=0;
                                    //Borramos el ultimo rectangulo dibujado ya que el usuario no ingreso un nombre
                                    int posicion = (dibujos.getChildren().size())-1;
                                    dibujos.getChildren().remove(posicion);
                                    activarBotonAlternar();

                                }
                                event.consume();
                                addKeyHandler(scene);
                                //LLamamos para dibujar de nuevo
                                dibujar2(root3,scene);
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
                        activarBotonAlternar();
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
        
        Image imagenDeleteJson = new Image(new File("botonEliminarJson.png").toURI().toString(),35,36,false,true);
        ImageView iconoDeleteJson = new ImageView(imagenDeleteJson);
        
        Image imagenAlternar = new Image(new File("botonCambiar.png").toURI().toString(),35,36,false,true);
        ImageView iconoAlternar = new ImageView(imagenAlternar);
        
        
        Tooltip tooltip;
        
        botonDibujar.setGraphic(iconoDibujar);
        botonDibujar.setStyle("-fx-base: white;");
        botonDibujar.setTooltip(tooltip = new Tooltip("Dibujar"));
        
        botonIzqq.setGraphic(iconoVolverAtras);
        botonIzqq.setStyle("-fx-base: white;");
        botonIzqq.setTooltip(tooltip = new Tooltip("Rehacer"));
        
        botonDerr.setGraphic(iconoVolverAdelante);
        botonDerr.setStyle("-fx-base: white;");
        botonDerr.setTooltip(tooltip = new Tooltip("Deshacer"));
        
        botonBorrar.setGraphic(iconoBorrar);
        botonBorrar.setStyle("-fx-base: white;");
        botonBorrar.setTooltip(tooltip = new Tooltip("Borrar"));
        
        botonGuardar.setGraphic(iconoGuardar);
        botonGuardar.setStyle("-fx-base: white;");
        botonGuardar.setTooltip(tooltip = new Tooltip("Guardar Plantilla"));
        
        botonLeer.setGraphic(iconoLeer);
        botonLeer.setStyle("-fx-base: white;");
        botonLeer.setTooltip(tooltip = new Tooltip("Leer Plantilla"));
        
        estaSeguro.setGraphic(iconoDeleteJson);
        estaSeguro.setStyle("-fx-base: white;");
        estaSeguro.setTooltip(tooltip = new Tooltip("Eliminar Plantilla Actual"));
        estaSeguro.setDisable(true);
        
        botonAlternar.setGraphic(iconoAlternar);
        botonAlternar.setStyle("-fx-base: white;");
        botonAlternar.setTooltip(tooltip = new Tooltip("Alternar vista"));
        botonAlternar.setDisable(true);
        
        
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
            activarBotonAlternar();

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
                activarBotonAlternar();
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
            activarBotonAlternar();
        }
        
    }
    
    //Funcion que corrige error al dibujar un rectangulo. Este error consiste en que
    //cuando el usuario esta ingresando el nombre del rectangulo se abre una ventana y si dentro
    //de esa ventana se pulsa control z el programa se cae
    public void quitarKeyHandler(Scene scene){
        
        scene.setOnKeyPressed(null);
        
    }
    
    public void addKeyHandler(Scene scene) {
        
        scene.setOnKeyPressed((event) -> {
            //control Z
            if (controlZ.match(event)) {
                
                funcionZ();
                activarBotonAlternar();
                event.consume();
            }
            //control Y
            if(controly.match(event)){
                funcionY();
                activarBotonAlternar();
                event.consume();
            }
        });
    }
}
