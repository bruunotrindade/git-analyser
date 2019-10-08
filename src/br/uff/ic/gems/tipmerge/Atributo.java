package br.uff.ic.gems.tipmerge;

import javax.swing.JCheckBox;

public class Atributo
{
	int codigo;
	JCheckBox checkbox;
	
	public Atributo(int codigo, JCheckBox checkbox)
	{
		this.codigo = codigo;
		this.checkbox = checkbox;
	}
}