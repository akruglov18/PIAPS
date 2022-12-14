/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.UI_ResourseCount;
import com.unn.piap_serverside.interfaces.SCMI;
import com.unn.piap_serverside.ui_modules.UIM_SMTS;

/**
 *
 * @author STALKER
 */
public class UIThread extends javax.swing.JFrame implements UIChangerThread.UICTIfc{
    private final SCMI routerThrd;
    
    /**
     * Creates new form UIThread
     */
    public UIThread() {
        initComponents();
        
        RouterThread routerThread = new RouterThread();
        this.routerThrd = routerThread;
        
        UIChangerThread uict = new UIChangerThread(routerThread, this);
        routerThread.registerChild(uict);
        
        DBManagerThread dbmt = new DBManagerThread(routerThread);
        routerThread.registerChild(dbmt);
        
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(routerThread));
        
        SocketManagerThread smt = new SocketManagerThread(routerThrd);
        routerThread.registerChild(smt);
        
        uIM_SMTS1.setRouter(routerThread);
        uIM_DBS1.setRouter(routerThread);
        uIM_NP_IP1.setRouter(routerThread);
        
        
        
        routerThread.start();
        uict.start();
        dbmt.start();
        smt.start();
        
        
        
    }
    
    
    // UIChangerThread interface implementation
    @Override
    public void setServerSocketStatus(String status) {
        uIM_SMTS1.setStatusLabel(status);
    }
    
    @Override
    public void updateDbStatusLabel(String status) {
        uIM_DBS1.updateStatusLabel(status);
    }

    @Override
    public void updateDbResoursesCnt(UI_ResourseCount rc) {
        uIM_DBS1.updateDbResourses(rc);
    }
    
    @Override
    public void setInfoPacketText(String text) {
        uIM_NP_IP1.setInfoPacketText(text);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        uIM_SMTS1 = new com.unn.piap_serverside.ui_modules.UIM_SMTS();
        uIM_DBS1 = new com.unn.piap_serverside.ui_modules.UIM_DBS();
        uIM_NP_IP1 = new com.unn.piap_serverside.ui_modules.UIM_NP_IP();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(uIM_DBS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(uIM_SMTS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uIM_NP_IP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(uIM_DBS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(uIM_SMTS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uIM_NP_IP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(275, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UIThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UIThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UIThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UIThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIThread().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.unn.piap_serverside.ui_modules.UIM_DBS uIM_DBS1;
    private com.unn.piap_serverside.ui_modules.UIM_NP_IP uIM_NP_IP1;
    private com.unn.piap_serverside.ui_modules.UIM_SMTS uIM_SMTS1;
    // End of variables declaration//GEN-END:variables
}
