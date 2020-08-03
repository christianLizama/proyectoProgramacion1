/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javafx.scene.control.TextArea;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.util.ArrayList;
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
    
    
    
    
    public void leerImagenEntera(){
        //Se cargan los dll necesarios para funcionar
        System.loadLibrary ("dll/liblept1744");
        System.loadLibrary ("dll/libtesseract3051");
        // TODO code application logic here
        File imageFile = new File("imagen.png");
        
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setLanguage("spa");//lenguaje
                
        try {
            
            String result = instance.doOCR(imageFile);
            
            guardarEnTXT(result,"lecturaPDF.txt");
            
            setResultado(result);
            TextArea agregado = new TextArea(resultado);
            
            setTextoOCR(agregado);
            
            
            //System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        
    }
    
    public void leerPorRectangulo(ArrayList<Button> rectangulos){
        //Se cargan los dll necesarios para funcionar
        System.loadLibrary ("dll/liblept1744");
        System.loadLibrary ("dll/libtesseract3051");
        
        File imageFile = new File("imagen.png");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setLanguage("spa");//lenguaje
                
        try {
            
            ArrayList<Rectangle> rectangulos2 = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            
            for (Button rectangulo : rectangulos) {
                
                Rectangle rec = new Rectangle((int)rectangulo.getLayoutX(),(int)rectangulo.getLayoutY(),(int)rectangulo.getWidth(),(int)rectangulo.getHeight());
                rectangulos2.add(rec);
                Tooltip aux = rectangulo.getTooltip();
                nombres.add(aux.getText());
                
            }
            
            
            ArrayList<RectangulosMatriz> rectangulosConTexto = new ArrayList<>();
            String salida="";
            
            for (int i = 0; i < rectangulos2.size(); i++) {
                String result = instance.doOCR(imageFile,rectangulos2.get(i));
                result = result.replaceFirst("\n", "");
                
                //System.out.print(nombres.get(i)+": "+result);
                salida = salida + nombres.get(i)+"\t"+result;
                rectangulosConTexto.add(new RectangulosMatriz(nombres.get(i), result));
            }
            
            MatrizDatos mAux = new MatrizDatos();
            mAux.muestraDeMatriz(rectangulosConTexto);
            setMatriz(mAux);
            
            guardarEnTXT(salida, "Grilla.txt");
            
            //guardarEnTXT(result);
            
            //setResultado(result);
            //TextArea agregado = new TextArea(resultado);
            
            //setTextoOCR(agregado);
            
            
            //System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        
    }
    
    public void guardarEnTXT(String imagenLeida,String nombreArchivo){
        
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            //Guardamos el archivo en la carpeta del proyecto
            fichero = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fichero);
            
            pw.print(imagenLeida);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
          
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        
    }
    
}
