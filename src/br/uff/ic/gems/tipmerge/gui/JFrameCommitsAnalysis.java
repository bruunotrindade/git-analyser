/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.gui;

import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.MergeCommits;
import br.uff.ic.gems.tipmerge.model.RepoCommits;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.Export;
import br.uff.ic.gems.tipmerge.util.RunGit;
import br.uff.ic.gems.tipmerge.util.Statistics;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 * This class is in charge of showing all results about the commit analysis
 * @author j2cf, Catarina
 */
public class JFrameCommitsAnalysis extends javax.swing.JFrame {
	
	private final Repository repo;
	private final RepoCommits repoCommitts;
	private MergeCommits mergeCommits;

	/**
	 * Creates new form JFrameCommitsAnalysis
	 * @param repository
	 */
	public JFrameCommitsAnalysis(Repository repository) {
		if(repository.getListOfMerges()== null || repository.getListOfMerges().isEmpty()){
            RepositoryDao rdao = new RepositoryDao(repository.getProject());
            rdao.setDetails(repository);
        }
		this.repoCommitts = new RepoCommits(repository);
		this.repo = repository;
		
		//this.repo = repository;
		initComponents();
		setParameters();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenu1 = new javax.swing.JMenu();
        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        btRun = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        btZScore = new javax.swing.JButton();
        btExport = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        cbBranchOne = new javax.swing.JComboBox();
        hash2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbBranchTwo = new javax.swing.JComboBox();
        hash1 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jFrame1.setBounds(500, 150, 500, 500);
        jFrame1.setResizable(true);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Commits Analysis");
        setResizable(false);

        btRun.setText("Run");
        btRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRunActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(jTable1);

        jTabbedPane1.addTab("Branch1", jScrollPane2);

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
        jScrollPane3.setViewportView(jTable2);

        jTabbedPane1.addTab("Branch2", jScrollPane3);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTable3);

        jTabbedPane1.addTab("Both Branches", jScrollPane4);

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
        jScrollPane5.setViewportView(jTable4);

        jTabbedPane1.addTab("Previous History", jScrollPane5);

        btZScore.setText("(M) Z-score");
        btZScore.setEnabled(false);
        btZScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btZScoreActionPerformed(evt);
            }
        });

        btExport.setText("Export");
        btExport.setEnabled(false);
        btExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExportActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/uff/ic/gems/tipmerge/icons/loading1.gif"))); // NOI18N
        jLabel2.setText("Loading ...");
        jLabel2.setVisible(false);

        jButton1.setText("Chart");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Select Previous Merges");
        jRadioButton2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton2StateChanged(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Select a Merge");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jRadioButton2))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Previous Merges", jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Select Branches to Merge");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Branch one's hash");

        cbBranchOne.setEnabled(false);
        cbBranchOne.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbBranchOneItemStateChanged(evt);
            }
        });
        cbBranchOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBranchOneActionPerformed(evt);
            }
        });

        hash2.setText("<hash>");

        jLabel8.setText("Branch two's hash");

        cbBranchTwo.setEnabled(false);
        cbBranchTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBranchTwoActionPerformed(evt);
            }
        });

        hash1.setText("<hash>");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addGap(0, 303, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hash2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hash1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbBranchOne, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbBranchTwo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(hash1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbBranchOne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hash2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbBranchTwo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Branches to Merge", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btZScore)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExport))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btRun, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jTabbedPane1)
            .addComponent(jTabbedPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRun)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btExport)
                    .addComponent(btZScore)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbBranchOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBranchOneActionPerformed
        // TODO add your handling code here:
        hash1.setText(RunGit.getResult("git log -n 1 --pretty=format:%H " + cbBranchOne.getSelectedItem().toString(), repo.getProject()));
    }//GEN-LAST:event_cbBranchOneActionPerformed

    private void cbBranchOneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBranchOneItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBranchOneItemStateChanged

    private void cbBranchTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBranchTwoActionPerformed
        hash2.setText(RunGit.getResult("git log -n 1 --pretty=format:%H " + cbBranchTwo.getSelectedItem().toString(), repo.getProject()));
    }//GEN-LAST:event_cbBranchTwoActionPerformed

    private void btRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRunActionPerformed
        int i = this.repoCommitts.getMerges().size();
        float j = (float) (i/100.0);
//        jLabel2.setVisible(true);
           Runnable r = () -> {
              jLabel2.setVisible(true);
              btRun.setEnabled(false);
              int count = 0;

		this.setMergeCommits();
		
		//updateTableWithResults(this.getMergeCommits());
		updateTableResBranch1();
		updateTableResBranch2();
		updateTableResBothBranches();
		updateTableResPreviousHistory();
                jLabel2.setVisible(false);
                btRun.setEnabled(true);
           };
           Thread t = new Thread(r);
           t.start();
        

    }//GEN-LAST:event_btRunActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void btExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExportActionPerformed
		Map<String, TableModel> sheet = new LinkedHashMap<>();
		sheet.put("Branch1", jTable1.getModel());
		sheet.put("Branch2", jTable2.getModel());
		sheet.put("Both Branches", jTable3.getModel());
		sheet.put("Previous History", jTable4.getModel());
		Export.toExcel(sheet);	
		JOptionPane.showMessageDialog(this, "File was sucessfully saved", null, JOptionPane.INFORMATION_MESSAGE);
	}
	/*	Map<String, TableModel> sheet = new HashMap<>();
		sheet.put("Commit Analisys", tableResult.getModel());
		Export.toExcel(sheet);
		JOptionPane.showMessageDialog(this, "File was sucessfully saved", null, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btExportActionPerformed
*/
    private void jRadioButton2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton2StateChanged
       invertEnabledFields();
    }//GEN-LAST:event_jRadioButton2StateChanged

	/**
	 * Responsible for generating the zscore modified for commits
	 * @param evt 
	 */
    private void btZScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btZScoreActionPerformed
       
		showZscore((DefaultTableModel)jTable1.getModel());
		showZscore((DefaultTableModel)jTable2.getModel());
		showZscore((DefaultTableModel)jTable3.getModel());
		showZscore((DefaultTableModel)jTable4.getModel());
		
		
    }//GEN-LAST:event_btZScoreActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        newGraphic(); 
    }//GEN-LAST:event_jButton1ActionPerformed

	private void showZscore(DefaultTableModel model1) {
		
		Integer[] values = new Integer[model1.getRowCount()];
		for(int i = 0 ; i < model1.getRowCount(); i++){
			values[i] = ((Integer) model1.getValueAt(i,1));
		}
		List<Double> scores = Statistics.getMZScore(values);
		for(int i = 0 ; i < model1.getRowCount(); i++){
			model1.setValueAt(scores.get(i), i, 1);
			
		}
		
		
		//updateModel(model1,scores,i);
		
		/*
		List<Committer> cmterBranch1 = this.mergeCommits.getCommittersBranchOne();
		cmterBranch1 = Statistics.getMZScoreCommitter(cmterBranch1);
		List<Committer> cmterBranch2 = this.mergeCommits.getCommittersBranchTwo();
		cmterBranch1 = Statistics.getMZScoreCommitter(cmterBranch2);
		List<Committer> cmterBothBranches = this.mergeCommits.getCommittersBothBranches();
		cmterBothBranches = Statistics.getMZScoreCommitter(cmterBothBranches);
		List<Committer> cmterPreviousHistory = this.mergeCommits.getCommittersPreviousHistory();
		cmterPreviousHistory = Statistics.getMZScoreCommitter(cmterPreviousHistory);
		*/
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btExport;
    private javax.swing.JButton btRun;
    private javax.swing.JButton btZScore;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbBranchOne;
    private javax.swing.JComboBox cbBranchTwo;
    private javax.swing.JLabel hash1;
    private javax.swing.JLabel hash2;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables

	private void setParameters() {
	//	txRepoName.setText("Project " +  repo.getName());
		cbBranchOne.setModel(new JComboBox(repo.getBranches().toArray()).getModel());
		cbBranchTwo.setModel(new JComboBox(repo.getBranches().toArray()).getModel());
		jComboBox1.setModel(
			new JComboBox(
					repo.getListOfMerges().toArray()).getModel()
		);
		jPanel1.setBorder(
			BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Project " +  repoCommitts.getRepository().getName())
		);
	}
	private void invertEnabledFields() {

		jComboBox1.setEnabled(!jComboBox1.isEnabled());
		cbBranchOne.setEnabled(!cbBranchOne.isEnabled());
		cbBranchTwo.setEnabled(!cbBranchTwo.isEnabled());
	}

	/*private void updateTableWithResults(MergeCommits merge) {
		DefaultTableModel model1 =  new DefaultTableModel(new Object[]{"Developers", "Branch 1", "Branch 2", "Both Branches", "Previous History"}, 0);
		model1.insertRow(model1.getRowCount(), new Object[] {"Merge Branch: ", merge.getHash(), "", "", ""});

		int cmtb1 = 0, cmtb2 = 0, cmtH = 0, cmtrs = 0;

		for (Conciliator conciliator : merge.getConciliators()){
			cmtrs++;
			cmtb1 += conciliator.getCommitsBranch1();
			cmtb2 += conciliator.getCommitsBranch2();
			cmtH += conciliator.getCommitsPreviousHistory();
			model1.insertRow(model1.getRowCount(),
				new Object[] {conciliator.getCommitter().getName() + ":" + conciliator.getCommitter().getEmail(),
					conciliator.getCommitsBranch1(),
					conciliator.getCommitsBranch2(),
					conciliator.getCommitsBoothBranchs(),
					conciliator.getCommitsPreviousHistory()}
			);
		}
		model1.insertRow(model1.getRowCount(), new Object[] {"Total of " + cmtrs + " authors", cmtb1 + " commits", cmtb2 + " commits", "", cmtH + " commits"});
		tableResult.setModel(model1);
		
		btExport.setEnabled(true);
		btZScore.setEnabled(true);
	}*/
	
	private void updateTableResBranch1(){
		DefaultTableModel model =  new DefaultTableModel(new Object[]{"Developers","Number of Commits on Branch 1"}, 0);
		
		for (Committer committers : this.mergeCommits.getCommittersBranchOne()){
			model.insertRow(model.getRowCount(), new Object[] {committers.getName(), committers.getCommits()});
		}
		jTable1.setModel(model);
	}
	
	private void updateTableResBranch2(){
		DefaultTableModel model =  new DefaultTableModel(new Object[]{"Developers","Number of Commits on Branch 2"}, 0);
		
		for (Committer committers : this.mergeCommits.getCommittersBranchTwo()){
			model.insertRow(model.getRowCount(), new Object[] {committers.getName(), committers.getCommits()});
		}
		jTable2.setModel(model);
	}
	
	private void updateTableResBothBranches(){
		DefaultTableModel model =  new DefaultTableModel(new Object[]{"Developers","Number of Commits on Both Branches"}, 0);

		List<Committer> lista = this.mergeCommits.getCommittersBothBranches();
	
		if (this.mergeCommits.getCommittersBothBranches() != null){
			for (Committer committers : this.mergeCommits.getCommittersBothBranches()){
				model.insertRow(model.getRowCount(), new Object[] {committers.getName(), committers.getCommits()});
			}
		}
		jTable3.setModel(model);
	}
	
	private void updateTableResPreviousHistory(){
		DefaultTableModel model =  new DefaultTableModel(new Object[]{"Developers","Number of Commits in Previous History"}, 0);
		
		for (Committer committers : this.mergeCommits.getCommittersPreviousHistory()){
			model.insertRow(model.getRowCount(), new Object[] {committers.getName(), committers.getCommits()});
		}
		jTable4.setModel(model);
		
		btExport.setEnabled(true);
		btZScore.setEnabled(true);
                jButton1.setEnabled(true);
	}
	

	/**
	 * @return the mergeCommits
	 */
	public MergeCommits getMergeCommits() {
		return mergeCommits;
	}

	/**
	 * @param mergeCommits the mergeCommits to set
	 */
	public void setMergeCommits() {
		MergeCommitsDao mergeDao = new MergeCommitsDao(repo.getProject());
		MergeCommits merge;
		if(jRadioButton1.isSelected()){
			merge = new MergeCommits("", this.repo.getProject());
			merge.setParents(hash1.getText(), hash2.getText());
			merge.setHashBase(mergeDao.getMergeBase(merge.getParents()[0], merge.getParents()[1], merge.getPath()));
		}else{  
                        String hash= codHash(jComboBox1.getSelectedItem().toString());
			merge = new MergeCommits(hash, this.repo.getProject());
			mergeDao.update(merge);
		}
		mergeDao.setCommittersOnBranch(merge);
		mergeDao.setCommittersPreviousHistory(merge);
		this.mergeCommits = merge;
	}

	private void updateModel(DefaultTableModel model, List<Double> scores, int column) {
		for(int j = 0 ; j < scores.size() ; j++ ){
			System.out.println("zscore: " + scores.get(j));
			model.setValueAt(scores.get(j), j+1, column);
		}
	}
        private CategoryDataset createBranch(){
        DefaultCategoryDataset dataBranch1 = new DefaultCategoryDataset();
        List<Committer> temp1 =  this.mergeCommits.getCommittersBranchOne();
        if(jTable1.isShowing()){
          temp1 =  this.mergeCommits.getCommittersBranchOne();
        }
        if(jTable2.isShowing()){
          temp1 =  this.mergeCommits.getCommittersBranchTwo();
        }
        if(jTable3.isShowing()){
          temp1 =  this.mergeCommits.getCommittersBothBranches();
        }
        if(jTable4.isShowing()){
          temp1 =  this.mergeCommits.getCommittersPreviousHistory();
        }
        for (Committer committers : temp1){    
        dataBranch1.addValue(committers.getCommits(),committers.getName(),"Commiters");
        }
        return dataBranch1;
        }
        public void newGraphic(){
            jFrame1.setVisible(true);
            CategoryDataset cdsBranch1 = createBranch();
            String title= "";
            if(jTable1.isShowing())
                title="Branch 1";
            if(jTable2.isShowing())
                title="Branch 2";
            if(jTable3.isShowing())
                title="Both Branches";
            if(jTable4.isShowing())
                title="Previous History";
            JFreeChart graphic = ChartFactory.createBarChart3D(title,"Nomes","Committs",cdsBranch1,PlotOrientation.VERTICAL,true,true,true);
            ChartPanel chartPanel = new ChartPanel(graphic);
               chartPanel.setPreferredSize(new java.awt.Dimension(590, 350));
               jFrame1.setContentPane(chartPanel);
               jFrame1.pack();
               RefineryUtilities.centerFrameOnScreen(jFrame1);
               jFrame1.setVisible(true);
        }
         private String codHash(String hash){
        String temp = hash;
        hash = "";
        String temp2;
                        boolean valid = true;
                        for(int i=0; i< temp.length();i++){
                            temp2 = String.valueOf(temp.charAt(i));
                            if(temp2.equals(" ")) {
                                valid = false;
                            } else {
                                 if(valid == true)
                                     hash = hash + temp2;
                             }
                        }
                    return hash;    
        }
}