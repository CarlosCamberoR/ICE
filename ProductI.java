import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Auction.AuctionItemStruct;
import Ice.Current;

/**
 * La clase ProductI implementa la interfaz generada automáticamente _ProductDisp.
 * Representa un producto en una subasta con nombre, descripción, precio inicial
 * y una lista de ofertas realizadas.
 */
public class ProductI extends Auction._ProductDisp {

    // Atributos de la clase
    private String name;
    private String description;
    private int initialPrice;
    private List<Auction.AuctionItemStruct> auctionItems = new ArrayList<>();

    /**
     * Método de inicialización del producto.
     * @param name Nombre del producto.
     * @param description Descripción del producto.
     * @param initialPrice Precio inicial del producto.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void initialize(String name, String description, int initialPrice, Current current) {
        this.name = name;
        this.description = description;
        this.initialPrice = initialPrice;
    }

    /**
     * Método para obtener el nombre del producto.
     * @param current Objeto de contexto ICE.
     * @return Nombre del producto.
     */
    @Override
    public String getName(Current current) {
        return this.name;
    }

    /**
     * Método para establecer el nombre del producto.
     * @param name Nuevo nombre del producto.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void setName(String name, Current current) {
        this.name = name;
    }

    /**
     * Método para obtener la descripción del producto.
     * @param current Objeto de contexto ICE.
     * @return Descripción del producto.
     */
    @Override
    public String getDescription(Current current) {
        return this.description;
    }

    /**
     * Método para establecer la descripción del producto.
     * @param description Nueva descripción del producto.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void setDescription(String description, Current current) {
        this.description = description;
    }

    /**
     * Método para obtener el precio inicial del producto.
     * @param current Objeto de contexto ICE.
     * @return Precio inicial del producto.
     */
    @Override
    public int getInitialPrice(Current current) {
        return this.initialPrice;
    }

    /**
     * Método para establecer el precio inicial del producto.
     * @param initialPrice Nuevo precio inicial del producto.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void setInitialPrice(int initialPrice, Current current) {
        this.initialPrice = initialPrice;
    }

    /**
     * Método para obtener las ofertas (bids) del producto.
     * @param current Objeto de contexto ICE.
     * @return Arreglo de ofertas (bids) del producto.
     */
    @Override
    public AuctionItemStruct[] getBids(Current current) {
        return this.auctionItems.toArray(new Auction.AuctionItemStruct[0]);
    }

    /**
     * Método para establecer las ofertas (bids) del producto.
     * @param bids Nuevo arreglo de ofertas (bids) del producto.
     * @param current Objeto de contexto ICE.
     */
    @Override
    public void setBids(AuctionItemStruct[] bids, Current current) {
        this.auctionItems = Arrays.asList(bids);
    }

    /**
     * Método para agregar una nueva oferta (bid) al producto.
     * @param username Nombre de usuario del ofertante.
     * @param price Precio de la oferta.
     * @param time Tiempo en que se realizó la oferta.
     * @param current Objeto de contexto ICE.
     * @return true si la oferta se agrega con éxito, false de lo contrario.
     */
    @Override
    public boolean addBid(String username, int price, long time, Current current) {
        Auction.AuctionItemStruct newItem = new Auction.AuctionItemStruct();
        newItem.username = username;
        newItem.price = price;
        newItem.time = time;
        return auctionItems.add(newItem);
    }

    /**
     * Método para obtener una representación de cadena del producto.
     * @param current Objeto de contexto ICE.
     * @return Cadena que representa el producto y sus ofertas.
     */
    @Override
    public String _toString(Current current) {
        StringBuilder str = new StringBuilder();
        str.append("Product name: " + this.name + "\n");
        str.append("Description: " + this.description + "\n");
        str.append("Initial price: " + this.initialPrice + "€\n");
        str.append("Bids:\n");

        for (AuctionItemStruct item : auctionItems) {
            str.append("\t- User " + item.username + " placed a bid for a value of " + item.price + "\n");
        }

        return str.toString();
    }
}
