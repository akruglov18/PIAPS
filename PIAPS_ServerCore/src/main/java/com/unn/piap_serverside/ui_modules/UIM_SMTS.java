/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.unn.piap_serverside.ui_modules;

import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;

/**
 *
 * @author STALKER
 */
public class UIM_SMTS extends javax.swing.JPanel {
    private SCMI router;
    /**
     * Creates new form UIM_SMTS
     */
    public UIM_SMTS() {
        initComponents();
    }
    
    public void setRouter(SCMI router) {
        this.router = router;
    }
    
    public void setStatusLabel(String status) {
        statusLabel.setText(status);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        portNumberTF = new javax.swing.JTextField();
        setPortBttn = new javax.swing.JButton();
        stopListeningBttn = new javax.swing.JButton();
        startListeningBttn = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("номер порта");

        statusLabel.setText("не задан");

        setPortBttn.setText("задать");
        setPortBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPortBttnActionPerformed(evt);
            }
        });

        stopListeningBttn.setText("прекратить");
        stopListeningBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopListeningBttnActionPerformed(evt);
            }
        });

        startListeningBttn.setText("слушать");
        startListeningBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startListeningBttnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(portNumberTF, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setPortBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startListeningBttn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopListeningBttn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(portNumberTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setPortBttn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startListeningBttn)
                    .addComponent(stopListeningBttn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setPortBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPortBttnActionPerformed
        Integer port = null;

        try {
            port = Integer.parseInt(portNumberTF.getText());
        } catch(NumberFormatException ex) {
            port = null;
        }

        if (port == null) {
            statusLabel.setText("error");
            return;
        } else {
            statusLabel.setText("задан");
        }
        
        SCM msg = SCM.nm()
                .setFrom(SCM.TID.UI_THREAD)
                .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                .setType(SCM.TYPE.UIT_SET_PORT)
                .setBody(port);
        router.sendMessage(msg);
    }//GEN-LAST:event_setPortBttnActionPerformed

    private void stopListeningBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopListeningBttnActionPerformed
        SCM msg = SCM.nm()
                .setFrom(SCM.TID.UI_THREAD)
                .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                .setType(SCM.TYPE.UIT_STOP_LISTENING)
                .setBody(null);
        router.sendMessage(msg);
    }//GEN-LAST:event_stopListeningBttnActionPerformed

    private void startListeningBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startListeningBttnActionPerformed
        SCM msg = SCM.nm()
                .setFrom(SCM.TID.UI_THREAD)
                .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                .setType(SCM.TYPE.UIT_START_LISTENING)
                .setBody(null);
        router.sendMessage(msg);
    }//GEN-LAST:event_startListeningBttnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField portNumberTF;
    private javax.swing.JButton setPortBttn;
    private javax.swing.JButton startListeningBttn;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stopListeningBttn;
    // End of variables declaration//GEN-END:variables
}
