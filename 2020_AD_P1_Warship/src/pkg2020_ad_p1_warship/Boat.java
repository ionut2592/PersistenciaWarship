package pkg2020_ad_p1_warship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author joange
 * acabado
 */

public class Boat {
    // Constantes para controlar el estado general del barco
    public static int BOAT_OK = 0;
    public static int BOAT_TOUCHED = 1;
    public static int BOAT_SUNKEN = 2;

    // Constantes para controlar su orientación
    public static int BOAT_DIR_HOR = 0;
    public static int BOAT_DIR_VER = 1;

    // Propiedades
    private int dimension = 0;
    private Cell[] cells;
    private int estado = BOAT_OK;

    private static int dir;
    

    // Constructor
    public Boat() {
        // Vacío, las propiedades se establecen en su iniciaización
    }

    // Para consultar el estado de un barco
    public int getBoatState() {
        return estado;
    }

    // Establece la dimensión del barco y lo coloca en el tablero si cabe
    public void setBoat(int dim, Board board) {
        cells = new Cell[dim];
        this.dimension = dim;

        // Posicionamiento aleatorio
        int fila = Double.valueOf((Math.random() * (Board.getBOARD_DIM()))).intValue();
        int columna = Double.valueOf(Math.random() * (Board.getBOARD_DIM())).intValue();
        int dir = Double.valueOf(Math.random() * (getBoatDirVer() + 1)).intValue();

        while (!boatFits(fila, columna, dim, dir, board)) {
            fila = Double.valueOf((Math.random() * Board.getBOARD_DIM())).intValue();
            columna = Double.valueOf(Math.random() * Board.getBOARD_DIM()).intValue();

        }

        if (dir == BOAT_DIR_HOR) {
            for (int i = columna; i < columna + dim; i++) {
                this.cells[i - columna] = board.getCell(fila, i);
                this.cells[i - columna].setBoat(this);
                setDir(0);
            }
        } else {
            for (int i = fila; i < fila + dim; i++) { // BOAT_DIR_VER
                this.cells[i - fila] = board.getCell(i, columna);
                this.cells[i - fila].setBoat(this);
                setDir(1);;
            }
        }
    }

    // no funciona ??????
    public void setBoatArchivo(int dim, int dir, int fila, int columna) {
        System.out.println("dim="+dim + ", dir="+ dir + "("+fila+","+columna+")");
        cells = new Cell[dim];
        this.dimension = dim;
         Board board=new Board();
        if (dir == BOAT_DIR_HOR) {
            for (int i = columna; i < columna + dim; i++) {
                this.cells[i - columna] = board.getCell(fila, i);
                this.cells[i - columna].setBoat(this);
            }
        } else {
            for (int i = fila; i < fila + dim; i++) { // BOAT_DIR_VER
                this.cells[i - fila] = board.getCell(i, columna);
                this.cells[i - fila].setBoat(this);
            }
        }

    }

    // Controla si el bote cabe el el tablero
    private boolean boatFits(int fila, int columna, int dimension, int direccion, Board board) {
        int min_col = 0, max_col = 0, min_row = 0, max_row = 0;

        // los barcos se colocan de fila, columna hacia la derecha o hacia abajo
        // Dependiendo de la orientación calcula el recuadro a controlar

        if (direccion == Boat.getBoatDirHor()) {
            if ((columna + dimension) > Board.getBOARD_DIM())
                return false; // El barco no cabe

            min_col = board.fitValueToBoard(columna - 1);
            max_col = board.fitValueToBoard(columna + dimension);

            min_row = board.fitValueToBoard(fila - 1);
            max_row = board.fitValueToBoard(fila + 1);
        }
        if (direccion == Boat.getBoatDirVer()) {
            if ((fila + dimension) > Board.getBOARD_DIM())
                return false; // El barco no cabe

            min_col = board.fitValueToBoard(columna - 1);
            max_col = board.fitValueToBoard(columna + 1);

            min_row = board.fitValueToBoard(fila - 1);
            max_row = board.fitValueToBoard(fila + dimension);
        }

        // Recorre la matriz que contendrá el barco para asegurarse que no hay ninguno
        for (int i = min_row; i <= max_row; i++) {
            for (int j = min_col; j <= max_col; j++) {
                if (board.getCell(i, j).getContains() == Cell.CELL_BOAT)
                    return false; // Ya hay un barco
            }
        }

        return true;
    }

    // Cuando una shot cae sobre un barco
    public int touchBoat(int fila, int columna) {
        int tocados = 0;
        // Si ya está hundido no puede empeorar
        if (estado == BOAT_SUNKEN)
            return BOAT_SUNKEN;

        // Si no está hundido como mínimo estará tocado
        estado = Boat.BOAT_TOUCHED;

        // Comprueba si esta parte del barco aún no habia sido tocada
        // Cuenta los tocados para saber si está hundido
        for (int i = 0; i < dimension; i++) {
            Cell c = cells[i];
            if ((c.getRow() == fila) && (c.getColumn() == columna)) {
                if (c.getContains() == Cell.CELL_BOAT)
                    c.setTouch();
            }
            if (c.getContains() == Cell.CELL_TOUCH)
                tocados++;
        }
        // Si todas las partes del barco están tocadas ... Cambiar estado a hundido
        if (tocados == dimension) {
            // Hundido ....
            for (int i = 0; i < dimension; i++) {
                Cell c = cells[i];
                c.setSunken();
            }
            estado = BOAT_SUNKEN;
        }

        return estado;
    }

    // Para mostrar por pantalla las celdas que ocupa un barco
    public void viewCells() {
        System.out.print("Posiciones: {");
        for (int i = 0; i < dimension; i++) {
            Cell c = cells[i];
            System.out.print("(" + c.getRow() + ", " + c.getColumn() + ")");
        }
        System.out.println(" }");

    }

    public Cell viewCell1() {

        Cell c = cells[0];

        return c;

    }

    // guarda la informacion de los barcos en un txt
    public static void GuardarBarcos(int p, int dim, int dir, int x, int y) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        

        try {
          
            fw = new FileWriter("boat_out.txt", true);
            bw = new BufferedWriter(fw);
            bw.write(String.valueOf(p) + ";" + String.valueOf(dim) + ";" + String.valueOf(dir) + ";" + String.valueOf(x)
                    + ";" + String.valueOf(y));
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (Exception e) {
                File f=new File("boat_out.txt");
                if(f.exists()){
                    f.delete();
                }
            }
        }

    }

    public static int getDir() {
        return dir;
    }

    public static void setDir(int dir) {
        Boat.dir = dir;
    }

    public static int getBoatDirHor() {
        return BOAT_DIR_HOR;
    }

    public static void setBoatDirHor(int boatDirHor) {
        BOAT_DIR_HOR = boatDirHor;
    }

    public static int getBoatDirVer() {
        return BOAT_DIR_VER;
    }

    public static void setBoatDirVer(int boatDirVer) {
        BOAT_DIR_VER = boatDirVer;
    }

    public static int getBOAT_OK() {
        return BOAT_OK;
    }

    public static void setBOAT_OK(int bOAT_OK) {
        BOAT_OK = bOAT_OK;
    }

    public static int getBOAT_TOUCHED() {
        return BOAT_TOUCHED;
    }

    public static void setBOAT_TOUCHED(int bOAT_TOUCHED) {
        BOAT_TOUCHED = bOAT_TOUCHED;
    }

    public static int getBOAT_SUNKEN() {
        return BOAT_SUNKEN;
    }

    public static void setBOAT_SUNKEN(int bOAT_SUNKEN) {
        BOAT_SUNKEN = bOAT_SUNKEN;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
