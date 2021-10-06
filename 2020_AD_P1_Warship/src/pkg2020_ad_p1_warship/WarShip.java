package pkg2020_ad_p1_warship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import Utils.ConsoleColors;
import Utils.Leer;

/**
 *
 * @author joange
 */
public class WarShip {
    public static Properties config = new Properties();

    public static OutputStream configOutput = null;
    public static InputStream configInput = null;
    

    /**
     * @param args the command line arguments
     * acabado
     */
    
    private static int MAX_JUGADAS = 100;

    private Random r;
    private Board board;
    private WarShip ws;
    public static File f=new File("boat_out.txt");
    

    public WarShip() {
        if(f.exists()){
            f.delete();
        }
        r = new Random(System.currentTimeMillis());
        board= new Board();
        board.initBoats();

    }

    private void autoPlay() {

        board.paint();

        // Vamos a realizar 50 jugadas aleatorias ...
        for (int i = 1; i <= MAX_JUGADAS; i++) {
            System.out.println(ConsoleColors.GREEN_BRIGHT + "JUGADA: " + i);

            int fila, columna;
            do {
                fila = r.nextInt(Board.getBOARD_DIM());
                columna = r.nextInt(Board.getBOARD_DIM());
            } while (board.fired(fila, columna));

            if (board.shot(fila, columna) != Cell.CELL_WATER) {
                board.paint();
            } else {
                System.out.println("(" + fila + "," + columna + ") --> AGUA");
            }

            if (board.getEnd_Game()) {
                System.out.printf("Joc acabat amb %2d jugades\n", i);
                break;
            }
        }
    }

    private void autoPlayArchivo(int fila, int columna) {
        if (fila >Board.getBOARD_DIM() || columna >Board.getBOARD_DIM()) {
           System.out.println("Fuera de rango");
           return;
            
        }

            if (board.shot(fila, columna) != Cell.CELL_WATER) {
               
                board.paint();

            } else {
                
                System.out.println("(" + fila + "," + columna + ") --> AGUA");
            }

            /*if (board.getEnd_Game()) {
                System.out.printf("Joc acabat amb %2d jugades\n", i);
                break;
            }*/

        
    }

    private void play() {
        int num_jugadas = 0;
        boolean rendit = false;

        String jugada;
        int fila = -1, columna = -1;
        do {
            do {
                jugada = Leer.leerTexto("Dime la jugada en dos letras A3, B5... de A0 a J9: ").toUpperCase();
                if (jugada.equalsIgnoreCase("00")) {
                    System.out.println("Jugador rendit");
                    rendit = true;
                    break;
                }
                if (jugada.length() == 0 || jugada.length() > 2) {
                    System.out.println("Format incorrecte.");

                    continue;
                }

                fila = jugada.charAt(0) - 'A';
                columna = jugada.charAt(1) - '0';

            } while (board.fired(fila, columna));

            // acaba el joc
            if (rendit) {
                break;
            }

            num_jugadas++;

            if (board.shot(fila, columna) != Cell.CELL_WATER) {

                board.paintGame();

            } else {
                System.out.println("(" + fila + "," + columna + ") --> AGUA");
            }

            if (board.getEnd_Game()) {
                System.out.printf("Joc acabat amb %2d jugades\n", num_jugadas);
                break;
            }

        } while (num_jugadas < MAX_JUGADAS);

    }

    public void Exportar() {
        try {
            configOutput = new FileOutputStream("warship.properties");
            config.setProperty("board_tam", String.valueOf(Board.getBOARD_DIM()));
            config.setProperty("num_boats", String.valueOf(Board.getBOARD_BOATS_COUNT()));
            config.setProperty("max_value", String.valueOf(WarShip.getMaxJugadas()));
            config.store(configOutput,"Fichero de configuracion");
        } catch (Exception e) {
            System.out.println("Error para guardar el fichero de configuración");
        } finally {
            try {
                configOutput.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void Importar() {
        try {
            configInput = new FileInputStream("warship.properties");
            config.load(configInput);
            Board.setBOARD_DIM(Integer.parseInt(config.getProperty("board_tam")));
            Board.setBOARD_BOATS_COUNT(Integer.parseInt(config.getProperty("num_boats")));
            WarShip.setMaxJugadas(Integer.parseInt(config.getProperty("max_value")));

        } catch (Exception e) {
            System.out.println("Error al importar el fichero de configuración");
        } finally {
            try {
                configInput.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void copiarchivo(String nombre, String destino) {
        File f = new File(nombre);
        File f2 = new File(destino);
        FileReader fr = null;
        FileWriter fw = null;

        try {

            FileInputStream in = new FileInputStream(f);
            FileOutputStream out = new FileOutputStream(f2);

            int c;
            while ((c = in.read()) != -1)
                out.write(c);

            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Hubo un error de entrada/salida!!!");
        }

    }

    public void ImpBoatMov() {

        FileReader fr = null;
        FileReader fr2 = null;
  

        try {
            File f = new File("boat_in.txt");
            File f2 = new File("moviments_in.txt");
           

            fr = new FileReader(f);
            fr2 = new FileReader(f2);
            BufferedReader bfr = new BufferedReader(fr);
            BufferedReader bfr2 = new BufferedReader(fr2);
               
            while (bfr.ready()) {
                String linea = bfr.readLine();
                
                String[] barcos = linea.split(";");
                Boat boat = new Boat();
                boat.setBoatArchivo(Integer.parseInt(barcos[1]), Integer.parseInt(barcos[2]),
                        Integer.parseInt(barcos[3]), Integer.parseInt(barcos[4]));
            }
           
            while (bfr2.ready()) {

                String linea2 = bfr2.readLine();
               
                String[] mov = linea2.split(";");
                this.autoPlayArchivo(Integer.parseInt(mov[1]), Integer.parseInt(mov[2]));

            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                fr2.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static int getMaxJugadas() {
        return MAX_JUGADAS;
    }

    public static void setMaxJugadas(int jugadas) {
        MAX_JUGADAS = jugadas;
    }

    public static  void main(String[] args) {
        // TODO code application logic here
        WarShip ws = new WarShip();

        int opcio = 0;
        do {
            System.out.println(ConsoleColors.GREEN + "--    Escollir   --");
            System.out.println(ConsoleColors.GREEN + "1. Joc automàtic...");
            System.out.println(ConsoleColors.GREEN + "2. Joc manual......");
            System.out.println(ConsoleColors.GREEN + "3. Exportar/Importar");
            System.out.println(ConsoleColors.GREEN + "4. Joc automàtic archivo");
            System.out.println(ConsoleColors.GREEN + "5. Salir...........");
            opcio = Leer.leerEntero(ConsoleColors.CYAN + "Indica el tipus de joc que vols: " + ConsoleColors.RESET);
        } while (opcio < 1 || opcio > 5);

        switch (opcio) {
            case 1:
                ws.autoPlay();
                break;
            case 2:
            
                ws.play();
                break;
            case 3:
                boolean op = true;
                while (op) {

                    System.out.println(ConsoleColors.GREEN + "1. Exportar archivo propiedad.");
                    System.out.println(ConsoleColors.GREEN + "2. Importar archivo propiedad.");
                    opcio = Leer.leerEntero("Elije una de las 2 opciones :");
                    switch (opcio) {
                        case 1:
                            op = false;
                            ws.Exportar();
                            break;
                        case 2:
                            op = false;
                            ws.Importar();
                            break;
                    }
                }

                break;
            case 4:
                if (new File("moviments_out.txt").exists() && new File("boat_out.txt").exists()) {
                    ws.copiarchivo("moviments_out.txt", "moviments_in.txt");
                    ws.copiarchivo("boat_out.txt", "boat_in.txt");
                    ws.ImpBoatMov();
                } else {
                    System.out.println("No hay juego guardado vuelva mas tarde");
                    ws.play();
                }

                break;
            case 5:
           
            if(f.exists()){
                f.delete();
            }
                break;
        }

    }

}
