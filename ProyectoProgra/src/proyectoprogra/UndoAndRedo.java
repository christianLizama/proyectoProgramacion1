/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoprogra;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author 56945
 */
public class UndoAndRedo {
    
    Stack <ArrayList> PilaZ = new Stack<>();
    Stack <ArrayList> pilaY = new Stack<>();
    
    
    //Se agregan todos los rectangulos que hayan sido dibujados a una posición en la pila
    public void agregarPila(GuardarContenidoPane pizarra) {
        PilaZ.push(pizarra.getRectangulos());
    }
    //Se imprime la cantidad de rectangulos por cada arraylist de la pila Z
    public void imprimir(){
        
        int i=0;
        if (!PilaZ.isEmpty()) {
            
            while(i<PilaZ.size()){
                
                System.out.println("Tamaño arreglo: " + PilaZ.get(i).size());
                
                
                i++;
            }
            System.out.println("");
            
        }
    }
    //Se efectua la accion de control Z retornando el arraylist correspondiente 
    public ArrayList controlZ(){
        ArrayList rectangulos = new ArrayList();
        if(!PilaZ.isEmpty()){
            pilaY.push(PilaZ.pop());//Se elimina de la pila Z y se carga en la pila Y
            
            if(PilaZ.isEmpty()){
                System.out.println("Tamaño pila control Z:0");
            }
            else{
                System.out.println("Tamaño pila control Z:"+ PilaZ.size());
            }
            
            System.out.println("Tamaño pila control Y:"+ pilaY.size());
            if(PilaZ.isEmpty()){
                
                return rectangulos;
            }
            else{
                rectangulos = PilaZ.peek();
                return rectangulos;
            }
        }
        
        return rectangulos;
    }

    
}
