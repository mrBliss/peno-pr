/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TGUI.java
 *
 * Created on 6-dec-2010, 23:58:19
 */
package pcpanic;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintStream;
import java.util.EmptyStackException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommandConnector;
import pcpanic.NXT.Pilot;


import pcpanic.Server.Server;

/**
 *
 * @author Me
 */
public class GUI extends javax.swing.JFrame {

    private static final long serialVersionUID = 1;
    private Pilot p;
    private Server s;

    public static TrackShower ts;
    private PrintStream serverStream;

    /** Creates new form TGUI */
    public GUI() {
        initComponents();
        chkOutActionPerformed(null);
        chkErrActionPerformed(null);
        chkServActionPerformed(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panRem = new javax.swing.JPanel();
        sliderL = new javax.swing.JSlider();
        sliderR = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRem = new javax.swing.JTextArea();
        txtComment = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtCustom = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        toggleConnection = new javax.swing.JToggleButton();
        buttonActive = new javax.swing.JToggleButton();
        jButton6 = new javax.swing.JButton();
        panTrace = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnFwd = new javax.swing.JButton();
        btnLef = new javax.swing.JButton();
        btnRight = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        btnTrack = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        panServ = new javax.swing.JPanel();
        inpServ = new javax.swing.JTextField();
        inpMess = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inpSubsc = new javax.swing.JTextField();
        jToolBar3 = new javax.swing.JToolBar();
        toggleServer = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        chkOut = new javax.swing.JCheckBox();
        chkErr = new javax.swing.JCheckBox();
        chkServ = new javax.swing.JCheckBox();
        chkNXJ = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        streamPane1 = new pcpanic.StreamPane();
        turtlePane1 = new ch.aplu.turtle.TurtlePane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("P&O Team GEEL GUI");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("nxt.png")));

        sliderL.setMajorTickSpacing(50);
        sliderL.setMinimum(-100);
        sliderL.setMinorTickSpacing(10);
        sliderL.setOrientation(javax.swing.JSlider.VERTICAL);
        sliderL.setPaintLabels(true);
        sliderL.setPaintTicks(true);
        sliderL.setValue(0);
        sliderL.setEnabled(false);
        sliderL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderLMouseReleased(evt);
            }
        });
        sliderL.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sliderLMouseDragged(evt);
            }
        });

        sliderR.setMajorTickSpacing(50);
        sliderR.setMinimum(-100);
        sliderR.setMinorTickSpacing(10);
        sliderR.setOrientation(javax.swing.JSlider.VERTICAL);
        sliderR.setPaintLabels(true);
        sliderR.setPaintTicks(true);
        sliderR.setValue(0);
        sliderR.setEnabled(false);
        sliderR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderRMouseReleased(evt);
            }
        });
        sliderR.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sliderRMouseDragged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txtRem.setColumns(20);
        txtRem.setRows(1);
        txtRem.setTabSize(0);
        txtRem.setEnabled(false);
        txtRem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRemKeyPresser(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRemKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtRem);

        txtComment.setText("Custom Command");

        txtCustom.setEnabled(false);
        txtCustom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustomKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(txtCustom);

        jLabel4.setText("Real Time Control");

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        toggleConnection.setText("Connect NXT");
        toggleConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleConnectionActionPerformed(evt);
            }
        });
        jToolBar2.add(toggleConnection);

        buttonActive.setText("Activate Remote");
        buttonActive.setEnabled(false);
        buttonActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActiveActionPerformed(evt);
            }
        });
        jToolBar2.add(buttonActive);

        jButton6.setText("PANIC");
        jButton6.setEnabled(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton6);

        javax.swing.GroupLayout panRemLayout = new javax.swing.GroupLayout(panRem);
        panRem.setLayout(panRemLayout);
        panRemLayout.setHorizontalGroup(
            panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRemLayout.createSequentialGroup()
                .addGroup(panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRemLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(sliderL, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sliderR, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panRemLayout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtComment)))
                            .addGroup(panRemLayout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panRemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panRemLayout.setVerticalGroup(
            panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRemLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRemLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(txtComment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sliderL, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sliderR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(262, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Remote", panRem);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        btnFwd.setText("Forward");
        btnFwd.setEnabled(false);
        btnFwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFwdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(btnFwd, gridBagConstraints);

        btnLef.setText("Left");
        btnLef.setEnabled(false);
        btnLef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLefActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(btnLef, gridBagConstraints);

        btnRight.setText("Right");
        btnRight.setEnabled(false);
        btnRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRightActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(btnRight, gridBagConstraints);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnTrack.setText("Start");
        btnTrack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrackActionPerformed(evt);
            }
        });
        jToolBar1.add(btnTrack);

        jButton4.setText("Undo");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setText("Reset");
        jButton5.setEnabled(false);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        javax.swing.GroupLayout panTraceLayout = new javax.swing.GroupLayout(panTrace);
        panTrace.setLayout(panTraceLayout);
        panTraceLayout.setHorizontalGroup(
            panTraceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTraceLayout.createSequentialGroup()
                .addGroup(panTraceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panTraceLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panTraceLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panTraceLayout.setVerticalGroup(
            panTraceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTraceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(221, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Turtle", panTrace);

        panServ.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        inpServ.setText("race.geel");

        inpMess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inpMessKeyTyped(evt);
            }
        });

        jLabel2.setText("Send to Server");

        jLabel3.setText("Subscribe to Server");

        inpSubsc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inpSubscKeyTyped(evt);
            }
        });

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        toggleServer.setText("Connect To Message Server");
        toggleServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleServerActionPerformed(evt);
            }
        });
        jToolBar3.add(toggleServer);

        javax.swing.GroupLayout panServLayout = new javax.swing.GroupLayout(panServ);
        panServ.setLayout(panServLayout);
        panServLayout.setHorizontalGroup(
            panServLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panServLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panServLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inpServ, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addComponent(inpMess, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(inpSubsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panServLayout.setVerticalGroup(
            panServLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panServLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inpServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inpMess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inpSubsc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(330, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Server", panServ);

        chkOut.setSelected(true);
        chkOut.setText("System Out");
        chkOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOutActionPerformed(evt);
            }
        });

        chkErr.setSelected(true);
        chkErr.setText("System Err");
        chkErr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkErrActionPerformed(evt);
            }
        });

        chkServ.setSelected(true);
        chkServ.setText("Server");
        chkServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkServActionPerformed(evt);
            }
        });

        chkNXJ.setText("NXJ");
        chkNXJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNXJActionPerformed(evt);
            }
        });

        jLabel6.setText("Select the streams to view in the console");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkOut)
                    .addComponent(chkErr)
                    .addComponent(chkServ)
                    .addComponent(chkNXJ)
                    .addComponent(jLabel6))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkErr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkServ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkNXJ)
                .addContainerGap(385, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Streams", jPanel2);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pcpanic/logo.jpg"))); // NOI18N

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize()+37));
        jLabel5.setText("P&O Team Geel");

        streamPane1.setBackground(new java.awt.Color(240, 240, 240));
        streamPane1.setEditable(false);
        jScrollPane3.setViewportView(streamPane1);

        jTabbedPane2.addTab("Console", jScrollPane3);

        turtlePane1.setBackgroundColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout turtlePane1Layout = new javax.swing.GroupLayout(turtlePane1);
        turtlePane1.setLayout(turtlePane1Layout);
        turtlePane1Layout.setHorizontalGroup(
            turtlePane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );
        turtlePane1Layout.setVerticalGroup(
            turtlePane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("TurtlePane", turtlePane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sliderLMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderLMouseReleased
        p.apply();
}//GEN-LAST:event_sliderLMouseReleased

    private void sliderRMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderRMouseReleased
        p.apply();
}//GEN-LAST:event_sliderRMouseReleased

    private void txtRemKeyPresser(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemKeyPresser
        txtRem.setText("");
        char ch = evt.getKeyChar();
        switch (ch) {
            case 'a':
                p.setL(90);
                p.setR(90);
                p.apply();
                break;
            case 'e':
                p.setL(-90);
                p.setR(-90);
                p.apply();
                break;
            case 'q':
                p.difL(-5);
                p.difR(5);
                p.apply();
                break;
            case 'd':
                p.difL(5);
                p.difR(-5);
                p.apply();
                break;
            case 'z':
                p.meanSpeed();
                p.difL(5);
                p.difR(5);
                p.apply();
                p.apply();
                break;
            case 's':
                p.meanSpeed();
                p.difL(-5);
                p.difR(-5);
                p.apply();
                break;
            case '\n':
                p.meanSpeed();
                p.apply();
                break;
            case 'à':
                p.fullStop();
                p.apply();
                break;
            case 'µ':
                p.multL(-1);
                p.multR(-1);
                p.apply();
                break;
            case ' ':
                p.toeter(3);
        }
    }//GEN-LAST:event_txtRemKeyPresser

    private void txtRemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemKeyReleased
}//GEN-LAST:event_txtRemKeyReleased

    private void txtCustomKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomKeyTyped
        try {
            if (evt.getKeyChar() == '\n') {
                p.send(Byte.parseByte(txtCustom.getText().trim()));
                txtCustom.setText("");
            }
        } catch (Exception e) {
            txtCustom.setText("");
        }
}//GEN-LAST:event_txtCustomKeyTyped

    private void buttonActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActiveActionPerformed
        if (buttonActive.isSelected()) {
            buttonActive.setText("Deactivate Remote");
            if (p != null) {
                sliderR.setEnabled(true);
                sliderL.setEnabled(true);
                p.setL(0);
                p.setR(0);
                txtRem.setEnabled(true);
                p.startRem();
            } else {
                System.err.println("Connecteer eerst naar een NXT alvorens de remote control te activeren");
                buttonActive.setSelected(false);
            }
        } else {
            buttonActive.setText("Activate Remote");
            sliderR.setEnabled(false);
            sliderL.setEnabled(false);
            txtRem.setEnabled(false);
            p.stopRem();
        }


    }//GEN-LAST:event_buttonActiveActionPerformed

    private void toggleConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleConnectionActionPerformed
        if (toggleConnection.isSelected()) {

            try {
                p = new Pilot(sliderL, sliderR);
                buttonActive.setEnabled(true);
                txtCustom.setEnabled(true);
                jButton6.setEnabled(true);
                toggleConnection.setText("Disconnect NXT");
            } catch (Exception ex) {
                System.err.println("Connecteren mislukt "+ex);
                toggleConnection.setSelected(false);
            }
        } else {
            p.close();
            p = null;
            buttonActive.setSelected(false);
            buttonActive.setEnabled(false);
            buttonActive.setText("Activate Remote");
            txtCustom.setEnabled(false);
            txtRem.setEnabled(false);
            jButton6.setEnabled(false);
            toggleConnection.setText("Connect NXT");
        }
}//GEN-LAST:event_toggleConnectionActionPerformed

    private void inpMessKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inpMessKeyTyped
        try {
            if (evt.getKeyChar() == '\n') {
                s.sendMessage(inpServ.getText().trim(), inpMess.getText().trim());
                inpMess.setText("");
                System.err.println("Message succeeded");
            }
        } catch (Exception e) {
            System.err.println("Message failed");
        }
}//GEN-LAST:event_inpMessKeyTyped

    private void inpSubscKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inpSubscKeyTyped
        try {
            if (evt.getKeyChar() == '\n') {
                s.subscribe(inpSubsc.getText().trim(), serverStream, p);
                inpSubsc.setText("");
                System.err.println("Subscribe succeeded");
            }
        } catch (Exception e) {
            System.err.println("Subscribe failed");
        }
}//GEN-LAST:event_inpSubscKeyTyped

    private void btnTrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrackActionPerformed
        ts = new TrackShower(turtlePane1);
        btnTrack.setEnabled(false);
        btnRight.setEnabled(true);
        btnLef.setEnabled(true);
        btnFwd.setEnabled(true);
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
}//GEN-LAST:event_btnTrackActionPerformed

    private void chkOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOutActionPerformed
        if (chkOut.isSelected()) {
            TextAreaOutputStream s = new TextAreaOutputStream(streamPane1);
            PrintStream ts = new PrintStream(s, true);
            System.setOut(ts);
        } else {
            System.setOut(null);
        }
    }//GEN-LAST:event_chkOutActionPerformed

    private void chkErrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkErrActionPerformed
     if (chkErr.isSelected()) {
            TextAreaOutputStream s = new TextAreaOutputStream(streamPane1, Color.RED);
            PrintStream ts = new PrintStream(s, true);
            System.setErr(ts);
        } else {
            System.setErr(null);
        }
    }//GEN-LAST:event_chkErrActionPerformed

    private void chkNXJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNXJActionPerformed
        if (chkNXJ.isSelected()) {
            if (p != null && p.inputStream != null) {
                streamPane1.traceIStream(p.inputStream);
            } else {
                System.err.println("Unable to connect to NXT-stream");
                chkNXJ.setSelected(false);
            }
        } else {
            streamPane1.stopITrace(p.inputStream);
        }
    }//GEN-LAST:event_chkNXJActionPerformed

    private void toggleServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleServerActionPerformed
        if (toggleServer.isSelected()) {
            try {
                s = new Server();
                System.err.println("Connection to server established");
            } catch (IOException ex) {
                System.err.println("Connection to server failed");
                toggleServer.setSelected(false);
            }
        } else {
            s.close();
        }
    }//GEN-LAST:event_toggleServerActionPerformed

    private void btnRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRightActionPerformed
        ts.moveTurtle(ts.right);
        
    }//GEN-LAST:event_btnRightActionPerformed

    private void btnFwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFwdActionPerformed
        ts.moveTurtle(ts.forward);
    }//GEN-LAST:event_btnFwdActionPerformed

    private void btnLefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLefActionPerformed
        ts.moveTurtle(ts.left);
    }//GEN-LAST:event_btnLefActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try{
            ts.undo();
        }catch(EmptyStackException e){
            System.err.println("Cannot undo");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void sliderLMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderLMouseDragged
        p.apply();
    }//GEN-LAST:event_sliderLMouseDragged

    private void sliderRMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderRMouseDragged
        p.apply();
    }//GEN-LAST:event_sliderRMouseDragged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ts.clear();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        p.send((byte)12);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void chkServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkServActionPerformed
        TextAreaOutputStream os = new TextAreaOutputStream(streamPane1);
        serverStream = new PrintStream(os, true);
    }//GEN-LAST:event_chkServActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFwd;
    private javax.swing.JButton btnLef;
    private javax.swing.JButton btnRight;
    private javax.swing.JButton btnTrack;
    private javax.swing.JToggleButton buttonActive;
    private javax.swing.JCheckBox chkErr;
    private javax.swing.JCheckBox chkNXJ;
    private javax.swing.JCheckBox chkOut;
    private javax.swing.JCheckBox chkServ;
    private javax.swing.JTextField inpMess;
    private javax.swing.JTextField inpServ;
    private javax.swing.JTextField inpSubsc;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JPanel panRem;
    private javax.swing.JPanel panServ;
    private javax.swing.JPanel panTrace;
    private javax.swing.JSlider sliderL;
    private javax.swing.JSlider sliderR;
    private pcpanic.StreamPane streamPane1;
    private javax.swing.JToggleButton toggleConnection;
    private javax.swing.JToggleButton toggleServer;
    private ch.aplu.turtle.TurtlePane turtlePane1;
    private javax.swing.JLabel txtComment;
    private javax.swing.JTextPane txtCustom;
    private javax.swing.JTextArea txtRem;
    // End of variables declaration//GEN-END:variables
}
