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
                        
                        parent.getTaConsole().append("*** VASE SLICICE ***\n");
                        parent.getTaConsole().append("Duplikati za razmenu: " + allDuplicates + "\n");
                        parent.getTaConsole().append("Slicice koje Vam nedostaju: " + allMissing + "\n"); 
                    }else{
                        parent.getTaConsole().append("Server: " + line);
                    }   
                }else{
                    parent.getTaConsole().append("Server je zatvorio vezu.\n");
                    break;
                }
            }catch (IOException problem){
                Logger.getLogger(ReceiveStickersFromServer.class.getName()).log(Level.SEVERE, null, problem);
                parent.getTaConsole().append("Izgubljena konekcija sa serverom!\n");
                break;
            }
        }
    }
    
}
