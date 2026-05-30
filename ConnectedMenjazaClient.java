/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menjazaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedMenjazaClient implements Runnable {
    
    private Socket socket;
    private String userName;
    private BufferedReader br;
    private PrintWriter pw;
    private ArrayList<ConnectedMenjazaClient> allClients;
    private ArrayList<Integer> duplicatesStickers;
    private ArrayList<Integer> missingStickers;
    
    public String getUserName(){
        return userName;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public ArrayList<Integer> getDuplicates(){
        return duplicatesStickers;
    }
    
    public ArrayList<Integer> getMissing(){
        return missingStickers;
    }
    
    public ConnectedMenjazaClient(Socket socket, ArrayList<ConnectedMenjazaClient> allClients){
        this.socket = socket;
        this.userName = "";
        this.allClients = allClients;
        this.duplicatesStickers = new ArrayList<>();
        this.missingStickers = new ArrayList<>();
        
        try{
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
        }catch(IOException problem){
            Logger.getLogger(ConnectedMenjazaClient.class.getName()).log(Level.SEVERE, null, problem);
        }
        
    }
    
    private void initialSetStickers(){
        Random rnd = new Random();
        int numberDuplicates = rnd.nextInt(10);
        int numberMissing = rnd.nextInt(10);
        
        while(duplicatesStickers.size() < numberDuplicates){
            int num = rnd.nextInt(99) + 1;
            if (!duplicatesStickers.contains(num)){
                duplicatesStickers.add(num);
            }
        }
        
        while(missingStickers.size() < numberMissing){
            int num = rnd.nextInt(99) + 1;
            if (!missingStickers.contains(num) && !duplicatesStickers.contains(num)){
                missingStickers.add(num);
            }
        }
        
        Collections.sort(duplicatesStickers);
        Collections.sort(missingStickers);
    }
    
    private void sendStickersToClient(){
        StringBuilder sb = new StringBuilder("INITIAL_SET;duplicates:");
        
        for (int i = 0; i < duplicatesStickers.size(); i++){
            sb.append(duplicatesStickers.get(i));
            if(i < duplicatesStickers.size() - 1){
                sb.append(",");
            }
        }
        
        sb.append(";missing:");
        for (int i = 0; i < missingStickers.size(); i++){
            sb.append(missingStickers.get(i));
            if(i < missingStickers.size() - 1){
                sb.append(",");
            }
        }
        
        this.pw.println(sb.toString());
        System.out.println("Sent to: " + userName + " " + sb.toString());
    }
    
    @Override
    public void run(){
        while(true){
            try{
                if(this.userName.equals("")){
                    this.userName = this.br.readLine();
                    if(this.userName != null){
                        this.userName = this.userName.trim();
                        System.out.println("Connected user: " + this.userName);
                        initialSetStickers();
                        sendStickersToClient();
                    }else{
                        System.out.println("Disconnected user: " + this.userName);
                        break;
                    }
                }else{
                    System.out.println("Waiting for user's " + this.userName + " request...");
                    String line = this.br.readLine();
                    if(line != null){
                        System.out.println("Arrived request from " + this.userName + " " + line);
                        //ovde dodati za brisanje i razmenu
                    } else{
                        System.out.println("User " + this.userName + "left the game...");
                        Iterator<ConnectedMenjazaClient> it = this.allClients.iterator();
                        while(it.hasNext()){
                            if(it.next().getUserName().equals(this.userName)){
                                it.remove();
                                break;
                            }
                        }
                        this.socket.close();
                        break;
                    }
                }
            }catch(IOException problem){
                System.out.println("Disconnected user: " + this.userName);
                
                for (ConnectedMenjazaClient cl : this.allClients){
                    if (cl.getUserName().equals(this.userName)){
                        this.allClients.remove(cl);
                        break;
                    }
                }
                return;
            }
        }
    }
    
}
