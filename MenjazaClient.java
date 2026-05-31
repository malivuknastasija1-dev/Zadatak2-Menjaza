/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.menjazaclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class MenjazaClient extends javax.swing.JFrame{
    
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ReceiveStickersFromServer rsfs;
    
    public MenjazaClient(){
        initComponents();
    }
    
    public JTextArea getTaConsole(){
        return taConsole;
    }
    
    public BufferedReader getBr(){
        return br;
    }
    
    public Socket getSoc(){
        return socket;
    }
    
    private void initComponents(){
        btnConnect = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfMyName = new javax.swing.JTextField();
        btnSendUserName = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taConsole = new javax.swing.JTextArea();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menjaza slicica klijent");
        
        btnConnect.setText("Konektuj se");
        btnConnect.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                btnConnectActionPerformed(evt);
            }
        });
        
        jLabel1.setText("Ime igraca: ");
        jLabel1.setEnabled(false);
        
        tfMyName.setEnabled(false);
        
        btnSendUserName.setText("Udji u igru");
        btnSendUserName.setEnabled(false);
        btnSendUserName.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                btnSendUserNameActionPerformed(evt);
            }
        });
        
        taConsole.setColumns(20);
        taConsole.setRows(5);
        taConsole.setEnabled(false);
        jScrollPane1.setViewportView(taConsole);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfMyName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSendUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConnect)
                    .addComponent(jLabel1)
                    .addComponent(tfMyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSendUserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }
    
//   konektuj se
    public void btnConnectActionPerformed(java.awt.event.ActionEvent evt){
        try{
            this.socket = new Socket("127.0.0.1", 4925);
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
            
            taConsole.append("Uspesno uspostavljena mreza sa serverom!\n");
            
            jLabel1.setEnabled(true);
            tfMyName.setEnabled(true);
            btnSendUserName.setEnabled(true);
            btnConnect.setEnabled(false);
        }catch(IOException problem){
            JOptionPane.showMessageDialog(this, "Greska pri povezivanju na server.");
            Logger.getLogger(MenjazaClient.class.getName()).log(Level.SEVERE, null, problem);
        }
    }
    
//    udji u igru
    private void btnSendUserNameActionPerformed(java.awt.event.ActionEvent evt){
        String ime = tfMyName.getText().trim();
        if (!ime.equals("")){
            this.pw.println(ime);
            tfMyName.setEnabled(false);
            btnSendUserName.setEnabled(false);
            taConsole.setEnabled(true);
            taConsole.append("Ime: " + ime + " je poslato serveru! Ceka se inicijalni set slicica...");
            
            this.rsfs = new ReceiveStickersFromServer(this);
            Thread thr = new Thread(rsfs);
            thr.start();
        }else {
            JOptionPane.showMessageDialog(this, "Unesite vase ime!");
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run(){
                new MenjazaClient().setVisible(true);
            }
        });  
    }
    
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnSendUserName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taConsole;
    private javax.swing.JTextField tfMyName;
}
