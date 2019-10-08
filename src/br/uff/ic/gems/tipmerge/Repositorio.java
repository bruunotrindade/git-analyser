package br.uff.ic.gems.tipmerge;

public class Repositorio 
{
	public String nome, linguagem;
	public int conflitos, merges, analisados;
	
	public Repositorio(String nome, String linguagem, int conflitos, int merges, int analisados)
	{
		this.nome = nome;
		this.linguagem = linguagem;
		this.conflitos = conflitos;
		this.merges = merges;
		this.analisados = analisados;
	}
}
