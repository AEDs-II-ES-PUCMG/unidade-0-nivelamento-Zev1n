import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProdutoPerecivel extends Produto {

	private LocalDate dataValidade;

	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataValidade) {
		super(desc, precoCusto, margemLucro);

		if (dataValidade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Produto com data de validade vencida.");
		}

		this.dataValidade = dataValidade;
	}

	@Override
	public double valorDeVenda() {
		double valor = super.valorDeVenda();

		if (!dataValidade.isAfter(LocalDate.now().plusDays(3))) {
			return valor * 0.75;
		}

		return valor;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return super.toString() + " (Validade: " + dataValidade.format(formatter) + ")";
	}

	@Override
	public String gerarDadosTexto() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return String.format(Locale.US, "2;%s;%.2f;%.2f;%s",
				getDescricao(),
				getPrecoCusto(),
				getMargemLucro(),
				dataValidade.format(formatter));
	}
}