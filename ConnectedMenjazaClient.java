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
        duplicatesStickers.clear();
        missingStickers.clear();
        Random rnd = new Random();
        int numberDuplicates = rnd.nextInt(100);
        int numberMissing = rnd.nextInt(100);
        
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
    
    public void makePlayerList() {
        for (ConnectedMenjazaClient playerReceiver : allClients) {
            String playerReceiverName = playerReceiver.getUserName();
            if (playerReceiverName == null || playerReceiverName.trim().isEmpty()) continue;

            java.util.StringJoiner sj = new java.util.StringJoiner(",");

            for (ConnectedMenjazaClient player2 : allClients) {
                String player2Name = player2.getUserName();
                if (player2Name == null || player2Name.trim().isEmpty() || playerReceiver == player2) {
                    continue; 
                }

                java.util.List<Integer> canOffer = new java.util.ArrayList<>(playerReceiver.getDuplicates());
                canOffer.retainAll(player2.getMissing());

                java.util.List<Integer> canReceive = new java.util.ArrayList<>(player2.getDuplicates());
                canReceive.retainAll(playerReceiver.getMissing());

                int numExcs = Math.min(canOffer.size(), canReceive.size());

                String canOfferString = canOffer.isEmpty() ? "Nista" : canOffer.toString().replaceAll("[\\[\\] ]", "");
                String canReceiveString = canReceive.isEmpty() ? "Nista" : canReceive.toString().replaceAll("[\\[\\] ]", "");
                
                sj.add(player2Name + " (" + numExcs + "|" + canOfferString + "|" + canReceiveString + ")");
            }

            String message = "LIST_PLAYERS;" + sj.toString();
            playerReceiver.pw.println(message);
        }
    }
    
    @Override
    public void run(){
        while(true){
            try{
                if(this.userName.equals("")){
                    this.userName = this.br.readLine();
                    if(this.userName != null){
                        this.userName = this.userName.trim();
                        System.out.println("Konektovan igrac: " + this.userName);
                        initialSetStickers();
                        sendStickersToClient();
                        makePlayerList(); // Osvežava listu svima čim uđeš
                    }else{
                        System.out.println("Diskonektovan igrac: " + this.userName);
                        break;
                    }
                }else{
                    System.out.println("Server ceka na " + this.userName + " zahtev...");
                    String line = this.br.readLine();
                    if(line != null){
                        System.out.println("Stigao je zahtev od " + this.userName + " " + line);
                        
                        if (line.startsWith("EXCHANGE_PROPOSAL")){
                            String[] tokeni = line.split(";");
                            String namePlayer = "";
                            String offer = "";
                            String require = "";
                            
                            for(String tok : tokeni){
                                if (tok.startsWith("target:")) namePlayer = tok.substring(7).trim();
                                if (tok.startsWith("offer:")) offer = tok.substring(6).trim();
                                if (tok.startsWith("require:")) require = tok.substring(8).trim();
                            }
                            
                            if (namePlayer.contains("(")) {
                                namePlayer = namePlayer.substring(0, namePlayer.indexOf("(")).trim();
                            }
                            
                            boolean foundPlayer = false;
                            for(ConnectedMenjazaClient cl : allClients){
                                if (cl.getUserName().equalsIgnoreCase(namePlayer)){
                                    cl.pw.println("ARRIVED_REQUEST;from:" + this.userName + ";offer:" + offer + ";require:" + require);
                                    foundPlayer = true;
                                    break;
                                }
                            }
                            
                            if (foundPlayer){
                                this.pw.println("Zahtev uspešno prosleđen igraču " + namePlayer + ". Čeka se odgovor...");
                            } else {
                                this.pw.println("Greška - Igrač: " + namePlayer + " je diskonektovan!");
                            }
                        }
                        
                        if (line.startsWith("EXCHANGE_ACCEPTED")) {
                            String[] tokeni = line.split(";");
                            String peerName = "";
                            String hisOffer = "";
                            String hisRequire = "";
                            for(String t : tokeni) {
                                if(t.startsWith("peer:")) peerName = t.substring(5).trim();
                                if(t.startsWith("hisOffer:")) hisOffer = t.substring(9).trim();
                                if(t.startsWith("hisRequire:")) hisRequire = t.substring(11).trim();
                            }
                            
                            for(ConnectedMenjazaClient cl : allClients) {
                                if(cl.getUserName().equalsIgnoreCase(peerName)) {
                                    
                                    if(!hisRequire.equals("Nista") && !hisRequire.isEmpty()){
                                        for(String s : hisRequire.split(",")) {
                                            this.duplicatesStickers.remove(Integer.valueOf(s.trim()));
                                            cl.missingStickers.remove(Integer.valueOf(s.trim()));
                                        }
                                    }
                                    
                                    if(!hisOffer.equals("Nista") && !hisOffer.isEmpty()){
                                        for(String s : hisOffer.split(",")) {
                                            cl.duplicatesStickers.remove(Integer.valueOf(s.trim()));
                                            this.missingStickers.remove(Integer.valueOf(s.trim()));
                                        }
                                    }
                                    
                                    Collections.sort(this.duplicatesStickers);
                                    Collections.sort(this.missingStickers);
                                    Collections.sort(cl.duplicatesStickers);
                                    Collections.sort(cl.missingStickers);
                                    
                                    this.sendStickersToClient();
                                    cl.sendStickersToClient();
                                    
                                    this.pw.println("Razmena sa igračem " + peerName + " je uspešno izvršena u bazi!");
                                    cl.pw.println("Igrač " + this.userName + " je prihvatio razmenu! Sličice su zamenjene.");
                                    break;
                                }
                            }
                            
                            makePlayerList();
                        }
                        
                    } else {
                        System.out.println("Igrac " + this.userName + " je napustio igru...");
                        Iterator<ConnectedMenjazaClient> it = this.allClients.iterator();
                        while(it.hasNext()){
                            if(it.next().getUserName().equals(this.userName)){
                                it.remove();
                                break;
                            }
                        }
                        
                        makePlayerList();
                        this.socket.close();
                        break;
                    }
                }
            }catch(IOException problem){
                System.out.println("Diskonektovan igrac: " + this.userName);
                
                for (ConnectedMenjazaClient cl : this.allClients){
                    if (cl.getUserName().equals(this.userName)){
                        this.allClients.remove(cl);
                        break;
                    }
                }
                
                makePlayerList();
                return;
            }
        }
    }
}

}
