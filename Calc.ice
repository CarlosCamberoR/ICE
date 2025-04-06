module Auction {

	struct AuctionItemStruct {
		string username;
		int price;
		long time;
	};
	
	sequence<AuctionItemStruct> bidCollection;
	
    interface Product {
        void initialize(string name, string description, int initialPrice);
        string getName();
        void setName(string name);
        string getDescription();
        void setDescription(string description);
        int getInitialPrice();
        void setInitialPrice(int initialPrice);
        bidCollection getBids();
        void setBids(bidCollection bids);
        bool addBid(string username, int price, long time);
        string toString();
    };

    interface AuctionServer {
        void setProduct(string name, string description, int initialPrice);
        Product getProduct();
        bool addAuctionItem(AuctionItemStruct bid);
        AuctionItemStruct getMaxBid();
        void handleAuction(Product product, Product currentProduct);
        void startAuction(Product product, out bool auctionOpen, out Product currentProduct);
        void closeAuction(Product product);
    };
};