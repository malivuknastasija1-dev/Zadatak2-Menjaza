/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.menjazaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenjazaServer {
    
    private ServerSocket ssocket;
    private int port;
    private ArrayList<ConnectedMenjazaClient> clients;

    public ServerSocket getSsocket(){
        return ssocket;
    }
    
    public void setSsocket(ServerSocket ssocket){
        this.ssocket = ssocket;
    }
    
    public int getPort(){
        return port;
    }
    
    public void setPort(int port){
        this.port = port;
    }
    
    public ArrayList<ConnectedMenjazaClient> getClients(){
        return clients;
    }
    
    public void acceptClients(){
        Socket client = null;
        Thread thrd;
        
        while(true){
            try{
                System.out.println("Server ceka nove igrace...");
                client = this.ssocket.accept();
            } catch(IOException problem){
                Logger.getLogger(MenjazaServer.class.getName()).log(Level.SEVERE, null, problem);
            }
            
            if (client != null){
                ConnectedMenjazaClient clnt = new ConnectedMenjazaClient(client, clients);
                clients.add(clnt);
                thrd = new Thread(clnt);
                thrd.start();
            } else{
                break;
            }
        }
    }
    
    public MenjazaServer(int port){
        this.clients = new ArrayList<>();
        try {
            this.port = port;
            this.ssocket = new ServerSocket(port);
        } catch(IOException problem){
            Logger.getLogger(MenjazaServer.class.getName()).log(Level.SEVERE, null, problem);
        }
    }
    
    public static void main(String[] args) {
        MenjazaServer server = new MenjazaServer(4925);
        System.out.println("Server za menjazu slicica je pokrenut, slusam na portu 4925");
        server.acceptClients();
    }
}

