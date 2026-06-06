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
                        String allMissing = "Nema sličica koje nedostaju";
                        
                        String[] duplicateTokeni = tokeni[1].split(":");
                        if (duplicateTokeni.length > 1){
                            allDuplicates = duplicateTokeni[1];
                        }
                        
                        String[] missingTokeni = tokeni[2].split(":");
                        if (missingTokeni.length > 1){
                            allMissing = missingTokeni[1];
                        }

                        if (!parent.isUputstvoPrikazano()){
                            String uputstvo = "UPUTSTVO ZA MENJAžU SLIčICA: \n" + 
                                "1. Štiklirajte SAMO duplikate koje želite da PONUDITE.\n" + 
                                "2. Štiklirajte SAMO određene sličice koje TRAŽITE.\n" + 
                                "Ako ne štiklirate NIŠTA - server sam izabere koje ćete ponuditi i potražiti!\n";
                            javax.swing.JOptionPane.showMessageDialog(parent, uputstvo, "Uputstvo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            parent.setUputstvoPrikazano(true);
                        }
                        
                        parent.getTaConsole().append("*** VASE SLICICE ***\n");
                        parent.getTaConsole().append("Duplikati za razmenu: " + allDuplicates + "\n");
                        parent.getTaConsole().append("Sličice koje Vam nedostaju: " + allMissing + "\n"); 
                        parent.getTaConsole().append("--------------------------------------------------\n");
                        
                        if (duplicateTokeni.length > 1 && !allDuplicates.equals("Nema duplikata")) {
                            String[] nizBrojevaDuplikata = allDuplicates.split(",");
                            parent.updateDuplicates(nizBrojevaDuplikata);
                        } else {
                            parent.updateDuplicates(new String[0]);
                        }

                        if (missingTokeni.length > 1 && !allMissing.equals("Nema sličica koje nedostaju")) {
                            String[] nizBrojevaNedostaje = allMissing.split(",");
                            parent.updateMissing(nizBrojevaNedostaje);
                        } else {
                            parent.updateMissing(new String[0]);
                        }
                        
                        parent.getBtnSendRequest().setEnabled(true);
                        
                    } 
                    else if (line.contains("LIST_PLAYERS")) {
                        String clearLine = line.substring(line.indexOf("LIST_PLAYERS"));
                        String[] tokeni = clearLine.split(";", -1); 
                        
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    parent.getTaConsole().setText("");
                                    parent.getCbPlayers().removeAllItems();
                                    int numOtherPlayers = 0;
                                    
                                    if (tokeni.length > 1 && !tokeni[1].trim().isEmpty()) {
                                        String allPlayersRaw = tokeni[1].trim();
                                      
                                        String[] players = allPlayersRaw.split(",(?![^()]*\\))");
                                        
                                        for (String plyr : players) {
                                            plyr = plyr.trim();
                                            if (plyr.contains("(") && plyr.contains(")")) {
                                                String name = plyr.substring(0, plyr.indexOf("(")).trim();
                                                String inside = plyr.substring(plyr.indexOf("(") + 1, plyr.lastIndexOf(")"));
                                                String[] data = inside.split("\\|");
                                                
                                                String numExcs = data[0].trim();
                                                String displayComboBox = name + " (" + numExcs + ")";
                                                parent.getCbPlayers().addItem(displayComboBox);
                                                numOtherPlayers++;
                                                
                                                parent.getTaConsole().append("Možeš da menjaš sličice sa korisnikom " + name + ".\n");
                                                if (data.length > 1 && !data[1].equals("Nista")) {
                                                    parent.getTaConsole().append("  -> Igrač tebi može dati: " + data[1].replace(",", ", ") + "\n");
                                                }
                                                if (data.length > 2 && !data[2].equals("Nista")) {
                                                    parent.getTaConsole().append("  -> Ti igraču možeš dati: " + data[2].replace(",", ", ") + "\n");
                                                }
                                                parent.getTaConsole().append("  Mogućih obostranih razmena: " + data[0] + "\n");
                                                parent.getTaConsole().append("----------------------------------------\n");
                                            }
                                        }
                                    }
                                    
                                    if (numOtherPlayers == 0) {
                                        parent.getCbPlayers().addItem("Nema drugih aktivnih igrača");
                                    }
                                    
                                    parent.getCbPlayers().setEnabled(true);
                                    parent.getCbPlayers().revalidate();
                                    parent.getCbPlayers().repaint();
                                    
                                    System.out.println("Uspešno osveženo! Broj stavki u ComboBox-u: " + parent.getCbPlayers().getItemCount());
                                    
                                } catch (Exception problem) {
                                    System.out.println("Greška pri parsiranju liste: " + problem.getMessage());
                                    problem.printStackTrace();
                                }
                            }
                        });
                    }
                    else if(line.startsWith("ARRIVED_REQUEST;")){
                        String[] tokeni = line.split(";");
                        String from = "";
                        String offer = "";
                        String require = "";
                        
                        for(String tok : tokeni){
                            if (tok.startsWith("from:")) from = tok.substring(5).trim();
                            if (tok.startsWith("offer:")) offer = tok.substring(6).trim();
                            if (tok.startsWith("require:")) require = tok.substring(8).trim();
                        }
                        
                        String finalFrom = from;
                        String finalOffer = offer;
                        String finalRequire = require;
                        
                        javax.swing.SwingUtilities.invokeLater(new Runnable(){
                            @Override
                            public void run(){
                                int answer = javax.swing.JOptionPane.showConfirmDialog(parent,
                                        "Igrač " + finalFrom + " nudi sličice: [" + finalOffer + "], a traži od Vas: [" + finalRequire + "]. Da li prihvatate razmenu?",
                                        "Zahtev za razmenu", javax.swing.JOptionPane.YES_NO_OPTION);
                                if (answer == javax.swing.JOptionPane.YES_OPTION){
                                    parent.getTaConsole().append("Prihvacena razmena sa " + finalFrom + "\n");
                                    try{
                                        java.io.PrintWriter pwFromClass = new java.io.PrintWriter(parent.getSoc().getOutputStream(), true);
                                        pwFromClass.println("EXCHANGE_ACCEPTED;peer:" + finalFrom + ";hisOffer:" + finalOffer + ";hisRequire:" + finalRequire);
                                    }catch(Exception problem){
                                        parent.getTaConsole().append("Greška prilikom slanja potvrde serveru.\n");
                                    }
                                }else{
                                    parent.getTaConsole().append("Odbijena razmena.\n");
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
