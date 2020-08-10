/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import com.opencsv.CSVWriter;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.TextArea;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 *
 * @author sebas
 */
public class OCR {

    TextArea textoOCR;
    String resultado;
    MatrizDatos matriz = new MatrizDatos();
    BufferedImage imagen;

    public void setImagen(BufferedImage imagen) {
        this.imagen = imagen;
    }
    
    public void setMatriz(MatrizDatos matriz) {
        this.matriz = matriz;
    }

    public MatrizDatos getMatriz() {
        return matriz;
    }

    public TextArea getTextoOCR() {
        return textoOCR;
    }

    public void setTextoOCR(TextArea textoOCR) {
        this.textoOCR = textoOCR;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void leerImagenEntera() {
        //Se cargan los dll necesarios para funcionar
        System.loadLibrary("dll/liblept1744");
        System.loadLibrary("dll/libtesseract3051");
        

        ITesseract instance = new Tesseract();  // JNA Interface Mapping
       
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setLanguage("spa");//lenguaje

        try {

            //String result = instance.doOCR(imageFile);
            String result = instance.doOCR(imagen);
            guardarEnTXT(result, "documentoCompleto.txt");

            setResultado(result);
            TextArea agregado = new TextArea(resultado);

            setTextoOCR(agregado);

            //System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

    }

    public void leerPorRectangulo(ArrayList<Button> rectangulos) {
        //Se cargan los dll necesarios para funcionar
        System.loadLibrary("dll/liblept1744");
        System.loadLibrary("dll/libtesseract3051");

        
        ITesseract instance = new Tesseract();  // JNA Interface Mapping

        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setLanguage("spa");//lenguaje

        try {

            ArrayList<Rectangle> rectangulos2 = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();

            for (Button rectangulo : rectangulos) {

                Rectangle rec = new Rectangle((int) rectangulo.getLayoutX(), (int) rectangulo.getLayoutY(), (int) rectangulo.getWidth(), (int) rectangulo.getHeight());
                rectangulos2.add(rec);
                Tooltip aux = rectangulo.getTooltip();
                nombres.add(aux.getText());
            }

            ArrayList<RectangulosMatriz> rectangulosConTexto = new ArrayList<>(); 
            String salida = "";
            //ArrayList donde se almacenara un arreglo de tipo String que contiene el nombre del rectangulo y su contenido
            ArrayList<String[]> contenido = new ArrayList<>(); 
            for (int i = 0; i < rectangulos2.size(); i++) { 
                String result = instance.doOCR(imagen, rectangulos2.get(i));
                result = result.replaceFirst("\n", "");
                String[] dato = {nombres.get(i),result}; //Almacenamos en el arreglo el nombre y el contenido del rectangulo
                contenido.add(dato); //Añadimos al arreglo
                
                rectangulosConTexto.add(new RectangulosMatriz(nombres.get(i), result));
            }
           
            MatrizDatos mAux = new MatrizDatos();
            mAux.muestraDeMatriz(rectangulosConTexto);
            setMatriz(mAux);
            
            writeToCsvFile(contenido, ";"); //Llamamos al metodo para crear el csv
            
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
    
    //Metodo para crear el archivo de tipo csv, que recibe un arraylist que almacena lo que escribiremos
    //también un separador, en este caso: ";"
    public void writeToCsvFile(ArrayList<String[]> thingsToWrite, String separator){
        try (FileWriter writer = new FileWriter("Grilla.csv")){
            for (String[] strings : thingsToWrite) {
                for (int i = 0; i < strings.length; i++) {
                    writer.append(strings[i]);
                    if(i < (strings.length-1))
                        writer.append(separator);
                }
                writer.append(System.lineSeparator());
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void guardarEnTXT(String texto, String nombreArchivo) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            //Guardamos el archivo en la carpeta del proyecto
            fichero = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fichero);

            pw.print(texto);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    System.out.println("Se ha creado el archivo: " + nombreArchivo);
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    


}
