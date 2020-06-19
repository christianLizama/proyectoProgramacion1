
package proyectoprogra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import java.awt.Rectangle;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import javafx.scene.control.Button;

/**
 *
 * @author Diego Aguilera
 */
public class GuardadoJson {
    
    
    ArrayList<Rectangle> rectangulos = new ArrayList<>();
    
    
    public void crearJson(String nombreArchivo, ArrayList rectangulosBotones){
        try {
            
            if(rectangulosBotones.size()!=0){ //Revisar que existan rectangulos
                
                agregarRectangulos(rectangulosBotones); //Agregamos los botones como rectangulos al arraylist rectangulos
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Writer writer = new FileWriter(nombreArchivo); //Crea el archivo gson en la ruta especifica
                gson.toJson(rectangulos, writer); //Crea el json
                rectangulos.clear(); //Limpiamos el arraylist para no sobreescribir datos
                writer.close(); 
            }

            
        } catch (IOException iOException) {
        } catch (JsonIOException jsonIOException) {
        }
        
    }
    
    public void agregarRectangulos(ArrayList<Button> rectangulosBotones){
        
        for (int i = 0; i < rectangulosBotones.size(); i++) {//Recorremos el arraylist de botones
            //Obtenemos los datos de los botomes/rectangulos
            int x = (int) rectangulosBotones.get(i).getLayoutX();
            int y = (int) rectangulosBotones.get(i).getLayoutY();
            int width = (int) rectangulosBotones.get(i).getWidth();
            int height = (int) rectangulosBotones.get(i).getHeight();
                    
            Rectangle rectangulo = new Rectangle(x, y, width, height); //Creamos nuevos rectangulos de tipo Rectangle
            rectangulos.add(rectangulo);
        }
        
    }
    
    
}
