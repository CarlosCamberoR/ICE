import Auction.AuctionItemStruct;
import Auction.Product;
import Auction.ProductHolder;
import Ice.BooleanHolder;
import Ice.Communicator;
import Ice.ObjectAdapter;
import Ice.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * La clase Server representa la aplicación principal del servidor de subastas.
 * Controla la inicialización del servidor, la entrada del usuario para la creación
 * del producto, la gestión del estado de la subasta y la espera para la finalización
 * de la aplicación.
 */
public class Server {

    // Atributos estáticos para mantener el estado del producto y la subasta
    private static Product product;
    private static Product currentProduct;
    private static boolean auctionOpen = false;

    /**
     * Método principal que inicia la aplicación del servidor de subastas.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        int status = 0;
        Communicator ic = null;

        try {
            // Inicializar el sistema de comunicación Ice
            ic = Util.initialize(args);

            // Crear y activar el adaptador de objetos
            ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints(
                    "AuctionServerAdapter", "default -p 10000");

            // Crear la implementación del servidor de subastas
            ServerI auctionServerImpl = new ServerI();
            adapter.add(auctionServerImpl, ic.stringToIdentity("AuctionServer"));

            // Activar el adaptador
            adapter.activate();

            // Capturar información del producto desde la entrada del usuario
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Ingrese la información del producto que se va a subastar:");
            System.out.print("Nombre: ");
            String name = userInput.readLine().trim();
            System.out.print("Descripción: ");
            String description = userInput.readLine().trim();
            System.out.print("Precio inicial: ");
            String str;
            int initialPrice;

            // Validar que el precio inicial sea un número entero
            while (true) {
                str = userInput.readLine();
                if (str.matches("\\d+")) {
                    initialPrice = Integer.parseInt(str);
                    break;
                }
                System.out.println("El valor del precio inicial debe ser un número entero.");
                System.out.print("Precio inicial: ");
            }

            userInput.close();

            // Iniciar la subasta y establecer la información del producto
            auctionServerImpl.setProduct(name, description, initialPrice);
            product = new ProductI();
            product.initialize(name, description, initialPrice);
            
            //Se crea la puja inicial y se añade a la subasta
            AuctionItemStruct bid=new AuctionItemStruct();
            bid.username="Puja inicial";
            bid.price=initialPrice;
            bid.time=System.currentTimeMillis();;
            auctionServerImpl.addAuctionItem(bid);
            
            //Se inicializan los parámetros de entrada-salida
            BooleanHolder auctionOpenH = new BooleanHolder();
            ProductHolder currentProductH = new ProductHolder();
            auctionOpenH.value = auctionOpen;
            currentProductH.value = currentProduct;

            //Se inicia la subasta
            auctionServerImpl.startAuction(product, auctionOpenH, currentProductH);
            //Se obtienen los valores de los parámetros de entrada-salida
            auctionOpen = auctionOpenH.value;
            currentProduct = currentProductH.value;

            //Se gestiona la subasta
            auctionServerImpl.handleAuction(product, currentProduct);

            System.out.println("Subasta iniciada. Esperando ofertas...");

            // Esperar a que se cierre la aplicación
            ic.waitForShutdown();
        } catch (Exception e) {
            e.printStackTrace();
            status = 1;
        } finally {
            // Cerrar el comunicador cuando se termine
            if (ic != null) {
                try {
                    ic.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                    status = 1;
                }
            }
        }

        // Salir de la aplicación con el código de estado
        System.exit(status);
    }
}
