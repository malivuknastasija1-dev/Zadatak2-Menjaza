/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.menjazaclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class MenjazaClient extends javax.swing.JFrame {
    
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ReceiveStickersFromServer rsfs;
    private java.util.HashMap<Integer, javax.swing.JCheckBox> mapDuplicates = new java.util.HashMap<>();
    private java.util.HashMap<Integer, javax.swing.JCheckBox> mapMissing = new java.util.HashMap<>();
    
    private javax.swing.JPanel pnlDuplicates;
    private javax.swing.JPanel pnlMissing;
    private javax.swing.JButton btnSendRequest;
    private javax.swing.JButton btnDelete;
    private javax.swing.JComboBox<String> cbPlayers;
    
    private boolean uputstvoPrikazano = false;

    public MenjazaClient() {
        initComponents();
    }
    
    public JTextArea getTaConsole() {
        return taConsole;
    }
    
    public BufferedReader getBr() {
        return br;
    }
    
    public Socket getSoc() {
        return socket;
    }
    
    public javax.swing.JButton getBtnSendRequest() {
        return this.btnSendRequest;
    }

    public javax.swing.JButton getBtnDelete() {
        return this.btnDelete;
    }
    
    public boolean isUputstvoPrikazano() {
        return this.uputstvoPrikazano;
    }
    
    public void setUputstvoPrikazano(boolean prikazi) {
        this.uputstvoPrikazano = prikazi;
    }
    
    public javax.swing.JComboBox<String> getCbPlayers() {
        return this.cbPlayers;
    }

    public javax.swing.JTextField getTfMyName() {
        return this.tfMyName;
    }
    
    private void initComponents() {
        pnlDuplicates = new javax.swing.JPanel(new java.awt.GridLayout(10, 10, 1, 1));
        pnlMissing = new javax.swing.JPanel(new java.awt.GridLayout(10, 10, 1, 1));
        
        pnlDuplicates.setBorder(javax.swing.BorderFactory.createTitledBorder("Moji Duplikati (Stiklirano = Imam duplikat)"));
        pnlMissing.setBorder(javax.swing.BorderFactory.createTitledBorder("Slicice koje mi nedostaju (Stiklirano = Nedostaje)"));

        mapDuplicates.clear();
        mapMissing.clear();
        
        for (int i = 1; i <= 99; i++) {
            javax.swing.JCheckBox cbDup = new javax.swing.JCheckBox(String.valueOf(i));
            cbDup.setEnabled(false);
            mapDuplicates.put(i, cbDup);
            pnlDuplicates.add(cbDup);
            
            javax.swing.JCheckBox cbMiss = new javax.swing.JCheckBox(String.valueOf(i));
            cbMiss.setEnabled(false);
            mapMissing.put(i, cbMiss);
            pnlMissing.add(cbMiss);
        }

        btnConnect = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfMyName = new javax.swing.JTextField();
        btnSendUserName = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taConsole = new javax.swing.JTextArea();
        
        btnSendRequest = new javax.swing.JButton();
        btnSendRequest.setText("Posalji zahtev za razmenu");
        btnSendRequest.setEnabled(false);
        btnSendRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendRequestActionPerformed(evt);
            }
        });
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menjaza slicica klijent");
        
        btnConnect.setText("Konektuj se");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });
        
        jLabel1.setText("Ime igraca: ");
        jLabel1.setEnabled(false);
        tfMyName.setEnabled(false);
        
        btnSendUserName.setText("Udji u igru");
        btnSendUserName.setEnabled(false);
        btnSendUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendUserNameActionPerformed(evt);
            }
        });
        
        taConsole.setColumns(20);
        taConsole.setRows(4);
        taConsole.setEnabled(false);
        taConsole.setLineWrap(true);
        taConsole.setWrapStyleWord(true);
        jScrollPane1.setViewportView(taConsole);
        
        cbPlayers = new javax.swing.JComboBox<>();
        cbPlayers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nema aktivnih igraca" }));
        cbPlayers.setEnabled(false);
        
        btnDelete = new javax.swing.JButton();
        btnDelete.setText("Prihvati i 'zasivi'");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
      
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfMyName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSendUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDuplicates, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlMissing, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbPlayers, 0, 320, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSendRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pnlDuplicates, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(pnlMissing, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSendRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    public void updateDuplicates(String[] nums) {
        for (javax.swing.JCheckBox cb : mapDuplicates.values()) {
            cb.setEnabled(false);
            cb.setSelected(false);
        }
        for (String numStr : nums) {
            if (!numStr.trim().isEmpty()) {
                int i = Integer.parseInt(numStr.trim());
                if (mapDuplicates.containsKey(i)) {
                    mapDuplicates.get(i).setEnabled(true);
                }
            }
        }
        pnlDuplicates.revalidate();
        pnlDuplicates.repaint();
    }
    
    public void updateMissing(String[] nums) {
        for (javax.swing.JCheckBox cb : mapMissing.values()) {
            cb.setEnabled(false);
            cb.setSelected(false);
        }
        for (String numStr : nums) {
            if (!numStr.trim().isEmpty()) {
                int i = Integer.parseInt(numStr.trim());
                if (mapMissing.containsKey(i)) {
                    mapMissing.get(i).setEnabled(true);
                }
            }
        }
        pnlMissing.revalidate();
        pnlMissing.repaint();
    }
    
    public void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.socket = new Socket("127.0.0.1", 4925);
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(this.socket.getOutputStream(), true);
            
            taConsole.append("Uspesno uspostavljena mreza sa serverom!\n");
            
            jLabel1.setEnabled(true);
            tfMyName.setEnabled(true);
            btnSendUserName.setEnabled(true);
            btnConnect.setEnabled(false);
        } catch (IOException problem) {
            JOptionPane.showMessageDialog(this, "Greska pri povezivanju na server.");
            Logger.getLogger(MenjazaClient.class.getName()).log(Level.SEVERE, null, problem);
        }
    }
    
    private void btnSendUserNameActionPerformed(java.awt.event.ActionEvent evt) {
        String ime = tfMyName.getText().trim();
        if (!ime.equals("")) {
            this.pw.println(ime);
            tfMyName.setEnabled(false);
            btnSendUserName.setEnabled(false);
            taConsole.setEnabled(true);
            taConsole.append("Ime: " + ime + " je poslato serveru! \n");
            taConsole.append("Ceka se inicijalni set slicica... \n");
            taConsole.append("------------------------------------------------------------------------\n");
            
            this.rsfs = new ReceiveStickersFromServer(this);
            Thread thr = new Thread(rsfs);
            thr.start();
        } else {
            JOptionPane.showMessageDialog(this, "Unesite vase ime!");
        }
    }
    
    private void btnSendRequestActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedPlayer = (String) cbPlayers.getSelectedItem();
        if (selectedPlayer == null || selectedPlayer.equals("Nema aktivnih igraca")){
            JOptionPane.showMessageDialog(this, "Izaberite igraca za razmenu");
            return;
        }
        
        String cleanTargetPlayer = selectedPlayer;
        if (cleanTargetPlayer.contains("(")) {
            cleanTargetPlayer = cleanTargetPlayer.substring(0, cleanTargetPlayer.indexOf("(")).trim();
        }
        
        boolean checkedDuplicates = false;
        boolean checkedMissing = false;

        for (int i = 1; i <= 99; i++) {
            if (mapDuplicates.get(i).isSelected()) {
                checkedDuplicates = true;
            }
            if (mapMissing.get(i).isSelected()) {
                checkedMissing = true;
            }
        }

        boolean takeAll = (!checkedDuplicates && !checkedMissing);

        StringBuilder offerSB = new StringBuilder();
        for (int i = 1; i <= 99; i++) {
            if (takeAll) {
                if (mapDuplicates.get(i).isEnabled()) {
                    offerSB.append(i).append(",");
                }
            } else {
                if (mapDuplicates.get(i).isSelected()) {
                    offerSB.append(i).append(",");
                }
            }
        }

        String offer = offerSB.toString();
        if (offer.endsWith(",")) {
            offer = offer.substring(0, offer.length() - 1);
        }
        if (offer.isEmpty()) {
            offer = "Nista";
        }

        StringBuilder requireSB = new StringBuilder();
        for (int i = 1; i <= 99; i++) {
            if (takeAll) {
                if (mapMissing.get(i).isEnabled()) {
                    requireSB.append(i).append(",");
                }
            } else {
                if (checkedMissing) {
                    if (mapMissing.get(i).isSelected()) {
                        requireSB.append(i).append(",");
                    }
                }
            }
        }

        String require = requireSB.toString();
        if (require.endsWith(",")) {
            require = require.substring(0, require.length() - 1);
        }
        if (require.isEmpty()) {
            require = "Nista";
        }
        
        taConsole.append("\n========================================\n");
        taConsole.append("Predlog razmene za igraca: " + cleanTargetPlayer + "\n");
        taConsole.append("Nudim duplikate: " + offer + "\n");
        taConsole.append("Trazim sličice: " + require + "\n");
        taConsole.append("========================================\n");

        // Šaljemo očišćeno ime serveru
        String porukaZaServer = "EXCHANGE_PROPOSAL;target:" + cleanTargetPlayer + ";offer:" + offer + ";require:" + require;
        this.pw.println(porukaZaServer);

        taConsole.append("\nZahtev za razmenu poslat serveru... Čeka se potvrda...\n");
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int grayDuplicates = 0;
        int grayMissing = 0;
        
        for(int i = 1; i <= 99; i++){
            if (mapDuplicates.get(i).isSelected()){
                mapDuplicates.get(i).setSelected(false);
                mapDuplicates.get(i).setEnabled(false);
                grayDuplicates++;
            }
            
            if (mapMissing.get(i).isSelected()){
                mapMissing.get(i).setSelected(false);
                mapMissing.get(i).setEnabled(false);
                grayMissing++;
            }
        }
        
        if (grayDuplicates == 0 && grayMissing == 0){
            for(int i = 1; i <= 99; i++){
                if (mapDuplicates.get(i).isEnabled()){
                    mapDuplicates.get(i).setEnabled(false);
                    grayDuplicates++;
                }
                
                if (mapMissing.get(i).isEnabled()){
                    mapMissing.get(i).setEnabled(false);
                    grayMissing++;
                }
            }
        }
        
        pnlDuplicates.revalidate();
        pnlDuplicates.repaint();
        pnlMissing.revalidate();
        pnlMissing.repaint();
        
        taConsole.append("Uspesna razmena!\n");
        taConsole.append("Sklonjeno je: " + grayDuplicates + " duplikata i dodato je: " + grayMissing + " nedostajucih slicica\n");
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
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


