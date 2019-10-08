package br.uff.ic.gems.tipmerge;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class TbModel extends DefaultTableModel 
{
	ArrayList<String> colunas;
	
	public TbModel(ArrayList<String> colunas)
	{
		this.colunas = colunas;
	}
	
	public int getColumnCount() 
	{
		return colunas.size();
	}
	
	public String getColumnName(int index) 
	{
		return colunas.get(index);
	}
}
