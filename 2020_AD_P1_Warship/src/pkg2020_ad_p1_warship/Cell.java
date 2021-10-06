package pkg2020_ad_p1_warship;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author joange
 * acabado
 */
public class Cell {
    // Contantes para controlar el contenido de la celda
    public static  int CELL_WATER = 0;
    public static  int CELL_BOAT = 1;
    public static  int CELL_TOUCH = 2;
    public static  int CELL_SUNKEN = 3;
    public static  int CELL_FIRED = 4;
    public static  int CELL_NOT_INITIALIZED = -1;

    // Constantes para controlar lo que ser verá por pantalla

    public static  String CELL_WATER_CHAR = "_";
    public static  String CELL_BOAT_CHAR = "X";
    public static  String CELL_TOUCH_CHAR = "T";
    public static  String CELL_SUNKEN_CHAR = "H";
    public static  String CELL_SUNKEN_FIRED = "o";

    // Inicializació de variables privadas
    private int fila = 0; // fila en la que se encuentra la celda
    private int columna = 0; // Columna en la que se eunentra la celda
    private int contains = CELL_NOT_INITIALIZED; // Contenido de la celda
    private Boat boat; // Si la celda contiene parte de un bote ..
    public static int n = 0;


    public Cell(int fila, int columna) {
        // Al crear la celda inicializamos los valores....
        this.fila = fila;
        this.columna = columna;
        this.contains = CELL_WATER;
        boat = null;
    }
   

    public  void setBoat(Boat boat) {
        // Si ponemos un bote en la celda cambiamos los valores de las propiedades
        this.boat = boat; // Puntero al objeto bote que contien la celda
        this.contains = CELL_BOAT;
        
    }

    // Se utilizará cuando caiga una bomba sobre la celda y esta contnega un bote
    public void setTouch() {
        this.contains = CELL_TOUCH;
    }

    // se utilizará cuando todas las celdas ocupadas por un barco estén tocadas
    public void setSunken() {
        this.contains = CELL_SUNKEN;
    }

    // se utilizará cuando todas las celdas ocupadas por un barco estén tocadas
    public void setFired() {
        this.contains = CELL_FIRED;
    }

    // Métodos para establecer/consultas las propiedades
    public Boat getBoat() {
        return boat;
    }

    public int getRow() {
        return this.fila;
    }

    public int getColumn() {
        return this.columna;
    }

    public  int getContains() {
        return this.contains;
    }

    // Método para controlar el texto que aparecerá en pantalla
    public String getContainsString() {

        if (this.contains == CELL_WATER)
            return CELL_WATER_CHAR;
        if (this.contains == CELL_BOAT)
            return CELL_BOAT_CHAR;
        if (this.contains == CELL_TOUCH)
            return CELL_TOUCH_CHAR;
        if (this.contains == CELL_SUNKEN)
            return CELL_SUNKEN_CHAR;
        if (this.contains == CELL_FIRED)
            return CELL_SUNKEN_FIRED;
        return "E"; // Si devuelve E 'Error' no está inicializado
    }
  
    public static void GuardarMov(int x, int y, int result) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        int res=0;
        if(result==-1){
            res=3;
        }
       else if(result==0){
            res=0;
        }
        else if(result==2){
         res=1;
        }
        else if(result==3){
            res=2;
        }

        try {
            fw = new FileWriter("moviments_out.txt", true);
            bw = new BufferedWriter(fw);
            bw.write(String.valueOf(n) + ";" + String.valueOf(x) + ";" + String.valueOf(y) + ";"
                    + String.valueOf(res));
            bw.newLine();
            bw.close();
            n++;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {

                bw.close();
                fw.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    // Sobreescribe el método tostring de Object.
    @Override
    public String toString() {
        return "(" + this.fila + ", " + this.columna + ")  --> " + this.getContainsString();
    }

}
