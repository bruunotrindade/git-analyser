package br.uff.ic.gems.tipmerge;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.model.Repository;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;

public class Frame extends JFrame {

	private JPanel contentPane;
	private JTextField tfProjeto;
	private JTable table;
	private JTextField tfSaida;
	private JFileChooser fcProjeto, fcSaida;
	private Repository repositorio;
	private ArrayList<Atributo> atributos;
	
		/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(799, 440);
		setLocationRelativeTo(null);
		setTitle("Análise de Projeto");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		fcProjeto = new JFileChooser();
        fcProjeto.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        fcSaida = new JFileChooser();
        fcSaida.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{789, 0};
		gbl_panel.rowHeights = new int[] {70, 136, 50, 70, 35, 40, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel pnProjeto = new JPanel();
		GridBagConstraints gbc_pnProjeto = new GridBagConstraints();
		gbc_pnProjeto.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnProjeto.insets = new Insets(0, 0, 5, 0);
		gbc_pnProjeto.gridx = 0;
		gbc_pnProjeto.gridy = 0;
		panel.add(pnProjeto, gbc_pnProjeto);
		pnProjeto.setLayout(new GridLayout(0, 1, 5, 0));
		
		JLabel lblNewLabel = new JLabel("Projeto:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		pnProjeto.add(lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		pnProjeto.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		tfProjeto = new JTextField();
		tfProjeto.setText("Selecione um projeto");
		tfProjeto.setEnabled(false);
		tfProjeto.setDisabledTextColor(Color.BLACK);
		panel_2.add(tfProjeto, BorderLayout.CENTER);
		tfProjeto.setColumns(10);
		
		JButton btnBuscarProjeto = new JButton("Buscar");
		btnBuscarProjeto.setFocusPainted(false);
		btnBuscarProjeto.setBackground(Color.WHITE);
		
		panel_2.add(btnBuscarProjeto, BorderLayout.EAST);
		
		JPanel pnAtributos = new JPanel();
		GridBagConstraints gbc_pnAtributos = new GridBagConstraints();
		gbc_pnAtributos.fill = GridBagConstraints.BOTH;
		gbc_pnAtributos.insets = new Insets(0, 0, 5, 0);
		gbc_pnAtributos.gridx = 0;
		gbc_pnAtributos.gridy = 1;
		panel.add(pnAtributos, gbc_pnAtributos);
		pnAtributos.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAtributos = new JLabel("Atributos:");
		lblAtributos.setFont(new Font("Dialog", Font.BOLD, 16));
		pnAtributos.add(lblAtributos, BorderLayout.NORTH);
		
		JPanel pnGAtributos = new JPanel();
		pnAtributos.add(pnGAtributos);
		GridBagLayout gbl_pnGAtributos = new GridBagLayout();
		gbl_pnGAtributos.columnWidths = new int[] {170, 185, 150, 185, 0};
		gbl_pnGAtributos.rowHeights = new int[]{23, 23, 23, 23, 0};
		gbl_pnGAtributos.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnGAtributos.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnGAtributos.setLayout(gbl_pnGAtributos);
		
		String atributosNome[] = {"Hash do merge", "Data do merge", "Tempo de Isolamento 1", "Tempo de Isolamento 2", "Tempo de Isolamento MAX", "Desenvolvedores 1", 
				"Desenvolvedores 2", "Intersecção de Desenvolvedores", "Commits 1", "Commits 2", "Arquivos Alterados 1", "Arquivos Alterados 2", 
				"Intersecção de Arquivos Alterados", "Conflito", "Arquivos em Conflito", "Chunks em Conflito"};
		
		atributos = new ArrayList<Atributo>();
		
		int x = 0, y = 0;
		for(int i = 0; i < atributosNome.length; i++)
		{
			Atributo at = new Atributo(i, new JCheckBox(atributosNome[i]));
			at.checkbox.setFont(new Font("Dialog", Font.PLAIN, 12));
			at.checkbox.setFocusPainted(false);
			at.checkbox.setEnabled(false);
			atributos.add(at);
			
			GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
			gbc_chckbxNewCheckBox.fill = GridBagConstraints.BOTH;
			gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxNewCheckBox.gridx = x;
			gbc_chckbxNewCheckBox.gridy = y++;
			pnGAtributos.add(at.checkbox, gbc_chckbxNewCheckBox);
			
			if(y == 4)
			{
				y = 0;
				x += 1;
			}
			
			at.checkbox.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					ArrayList<String> colunas = new ArrayList<String>();
					for(Atributo at : atributos)
						if(at.checkbox.isSelected())
							colunas.add(at.checkbox.getText());
					
					if(colunas.size() == 0)
						colunas.add(" ");
					
					table.setModel(new TbModel(colunas));
				}
			});
		}
		
		JPanel pnOrdem = new JPanel();
		GridBagConstraints gbc_pnOrdem = new GridBagConstraints();
		gbc_pnOrdem.fill = GridBagConstraints.BOTH;
		gbc_pnOrdem.insets = new Insets(0, 0, 5, 0);
		gbc_pnOrdem.gridx = 0;
		gbc_pnOrdem.gridy = 2;
		panel.add(pnOrdem, gbc_pnOrdem);
		pnOrdem.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOrdemDosAtributos = new JLabel("Ordem dos Atributos:");
		lblOrdemDosAtributos.setFont(new Font("Dialog", Font.BOLD, 16));
		pnOrdem.add(lblOrdemDosAtributos, BorderLayout.NORTH);
		
		table = new JTable();
		table.setEnabled(false);
		ArrayList<String> colunas = new ArrayList<String>();
		colunas.add(" ");
		table.setModel(new TbModel(colunas));
		table.getTableHeader().setReorderingAllowed(false);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tablePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		tablePanel.add(table, BorderLayout.CENTER);
		tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
		
		pnOrdem.add(tablePanel, BorderLayout.SOUTH);
		
		JPanel pnSaida = new JPanel();
		GridBagConstraints gbc_pnSaida = new GridBagConstraints();
		gbc_pnSaida.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnSaida.insets = new Insets(0, 0, 5, 0);
		gbc_pnSaida.gridx = 0;
		gbc_pnSaida.gridy = 3;
		panel.add(pnSaida, gbc_pnSaida);
		pnSaida.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDiretrioDeSada = new JLabel("Diretório de Saída:");
		lblDiretrioDeSada.setFont(new Font("Dialog", Font.BOLD, 16));
		pnSaida.add(lblDiretrioDeSada, BorderLayout.NORTH);
		
		JPanel panel_3 = new JPanel();
		pnSaida.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		tfSaida = new JTextField();
		tfSaida.setText("Selecione um diretório");
		tfSaida.setEnabled(false);
		tfSaida.setDisabledTextColor(Color.BLACK);
		panel_3.add(tfSaida, BorderLayout.CENTER);
		tfSaida.setColumns(10);
		
		JButton btnBuscarSaida = new JButton("Buscar");
		btnBuscarSaida.setEnabled(false);
		btnBuscarSaida.setBackground(Color.WHITE);
		btnBuscarSaida.setFocusPainted(false);
		btnBuscarSaida.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int resp = fcSaida.showDialog(null, "Selecionar");
				if(resp == JFileChooser.APPROVE_OPTION)
				{
					File saida = fcProjeto.getSelectedFile();
					tfProjeto.setText(saida.getAbsolutePath());
				}
			}
		});
		panel_3.add(btnBuscarSaida, BorderLayout.EAST);
		
		JPanel pnBarra = new JPanel();
		GridBagConstraints gbc_pnBarra = new GridBagConstraints();
		gbc_pnBarra.fill = GridBagConstraints.BOTH;
		gbc_pnBarra.insets = new Insets(0, 0, 5, 0);
		gbc_pnBarra.gridx = 0;
		gbc_pnBarra.gridy = 4;
		panel.add(pnBarra, gbc_pnBarra);
		pnBarra.setLayout(new BorderLayout(0, 0));
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		pnBarra.add(progressBar);
		
		JPanel pnBotoes = new JPanel();
		GridBagConstraints gbc_pnBotoes = new GridBagConstraints();
		gbc_pnBotoes.fill = GridBagConstraints.BOTH;
		gbc_pnBotoes.gridx = 0;
		gbc_pnBotoes.gridy = 5;
		panel.add(pnBotoes, gbc_pnBotoes);
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setFocusPainted(false);
		btnIniciar.setForeground(new Color(255, 255, 255));
		btnIniciar.setBackground(new Color(60, 179, 113));
		btnIniciar.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				ArrayList<String> colunas = new ArrayList<String>();
				for(int i = 0; i < table.getColumnModel().getColumnCount(); i++)
					colunas.add((String) table.getColumnModel().getColumn(i).getHeaderValue());
				
				if(colunas.size() == 0)
					JOptionPane.showMessageDialog(null, "É necessário que pelo menos um atributo seja selecionado!", "Falha", JOptionPane.ERROR_MESSAGE);
				else if(fcSaida.getSelectedFile() == null)
					JOptionPane.showMessageDialog(null, "Selecione um diretório de saída!", "Falha", JOptionPane.ERROR_MESSAGE);
				else
				{
					MergeAnalyser ma = new MergeAnalyser(repositorio);
					ma.runAnalysis(atributos, fcSaida.getSelectedFile());
				}
			}
		});
		pnBotoes.add(btnIniciar);
		
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.setForeground(new Color(255, 255, 255));
		btnLimpar.setBackground(new Color(232, 91, 84));
		btnLimpar.setFocusPainted(false);
		pnBotoes.add(btnLimpar);
		
		btnBuscarProjeto.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int resp = -1;
				while(resp == -1)
				{
					try
					{
						resp = fcProjeto.showDialog(null, "Selecionar");
						if(resp == JFileChooser.APPROVE_OPTION)
						{
							File repos = fcProjeto.getSelectedFile();
							repositorio = new RepositoryDao(repos).getRepository();
							tfProjeto.setText(repos.getAbsolutePath());
							System.out.println(repos.getAbsolutePath());
							
							for(Atributo at : atributos)
								at.checkbox.setEnabled(true);
							
							btnBuscarSaida.setEnabled(true);
							table.getTableHeader().setReorderingAllowed(true);
						}
						else
							break;
					}
					catch(NullPointerException npe)
					{
						JOptionPane.showMessageDialog(null, "O diretório selecionado não é um repositório!", "Falha", JOptionPane.ERROR_MESSAGE);
						resp = -1;
					}
				}
			}
		});
		
		btnBuscarSaida.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int resp = fcSaida.showDialog(null, "Selecionar");
				if(resp == JFileChooser.APPROVE_OPTION)
				{
					File saida = fcSaida.getSelectedFile();
					tfSaida.setText(saida.getAbsolutePath());
				}
			}
		});
	}

}
