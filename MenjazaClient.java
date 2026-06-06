package com.mycompany.menjazaclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
        btnSendRequest.setText("Pošalji zahtev za razmenu");
        btnSendRequest.setEnabled(false);
        btnSendRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendRequestActionPerformed(evt);
            }
        });
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menjaza sličica");
        
        btnConnect.setText("Konektuj se");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });
        
        jLabel1.setText("Ime igrača: ");
        jLabel1.setEnabled(false);
        tfMyName.setEnabled(false);
        
        btnSendUserName.setText("Uđi u igru");
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
        cbPlayers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nema aktivnih igrača" }));
        cbPlayers.setEnabled(false);
        
        btnDelete = new javax.swing.JButton();
        btnDelete.setText("Obriši"); 
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
            
            taConsole.append("Uspešno uspostavljena mreža sa serverom!\n");
            
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
            btnDelete.setEnabled(true); 
            taConsole.append("Ime: " + ime + " je poslato serveru! \n");
            taConsole.append("Čeka se inicijalni set sličica... \n");
            
            this.rsfs = new ReceiveStickersFromServer(this);
            Thread thr = new Thread(rsfs);
            thr.start();
        } else {
            JOptionPane.showMessageDialog(this, "Unesite korisničko ime!");
        }
    }
    
    private void btnSendRequestActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedPlayer = (String) cbPlayers.getSelectedItem();
        if (selectedPlayer == null || selectedPlayer.equals("Nema aktivnih igraca") || selectedPlayer.equals("Nema drugih aktivnih igrača")){
            JOptionPane.showMessageDialog(this, "Izaberite igrača za razmenu");
            return;
        }

        String cleanTargetPlayer = selectedPlayer;
        if (cleanTargetPlayer.contains("(")) {
            cleanTargetPlayer = cleanTargetPlayer.substring(0, cleanTargetPlayer.indexOf("(")).trim();
        }

        int maxMogucihRazmena = 0;
        try {
            if (selectedPlayer.contains("(") && selectedPlayer.contains(")")) {
                String brStr = selectedPlayer.substring(selectedPlayer.indexOf("(") + 1, selectedPlayer.indexOf(")")).trim();
                if (brStr.contains("|")) {
                    brStr = brStr.split("\\|")[0].trim();
                }
                maxMogucihRazmena = Integer.parseInt(brStr);
            }
        } catch (Exception problem) {
            maxMogucihRazmena = 0;
        }

        boolean checkedDuplicates = false;
        boolean checkedMissing = false;

        int countOffer = 0;
        int countRequire = 0;

        for (int i = 1; i <= 99; i++) {
            if (mapDuplicates.get(i).isSelected()) {
                checkedDuplicates = true;
                countOffer++;
            }
            if (mapMissing.get(i).isSelected()) {
                checkedMissing = true;
                countRequire++;
            }
        }

        boolean takeAll = (!checkedDuplicates && !checkedMissing);

        String offer = "";
        String require = "";

        if (takeAll) {
            if (maxMogucihRazmena == 0){
                JOptionPane.showMessageDialog(this, "Sa igračem " + cleanTargetPlayer + " nemate zajedničkih sličica!", "Validacija razmene", JOptionPane.WARNING_MESSAGE);
                return;
            }

            offer = "SVE";
            require = "SVE";

        } else {
            if (countOffer != countRequire) {
                JOptionPane.showMessageDialog(this, 
                    "Greška: Broj ponuđenih (" + countOffer + ") i traženih sličica (" + countRequire + ") mora biti jednak za razmenu 1-na-1!", 
                    "Validacija razmene", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (countOffer > maxMogucihRazmena) {
                JOptionPane.showMessageDialog(this, 
                    "Greška: Sa igračem " + cleanTargetPlayer + " možete razmeniti maksimalno " + maxMogucihRazmena + " sličica!\n" +
                    "Vi ste selektovali " + countOffer + ".", 
                    "Validacija limita razmene", 
                    JOptionPane.ERROR_MESSAGE);
                return; 
            }

            StringBuilder offerSB = new StringBuilder();
            for (int i = 1; i <= 99; i++) {
                if (mapDuplicates.get(i).isSelected()) {
                    offerSB.append(i).append(",");
                }
            }
            offer = offerSB.toString();
            if (offer.endsWith(",")) {
                offer = offer.substring(0, offer.length() - 1);
            }
            if (offer.isEmpty()) offer = "Nista";

            StringBuilder requireSB = new StringBuilder();
            for (int i = 1; i <= 99; i++) {
                if (mapMissing.get(i).isSelected()) {
                    requireSB.append(i).append(",");
                }
            }
            require = requireSB.toString();
            if (require.endsWith(",")) {
                require = require.substring(0, require.length() - 1);
            }
            if (require.isEmpty()) require = "Nista";
        } 

        taConsole.append("\n========================================\n");
        taConsole.append("Predlog razmene za igrača: " + cleanTargetPlayer + "\n");
        taConsole.append("Automatski: " + takeAll + "\n");
        taConsole.append("Nudim duplikate: " + offer + "\n");
        taConsole.append("Tražim sličice: " + require + "\n");
        taConsole.append("========================================\n");

        String messageForServer = "EXCHANGE_PROPOSAL;target:" + cleanTargetPlayer + ";offer:" + offer + ";require:" + require;
        this.pw.println(messageForServer);

        taConsole.append("\nZahtev za razmenu poslat serveru... Ceka se potvrda...\n");
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<Integer> zaBrisanjeDuplikata = new ArrayList<>();
        ArrayList<Integer> zaBrisanjeMissing = new ArrayList<>();

        for (int i = 1; i <= 99; i++) {
            if (mapDuplicates.get(i).isSelected()) {
                zaBrisanjeDuplikata.add(i);
            }
            if (mapMissing.get(i).isSelected()) {
                zaBrisanjeMissing.add(i);
            }
        }

        if (zaBrisanjeDuplikata.isEmpty() && zaBrisanjeMissing.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Morate selektovati barem jednu sličicu u panelima za brisanje!");
            return;
        }

        StringBuilder dupSB = new StringBuilder();
        for (int num : zaBrisanjeDuplikata) dupSB.append(num).append(",");
        String dupStr = dupSB.toString().isEmpty() ? "Nista" : dupSB.substring(0, dupSB.length() - 1);

        StringBuilder missSB = new StringBuilder();
        for (int num : zaBrisanjeMissing) missSB.append(num).append(",");
        String missStr = missSB.toString().isEmpty() ? "Nista" : missSB.substring(0, missSB.length() - 1);

        String porukaZaServer = "MANUAL_DELETE;duplicates:" + dupStr + ";missing:" + missStr;
        this.pw.println(porukaZaServer);

        taConsole.append("\nZahtev za ručno brisanje selektovanih sličica je poslat serveru...\n");
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
