
package proyectoprogra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 *
 * @author Diego Aguilera
 */
public class GuardadoJson {
    
    ArrayList<Button> rectangulosLeidos = new ArrayList<>();
    ArrayList<RectangulosJson> rectangulosGuardados = new ArrayList<>();
    
    public ArrayList<Button> getRectangulosLeidos() {
        return rectangulosLeidos;
    }
    
    public void crearJson(String nombreArchivo, ArrayList rectangulosBotones){
        try {
            
            if(rectangulosBotones.size()!=0){ //Revisar que existan rectangulos
                
                agregarRectangulos(rectangulosBotones); //Agregamos los botones como rectangulos al arraylist rectangulos
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Writer writer = new FileWriter(nombreArchivo); //Crea el archivo gson en la ruta especifica
                gson.toJson(rectangulosGuardados, writer); //Crea el json
                rectangulosGuardados.clear(); //Limpiamos el arraylist para no sobreescribir datos
                writer.close(); 
            }

            
        } catch (IOException iOException) {
        } catch (JsonIOException jsonIOException) {
        }
        
    }
    
    public void agregarRectangulos(ArrayList<Button> rectangulosBotones){
        
        for (int i = 0; i < rectangulosBotones.size(); i++) {//Recorremos el arraylist de botones
            //Obtenemos los datos de los botomes/rectangulos
            RectangulosJson rectangle = new RectangulosJson();
            
            int x = (int) rectangulosBotones.get(i).getLayoutX();
            int y = (int) rectangulosBotones.get(i).getLayoutY();
            int width = (int) rectangulosBotones.get(i).getWidth();
            int height = (int) rectangulosBotones.get(i).getHeight();
            
            String name = rectangulosBotones.get(i).getTooltip().getText();
            Rectangle rectangulo = new Rectangle(x, y, width, height); //Creamos nuevos rectangulos de tipo Rectangle

            rectangle.setRectangulo(rectangulo);
            rectangle.setNombre(name);
            
            rectangulosGuardados.add(rectangle);
            
        }
        
    }
    
    public void leerJson(File jsonFile){
        
        try {
            //Se lee el archivo 
            BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile.getPath()));
            //creamos un gson para luego transformar el archivo en tipo json
            Gson gson2 = new Gson();
            //guardamos en un objeto el archivo convertido en json
            Object jsonLeido = gson2.fromJson(bufferedReader, Object.class);
            //creamos un String para convertir nuevamente el json en una cadena
            String jsonRectangulosLeidos = gson2.toJson(jsonLeido);
            //creamos un parser para convertir de array a jsonElement
            JsonParser parser = new JsonParser();
            //Hacemos el parse
            JsonElement datos = parser.parse(jsonRectangulosLeidos);
            bufferedReader.close();

            //obtenemos el tipo de variable que en este casos seria un arraylist de Tipo Rectangle
            //con esto podemos leer cada rectangulo creado con el json y cargarlo en un nuevo arraylist
            Type type = new TypeToken<ArrayList<RectangulosJson>>() {
            }.getType();

            //cargamos la planilla del usuario en un nuevo arraylist
            ArrayList<RectangulosJson> inpList = new Gson().fromJson(datos, type);

//            System.out.println("x: "+inpList.get(0).getX());
//            System.out.println("y: "+inpList.get(0).getY());
//            System.out.println("widh: "+inpList.get(0).getWidth());
//            System.out.println("height: "+inpList.get(0).getHeight());
            convertirRectangulos(inpList);
            
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("No se ha encontrado el archivo");
            
        } catch (IOException ex) {
            Logger.getLogger(GuardadoJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void convertirRectangulos(ArrayList<RectangulosJson> rectangulos){
        
        for (RectangulosJson rectangulo : rectangulos) {
            Button botonRectangulo = new Button();
            botonRectangulo.setLayoutX(rectangulo.getRectangulo().getX());
            botonRectangulo.setLayoutY(rectangulo.getRectangulo().getY());
            botonRectangulo.setPrefSize(rectangulo.getRectangulo().getWidth(),rectangulo.getRectangulo().getHeight());
            
            Tooltip nombreRec = new Tooltip(rectangulo.getNombre());
            
            botonRectangulo.setTooltip(nombreRec);
            
            botonRectangulo.setMinSize(botonRectangulo.USE_PREF_SIZE,botonRectangulo.USE_PREF_SIZE);
            botonRectangulo.setStyle("-fx-background-color: transparent;-fx-border-color:black;");
            rectangulosLeidos.add(botonRectangulo);
        }
    }
}
