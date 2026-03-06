import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	private String descricao;
	private double precoCusto;
	private double margemLucro;
	
	/**
     * Inicializador privado.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	/**
     * Construtor sem margem de lucro.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
	/**
     * Retorna o valor de venda do produto.
     * @return Valor de venda do produto
     */
	public double valorDeVenda() {
		return precoCusto * (1.0 + margemLucro);
	}
	
	/**
     * Descrição em string do produto.
     * @return String com nome e valor de venda
     */
	@Override
	public String toString() {
		NumberFormat moeda = NumberFormat.getCurrencyInstance();
		return descricao + ": " + moeda.format(valorDeVenda());
	}
	
	/**
     * Igualdade de produtos: caso possuam o mesmo nome/descrição.
     * @param obj Outro produto a ser comparado
     * @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
     */
	@Override
	public boolean equals(Object obj) {
		Produto outro = (Produto) obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}
	
	/**
     * Gera uma linha de texto a partir dos dados do produto
     * @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
     */
	public abstract String gerarDadosTexto();
	
	/**
     * Cria um produto a partir de uma linha de dados em formato texto.
     * @param linha Linha com os dados do produto a ser criado.
     * @return Um produto com os dados recebidos
     */
	public static Produto criarDoTexto(String linha) {
		Produto novoProduto = null;
		
		String[] partes = linha.split(";");
		int tipo = Integer.parseInt(partes[0]);
		String descricao = partes[1];
		double precoCusto = Double.parseDouble(partes[2]);
		double margemLucro = Double.parseDouble(partes[3]);
		
		if (tipo == 1) {
			novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
		} else if (tipo == 2) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dataValidade = LocalDate.parse(partes[4], formatter);
			
			if (dataValidade.isBefore(LocalDate.now())) {
				dataValidade = LocalDate.now().plusDays(1);
			}
			
			novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade);
		}
		
		return novoProduto;
	}
	
	protected String getDescricao() {
		return descricao;
	}
	
	protected double getPrecoCusto() {
		return precoCusto;
	}
	
	protected double getMargemLucro() {
		return margemLucro;
	}
}