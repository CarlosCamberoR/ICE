# Auction System with ICE

This project implements a distributed auction system using the Internet Communications Engine (ICE) framework. The system consists of a server that hosts auctions for products and clients that can connect to place bids.

## Features

- Server-side product management
- Real-time bidding system
- Time-limited auctions (60 seconds)
- Client interface for placing bids and viewing auction information
- Winner determination based on highest bid

## System Requirements

- Java Development Kit (JDK) 8 or later
- ICE framework for Java
- pcd.util.TextIO4GUI library for client interface

## Project Structure

- [`Calc.ice`](Calc.ice) - ICE interface definitions for the auction system
- [`ProductI.java`](ProductI.java) - Implementation of the Product interface
- [`ServerI.java`](ServerI.java) - Implementation of the AuctionServer interface
- [`Server.java`](Server.java) - Main server application
- [`Client.java`](Client.java) - Client application for placing bids

## Setup and Installation

1. Install the ICE framework for Java according to your operating system
2. Make sure you have the pcd.util.TextIO4GUI library in your classpath
3. Compile the ICE slice definition:
   ```bash
   slice2java Calc.ice
   ```
4. Compile the Java files:
   ```bash
   javac *.java
   ```

## Running the Application
Starting the Server

1. Run the Server application:
    ```bash
        java Server
    ```
2. When prompted, enter the product information:
    * Name
    * Description
    * Initial price (must be an integer)

The server will start the auction and wait for client connections.

### Starting a Client
1. Run the Client application:
    ```bash
        java Server
    ```
2. Enter your username when prompted
3. Use the menu to:
    * Place bids
    * View auction information
    * Exit the application
## How the Auction Works

- The server initializes a product with details entered by the user  
- An auction starts and runs for 60 seconds  
- Clients can connect and place bids, but only bids higher than the current highest bid are accepted  
- The system displays a countdown during the last 10 seconds  
- When the auction ends, the system announces the winner with the highest bid  

## Implementation Details

The system uses a client-server architecture with **ICE** as the communication middleware:

- The `Product` interface handles product information and bid storage  
- The `AuctionServer` interface manages the auction lifecycle  
- Bidding is managed with timestamp information to track when each bid was placed  
