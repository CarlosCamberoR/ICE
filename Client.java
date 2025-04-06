import Auction.AuctionItemStruct;
import Auction.AuctionServerPrx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import pcd.util.TextIO4GUI;

/**
 * La clase Client representa la aplicación cliente para interactuar con el servidor de subastas.
 * Permite al usuario enviar pujas al servidor de subastas.
 */
public class Client {

    // Atributo para almacenar el proxy del servidor de subastas
    private final AuctionServerPrx auctionServer;

    /**
     * Constructor que recibe el proxy del servidor.
     * @param auctionServer El proxy del servidor de subastas.
     */
    public Client(AuctionServerPrx auctionServer) {
        this.auctionServer = auctionServer;
    }

    /**
     * Método principal que maneja la interacción del cliente con el servidor.
     * @throws IOException 
     */
    public void run() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Ingresa tu nombre de usuario: ");
            String username = scanner.nextLine();
            
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            
            //Creación de la interfaz del cliente
            new TextIO4GUI("Auction Client");

            // Bucle principal del cliente
            while (true) {
                System.out.println("1. Enviar puja");
                System.out.println("2. Información de la subasta");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                
                int choice;
                
                String str1;
                str1 = userInput.readLine();
                if (str1.matches("\\d+")) {
                    choice = Integer.parseInt(str1);
	                
	                // Crea una nueva puja
	                AuctionItemStruct bid = new AuctionItemStruct();
	                
	                BufferedReader userInput2 = new BufferedReader(new InputStreamReader(System.in));
	                switch (choice) {
	                    case 1:
	                        System.out.print("Ingrese su puja: ");
	                        String str;
	                        int bidAmount;
	                        str = userInput2.readLine();
	                        if (str.matches("\\d+")) {
	                            bidAmount = Integer.parseInt(str);
	                            bid.username = username;
	                            bid.price = bidAmount;
	                            bid.time = System.currentTimeMillis();
	                            
	                            // Envía la puja al servidor
	                            this.auctionServer.addAuctionItem(bid);
	                        }
	                        else {
	                        	System.out.println("la puja debe ser un número entero, volviendo al menú de selección.");
	                        }
	                        break;
	                    case 2:
	                    	// Muestra por pantalla información de la puja
	                    	TextIO4GUI.putln ("\n--------- INFORMACIÓN DE LA SUBASTA ---------\n" + 
	                    						"La mayor puja hasta el momento es de "+this.auctionServer.getMaxBid().username +
	                    						" por "+this.auctionServer.getMaxBid().price + " euros"+
	                    						" en el momento "+this.auctionServer.getMaxBid().time + "\n");
	                    	break;
	                    case 3:
	                        System.out.println("Saliendo...");
	                        System.exit(0);
	                    default:
	                        System.out.println("Opción no válida");
	                }
	            }
                else {
                	System.out.println("El valor de la opción debe ser un número entero.");
                }
            }
        }
    }

    /**
     * Método principal que inicia la aplicación cliente.
     * @param args Argumentos de línea de comandos.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        Ice.Communicator communicator = null;

        try {
            // Inicializa Ice
            communicator = Ice.Util.initialize(args);

            // Obtén el proxy del servidor usando el adaptador Ice
            Ice.ObjectPrx base = communicator.stringToProxy("AuctionServer:tcp -h localhost -p 10000");
            AuctionServerPrx auctionServer = Auction.AuctionServerPrxHelper.checkedCast(base);

            if (auctionServer == null) {
                throw new Error("Invalid proxy");
            }

            // Crea una nueva instancia del cliente
            Client client = new Client(auctionServer);

            // Inicia la interacción del cliente
            client.run();
        } catch (Ice.LocalException e) {
            e.printStackTrace();
        } finally {
            // Apaga Ice
            if (communicator != null) {
                try {
                    communicator.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
