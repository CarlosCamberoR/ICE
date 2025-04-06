import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;

import Auction.AuctionItemStruct;
import Auction.Product;
import Auction.ProductHolder;
import Ice.BooleanHolder;
import Ice.Current;

/**
 * La clase ServerI implementa la interfaz generada automáticamente _AuctionServerDisp.
 * Representa la lógica del servidor de subastas, con métodos para gestionar productos,
 * pujas, iniciar y cerrar subastas.
 */
public class ServerI extends Auction._AuctionServerDisp {

    // Atributo que representa el producto asociado al servidor de subastas
    ProductI product;

    /**
     * Método para obtener el producto asociado al servidor.
     * @param current Objeto de contexto ICE.
     * @return El producto actual en subasta.
     */
    @Override
    public Product getProduct(Current current) {
        return this.product;
    }
    
    /**
     * Método para establecer el producto en el servidor.
     * @param name Nombre del producto.
     * @param description Descripción del producto.
     * @param initialPrice Precio inicial del producto.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void setProduct(String name, String description, int initialPrice, Current current) {
        product = new ProductI();
        product.initialize(name, description, initialPrice);
    }

    /**
     * Método para agregar una puja a un producto.
     * @param bid Información de la puja.
     * @param current Objeto de contexto ICE.
     * @return true si la puja se agrega con éxito, false de lo contrario.
     */
	@Override
	public boolean addAuctionItem(AuctionItemStruct bid, Current current) {
		List<AuctionItemStruct> bids = Arrays.asList(product.getBids());
        if (bids.isEmpty() || (bid.price > bids.get(bids.size()-1).price) || (bids.isEmpty() && bid.price >= product.getInitialPrice())) {
            return product.addBid(bid.username, bid.price, bid.time);
        } else {
            return false;
        }
	}

    /**
     * Método para obtener la oferta máxima (máxima puja) de un producto.
     * @param current Objeto de contexto ICE.
     * @return La oferta máxima del producto.
     */
	@Override
	public AuctionItemStruct getMaxBid(Current current) {
		List<AuctionItemStruct> bids = Arrays.asList(product.getBids());
        if (!bids.isEmpty()) {
            return bids.get(bids.size() - 1);
        } else {
            return null;
        }
	}
    
    /**
     * Método para manejar la subasta de un producto.
     * @param product Producto en subasta.
     * @param currentProduct Producto actual en subasta.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void handleAuction(Product product, Product currentProduct, Current current) {
        long startTime = System.currentTimeMillis();
        long AUCTION_DURATION = 60 * 1000;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = AUCTION_DURATION - elapsedTime;

                if (remainingTime <= 10 * 1000) {
                    // Muestra el tiempo restante cuando quedan 10 segundos o menos
                    System.out.println("Tiempo restante: " + (remainingTime / 1000) + " segundos");
                }

                if (remainingTime <= 0) {
                    // Cierre de la subasta
                    closeAuction(product);
                    System.out.println("La subasta ha finalizado.");
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 1000);
    }

    /**
     * Método para iniciar una subasta para un producto dado.
     * @param product Producto a subastar.
     * @param auctionOpen Indicador de subasta abierta.
     * @param currentProduct Producto actual en subasta.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void startAuction(Product product, BooleanHolder auctionOpen, ProductHolder currentProduct,
            Current current) {
        if (!auctionOpen.value) {
            currentProduct.value = product;
            auctionOpen.value = true;
            System.out.println("Subasta iniciada para el producto: " + product.getName());
        }
    }

    /**
     * Método para cerrar una subasta y mostrar al ganador.
     * @param product Producto asociado a la subasta.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void closeAuction(Product product, Current current) {
        AuctionItemStruct maxBid = getMaxBid();
        if (maxBid != null) {
            System.out.println("GANADOR: " + maxBid.username + " con una cantidad de " +
                    maxBid.price + " euros.");
        }
        else {
            System.out.println("No se recibió ninguna puja.");
        }
    }
}
