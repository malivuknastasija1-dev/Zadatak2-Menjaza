/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menjazaclient;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveStickersFromServer implements Runnable {
    
    MenjazaClient parent;
    BufferedReader br;
    
    public ReceiveStickersFromServer(MenjazaClient parent){
        this.parent = parent;
        this.br = parent.getBr();
    }
    
    @Override
    public void run(){
        while(true){
            String line;
            try{
                line = this.br.readLine();
                if (line != null){
                    
                    if (line.startsWith("INITIAL_SET;")){
                        String[] tokeni = line.split(";");
                        String allDuplicates = "Nema duplikata";
                        String allMissing = "Nema slicica koje nedostaju";
                        
                        String[] duplicateTokeni = tokeni[1].split(":");
                        if (duplicateTokeni.length > 1){
                            allDuplicates = duplicateTokeni[1];
                        }
                        
                        String[] missingTokeni = tokeni[2].split(":");
                        if (missingTokeni.length > 1){
                            allMissing = missingTokeni[1];
                        }

                        if (!parent.isUputstvoPrikazano()){
                            String uputstvo = "UPUTSTVO ZA MENJAZU SLICICA: \n" + 
                                "1. Stiklirajte SAMO duplikate koje zelite da PONUDITE.\n" + 
                                "2. Stiklirajte SAMO odredjene slicice koje TRAZITE.\n" + 
                                "Ako ne stiklirate NISTA - nudite sve duplikate i trazite sve koje Vam fale!\n";
                            javax.swing.JOptionPane.showMessageDialog(parent, uputstvo, "Uputstvo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            parent.setUputstvoPrikazano(true);
                        }
                        
                        parent.getTaConsole().append("*** VASE SLICICE ***\n");
                        parent.getTaConsole().append("Duplikati za razmenu: " + allDuplicates + "\n");
                        parent.getTaConsole().append("Slicice koje Vam nedostaju: " + allMissing + "\n"); 
                        parent.getTaConsole().append("--------------------------------------------------\n");
                        
                        if (duplicateTokeni.length > 1 && !allDuplicates.equals("Nema duplikata")) {
                            String[] nizBrojevaDuplikata = allDuplicates.split(",");
                            parent.updateDuplicates(nizBrojevaDuplikata);
                        } else {
                            parent.updateDuplicates(new String[0]);
                        }

                        if (missingTokeni.length > 1 && !allMissing.equals("Nema slicica koje nedostaju")) {
                            String[] nizBrojevaNedostaje = allMissing.split(",");
                            parent.updateMissing(nizBrojevaNedostaje);
                        } else {
                            parent.updateMissing(new String[0]);
                        }
                        
                        parent.getBtnSendRequest().setEnabled(true);
                        
                    } 
                    else if (line.contains("LIST_PLAYERS")) {
                        String clearLine = line.substring(line.indexOf("LIST_PLAYERS"));
                        String[] tokeni = clearLine.split(";");
                        
                        if (tokeni.length > 1 && !tokeni[1].trim().isEmpty()) {
                            String[] players = tokeni[1].split(",");
                            
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    parent.getCbPlayers().removeAllItems();
                                    int numOtherPlayers = 0;
                                    
                                    for (String plyr : players) {
                                        if (plyr.contains("(")){
                                            String name = plyr.substring(0, plyr.indexOf("("));
                                            String inside = plyr.substring(plyr.indexOf("(") + 1, plyr.indexOf(")"));
                                            String[] data = inside.split("\\|");
                                            String displayComboBox = name + " (" + data[0] + " slicica)";
                                            parent.getCbPlayers().addItem(displayComboBox);
                                            numOtherPlayers++;
                                            
                                            parent.getTaConsole().append("Mozes da menjas slicice sa korisnikom " + name + ".\n");
                                            parent.getTaConsole().append("Ti imas za njega slicice: " + data[1].replace(",", ", ") + "\n");
                                            parent.getTaConsole().append("On za tebe ima slicice: " + data[2].replace(",", ", ") + "\n");
                                            parent.getTaConsole().append("----------------------------------------\n");
                                        }
                                        
                                    }
                                    
                                    if (numOtherPlayers > 0) {
                                        parent.getCbPlayers().setEnabled(true);
                                        parent.getBtnDelete().setEnabled(true); // Otključavamo i dugme za potvrdu
                                    } else {
                                        parent.getCbPlayers().addItem("Nema drugih aktivnih igraca");
                                        parent.getCbPlayers().setEnabled(false);
                                    }
                                }
                            });
                        }
                    } 
                    else if(line.startsWith("ARRIVED_REQUEST;")){
                        String[] tokeni = line.split(";");
                        String from = "";
                        String offer = "";
                        String require = "";
                        for(String tok : tokeni){
                            if (tok.startsWith("from:")) from = tok.substring(5);
                            if (tok.startsWith("offer:")) offer = tok.substring(6);
                            if (tok.startsWith("require:")) require = tok.substring(8);
                        }
                        
                        String finalFrom = from;
                        String finalOffer = offer;
                        String finalRequire = require;
                        
                        javax.swing.SwingUtilities.invokeLater(new Runnable(){
                            @Override
                            public void run(){
                                int answer = javax.swing.JOptionPane.showConfirmDialog(parent,
                                        "Igrac " + finalFrom + " nudi slicice: [" + finalOffer + "], a trazi od Vas: [" + finalRequire + "]. Da li prihvatate razmenu?",
                                        "Zahtev za razmenu", javax.swing.JOptionPane.YES_NO_OPTION);
                                if (answer == javax.swing.JOptionPane.YES_OPTION){
                                    parent.getTaConsole().append("Prihvacena razmena sa " + finalFrom + "\n");
                                    parent.getBr();
                                    try{
                                        java.io.PrintWriter pwFromClass = new java.io.PrintWriter(parent.getSoc().getOutputStream(), true);
                                        pwFromClass.println("EXCHANGE_ACCEPTED;rival;" + finalFrom + ";rivalOffer:" + finalOffer + ";rivalRequire:" + finalRequire);
                                    }catch(Exception problem){}
                                }else{
                                    parent.getTaConsole().append("Odbijena razmena");
                                }
                            }
                        });
                        
                    }
                    else {
                        parent.getTaConsole().append("Server: " + line + "\n");
                    }   
                } else {
                    parent.getTaConsole().append("Server je zatvorio vezu.\n");
                    break;
                }
            } catch (IOException problem){
                Logger.getLogger(ReceiveStickersFromServer.class.getName()).log(Level.SEVERE, null, problem);
                parent.getTaConsole().append("Izgubljena konekcija sa serverom!\n");
                break;
            }
        }
    }
}
