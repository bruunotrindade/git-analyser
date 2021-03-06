/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.util.Export;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * This class is in charge of showing all results about the files analysis
 *
 * @author j2cf, Catarina
 */
public class JFrameKeyFiles extends javax.swing.JFrame {

    private MergeFiles merge;
    private Set<EditedFile> keyFiles;

    /**
     * Creates new form JFrameCommitsAnalysis
     *
     * @param merge
     * @param depBranchOne
     * @param depBranchTwo
     */
    public JFrameKeyFiles(MergeFiles merge, Map<EditedFile, Set<EditedFile>> depBranchOne, Map<EditedFile, Set<EditedFile>> depBranchTwo) {
        this.merge = merge;
        initComponents();
        setParameters();
        this.keyFiles = getKeyFiles(merge.getFilesOnBothBranch(), depBranchOne, depBranchTwo);
        showResultsTable();
        
    }

    //1- Shows the project branches that the user can select to merge - 2- shows all the existing merges - 3 - and put the name of the project
    private void setParameters() {
        jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Project " + merge.getPath().getName())
        );
        txRepositoryName.setText(merge.getPath().getName());
        txHashes.setText(merge.getHash());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hash1 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        labelRepository = new javax.swing.JLabel();
        txRepositoryName = new javax.swing.JTextField();
        txHashes = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPaneBranch1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPaneBranch2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPaneHistory = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        buttonExport = new javax.swing.JButton();

        hash1.setText("<hash>");

        jFrame1.setBounds(500, 150, 500, 500);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Key Files");

        jLabel4.setText("Branches");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/loading1.gif"))); // NOI18N
        jLabel1.setText("Loading ...");
        jLabel1.setVisible(false);

        labelRepository.setText("Repository Name");

        txRepositoryName.setEnabled(false);

        txHashes.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelRepository)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txRepositoryName, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                            .addComponent(txHashes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRepository)
                    .addComponent(txRepositoryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txHashes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPaneBranch1.setViewportView(jTable1);

        jTabbedPane1.addTab("Branch1", jScrollPaneBranch1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPaneBranch2.setViewportView(jTable2);

        jTabbedPane1.addTab("Branch2", jScrollPaneBranch2);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable4.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPaneHistory.setViewportView(jTable4);

        jTabbedPane1.addTab("Previous History", jScrollPaneHistory);

        buttonExport.setText("Export...");
        buttonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonExport)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonExport)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportActionPerformed
        Map<String, TableModel> sheet = new LinkedHashMap<>();
        sheet.put("Branch1", jTable1.getModel());
        sheet.put("Branch2", jTable2.getModel());
        sheet.put("Previous History", jTable4.getModel());

        Export.toExcel(sheet);
        JOptionPane.showMessageDialog(this, "File was sucessfully saved", null, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_buttonExportActionPerformed

    private void showResultsTable() {
        //organizes the data in the table
        showResBranch1(this.merge.getFilesOnBranchOne(), this.keyFiles);
        showResBranch2(this.merge.getFilesOnBranchTwo(), this.keyFiles);
        showResPreviousHistory(this.merge.getFilesOnPreviousHistory(), this.keyFiles);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonExport;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel hash1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneBranch1;
    private javax.swing.JScrollPane jScrollPaneBranch2;
    private javax.swing.JScrollPane jScrollPaneHistory;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    private javax.swing.JLabel labelRepository;
    private javax.swing.JTextField txHashes;
    private javax.swing.JTextField txRepositoryName;
    // End of variables declaration//GEN-END:variables

    //shows the number of commits by committers in each file on Branch 1
    private void showResBranch1(List<EditedFile> filesb1, Set<EditedFile> keyFiles) {

        List<EditedFile> finalList = getEditedFilesList(filesb1, keyFiles);
        Set<Committer> committers = getCommittersList(finalList);
        
        jTable1.setModel(getTableModel(committers, finalList));
    }

    //shows the number of commits by committers in each file on Branch 2
    private void showResBranch2(List<EditedFile> filesb2, Set<EditedFile> keyFiles) {

        List<EditedFile> finalList = getEditedFilesList(filesb2, keyFiles);
        Set<Committer> committers = getCommittersList(finalList);

        jTable2.setModel(getTableModel(committers, finalList));
    }

    //shows the number of commits by committers in each file (changed on any branch) that was changed in the history before the branch
    private void showResPreviousHistory(Set<EditedFile> filesHistory, Set<EditedFile> keyFiles) {

        List<EditedFile> finalList = getEditedFilesList(new ArrayList<>(filesHistory), keyFiles);
        Set<Committer> committers = getCommittersList(finalList);

        jTable4.setModel(getTableModel(committers, finalList));
    }

    private DefaultTableModel getTableModel(Set<Committer> committers, List<EditedFile> finalList) {

        DefaultTableModel dftModel = new DefaultTableModel(new Object[]{"File name"}, 0);

        //Includes columns with the names of all developers (branches 1 and 2)
        committers.stream().forEach((committer) -> {
            dftModel.addColumn(committer.getName());
        });

        //dftModel.addRow(new Object[]{"PREVIOUS HISTORY"});
        finalList.stream().forEach((file) -> {
            //if (file.getWhoEditTheFile().size() > 0) {
                dftModel.addRow(getValueToRow(file, committers));
            //}
        });
        
        return dftModel;
    }

    private String[] getValueToRow(EditedFile editedFile, Set<Committer> committers) {

        String fileName = editedFile.getFileName();
        Integer[] values = new Integer[committers.size()];
        editedFile.getWhoEditTheFile().stream().forEach((cmtrFile) -> {
            int index = 0;
            for (Committer cmter : committers) {
                if (cmtrFile.equals(cmter)) {
                    values[index] = cmtrFile.getCommits();
                    break;
                }
                index++;
            }

        });
        String[] result = getArrayResult(fileName, values);

        return result;
    }

    /**
     * @return the mergeFiles
     */
    public MergeFiles getMergeFiles() {
        return merge;
    }

    /**
     * @param mergeFiles the mergeFiles to set
     */
    public void setMergeFiles(MergeFiles mergeFiles) {
        this.merge = mergeFiles;
    }

    private String[] getArrayResult(String fileName, Integer[] values) {
        String[] result = new String[values.length + 1];
        result[0] = fileName;

        for (int i = 1; i < result.length; i++) {
            result[i] = values[i - 1] == null ? (values[i - 1] = 0).toString() : values[i - 1].toString();
        }
        
        return result;
    }

    private Set<EditedFile> getKeyFiles(Set<EditedFile> filesBothBranches, Map<EditedFile, Set<EditedFile>> depBranchOne, Map<EditedFile, Set<EditedFile>> depBranchTwo) {
        
        depBranchOne.entrySet().stream().forEach((entry) -> {
            EditedFile key = entry.getKey();
            Set<EditedFile> value = entry.getValue();
            filesBothBranches.add(key);
            filesBothBranches.addAll(value);
        });

        depBranchTwo.entrySet().stream().forEach((entry) -> {
            EditedFile key = entry.getKey();
            Set<EditedFile> value = entry.getValue();
            filesBothBranches.add(key);
            filesBothBranches.addAll(value);
        });
        
        return filesBothBranches;
    }

    private List<EditedFile> getEditedFilesList(List<EditedFile> filesb1, Set<EditedFile> keyFiles) {
        List<EditedFile> finalList = new ArrayList<>();
        
        keyFiles.stream().forEach((file) -> {
            if (filesb1.contains(file))
                finalList.add(filesb1.get(filesb1.indexOf(file)));
            else
                finalList.add(new EditedFile(file.getFileName()));
        });
        
        return finalList;
    }

    private Set<Committer> getCommittersList(List<EditedFile> files) {
        Set<Committer> result = new HashSet<>();
        files.stream().forEach((file) -> {
            result.addAll(file.getWhoEditTheFile());
        });
        return result;
    }

}
