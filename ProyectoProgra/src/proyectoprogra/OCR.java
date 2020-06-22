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
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author sebas
 */
public class OCR {
    
    
    public void leerImagenEntera(){
        //Se cargan los dll necesarios para funcionar
        System.loadLibrary ("dll/liblept1744");
        System.loadLibrary ("dll/libtesseract3051");
        // TODO code application logic here
        File imageFile = new File("imagen_0.png");
        
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setLanguage("spa");//lenguaje
                
        try {
            
            String result = instance.doOCR(imageFile);
            guardarEnTXT(result);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        
    }
    
    
    public void guardarEnTXT(String imagenLeida){
        
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            //Guardamos el archivo en la carpeta del proyecto
            fichero = new FileWriter("lecturaPDF.txt");
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
