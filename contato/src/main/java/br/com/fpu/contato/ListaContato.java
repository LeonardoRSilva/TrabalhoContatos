package br.com.fpu.contato;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/listaContato")
public class ListaContato extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String novoContato = req.getParameter("novoContato");
		String novoTelefone = req.getParameter("novoTelefone");

		String nome = req.getParameter("contato");
		String status = req.getParameter("Status");
		List<String> readAllLines = new ArrayList();

		if (novoContato != null && novoTelefone != null) {

			HttpSession sessao = req.getSession();
			String user = null;
			if (sessao.getAttribute("user") == null) {
				resp.sendRedirect("index.html");
			} else
				user = (String) sessao.getAttribute("user");
			@SuppressWarnings("deprecation")
			String nomeUser = (String) sessao.getValue("user");

			Contato contato = new Contato();
			contato.setUser(nomeUser);
			contato.setContato(novoContato);
			contato.setTelefone(novoTelefone);

			addContato(contato);

			readAllLines = null;
			readFile(req, resp, leitura(resp, readAllLines));

			
		} else if (nome != null && status != null) {

			readAllLines = leitura(resp, readAllLines);
			String linhaRemove = new String();

			for (String linha : readAllLines) {
				
				String[] tic = linha.split(";");
				if (!linha.isEmpty()) {
					if (nome.equalsIgnoreCase(tic[1])) {
						linhaRemove = linha;

					}
				}

			}
			readAllLines.remove(linhaRemove);

			escrita(readAllLines);
			PrintWriter writer = resp.getWriter();
			readAllLines = null;
			readFile(req, resp, leitura(resp, readAllLines));

			
		} else {
			readAllLines = null;
			readFile(req, resp, leitura(resp, readAllLines));
		}

	}

	public static void addContato(Contato contato) throws IOException {
		Path path = Paths.get("c:/arquivojava/teste/Contatos.txt");
		String conteudo = contato.getUser() + ";" + contato.getContato() + ";" + contato.getTelefone()
				+ System.getProperty("line.separator");
		OpenOption modoAbertura = Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE;
		Files.write(path, conteudo.getBytes(), modoAbertura);

	}

	public static void escrita(List<String> readAllLines) throws IOException {
		Path path = Paths.get("c:/arquivojava/teste/Contatos.txt");
		// String conteudo = "Escrevendo\n em arquivo\n";
		// OpenOption modoAbertura = Files.exists(path) ?
		// StandardOpenOption.APPEND : StandardOpenOption.CREATE;

		OpenOption modoAbertura = StandardOpenOption.CREATE;

		Files.delete(path);
		Files.write(path, readAllLines, Charset.defaultCharset(), modoAbertura);

	}

	public List<String> leitura(HttpServletResponse resp, List<String> readAllLines) throws IOException {
		Path path = null;

		try {
			path = Paths.get("c:/arquivojava/teste/Contatos.txt");

			readAllLines = Files.readAllLines(path, Charset.defaultCharset());

		} catch (NoSuchFileException x) {
			System.err.format("%s: no such" + " file or directory%n", path);

			// Logic for case when file doesn't exist.
		} catch (IOException x) {
			System.err.format("%s%n", x);

			// Logic for other sort of file error.
		}

		return readAllLines;
	}

	private boolean readFile(HttpServletRequest req, HttpServletResponse resp, List<String> readAllLines)
			throws IOException, FileNotFoundException {

		List<Contato> contatos = new ArrayList();
		readAllLines = leitura(resp, readAllLines);

		if (readAllLines != null && !readAllLines.isEmpty()) {

			for (String linha : readAllLines) {

				String[] tic = linha.split(";");

				// resp.sendRedirect("");
				// System.out.println(tic[0]);

				HttpSession sessao = req.getSession();
				String user = null;

				if (sessao.getAttribute("user") == null) {
					// sessao.removeAttribute("user");
					resp.sendRedirect("index.html");
				} else {
					user = (String) sessao.getAttribute("user");
				}

				if (user.equalsIgnoreCase(tic[0])) {
					Contato contato = new Contato();
					contato.setUser(tic[0]);
					contato.setContato(tic[1]);
					contato.setTelefone(tic[2]);

					contatos.add(contato);

				}

			}

			// if(!contatos.isEmpty() && contatos != null){
			paginaListaContato(resp, contatos);
			return true;
			// }
			// else{
			// return false;
			// }

		}

		paginaListaContato(resp, contatos);
		return false;

	}

	private void paginaListaContato(HttpServletResponse resp, List<Contato> contatos) throws IOException {
		PrintWriter writer = resp.getWriter();

		StringBuilder text = new StringBuilder();
		// DecimalFormat fmt = new DecimalFormat("#,##0.00");

		text.append("<html>");
		text.append("<head>");
		text.append("<meta charset=\"utf-8\">");
		text.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
		text.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		text.append("<title>Aprendendo Servlet</title>");
		text.append("<link rel=\"stylesheet\" href=\"resources/bootstrap-3.3.5-dist/css/bootstrap.min.css\">");
		text.append("<link rel=\"stylesheet\" href=\"resources/bootstrap-3.3.5-dist/css/bootstrap-theme.min.css\">");

		text.append("<style type=\"text/css\">");
		text.append(".display_block{");
		text.append("display:block;");
		text.append("-webkit-transition:0.6s ease-in;");
		text.append("-moz-transition:0.6s ease-in;");
		text.append("-o-transition:0.6s ease-in;");
		text.append("transition:0.6s ease-in;");
		text.append("}");
		text.append("</style>");

		text.append("</head>");
		text.append("<body>");

		text.append("<div class=\"container\">");
		text.append("<div class=\"panel panel-default\">");
		text.append("<div class=\"panel-heading\">");
		text.append("<h3 class=\"panel-title\">Trabalho Contato</h3><br>");
		text.append("<a href=\"index.html\" class=\"btn btn-default\">Home</a>");

		text.append("</div>");
		text.append("<div class=\"panel-body\">");
		text.append("");
		text.append("<div class=\"bs-example\" data-example-id=\"panel-without-body-with-table\">");
		text.append("<div class=\"panel panel-default\">");
		if (!contatos.isEmpty() && contatos != null) {
			text.append("<div class=\"panel-heading\">Contatos do usuario " + contatos.get(0).getUser() + " </div>");

			text.append(
					"<a class=\"btn btn-primary\"  style =\"margin-left: 5px;\" onclick=\"chama_modal();\">Novo</a>");
			text.append("<table class=\"table\">");
			text.append("<thead>");
			text.append("<tr>");
			text.append("<th>#</th>");
			text.append("<th>Contato</th>");
			text.append("<th>Telefone</th>");
			text.append("<th>A&ccedil;&otilde;es</th>");
			text.append("</tr>");
			text.append("</thead>");
			text.append("<tbody>");
			int i = 1;

			for (Contato contato : contatos) {
				text.append("<tr>");
				text.append("<th scope=\"row\">" + i + "</th>");
				text.append("<td>");
				text.append(contato.getContato());
				text.append("</td>");
				text.append("<td>");
				text.append(contato.getTelefone());
				text.append("</td>");
				text.append("<td>");

				text.append("<form  method=\"post\" action=\"listaContato\">");
				text.append("<input type=\"hidden\" name=\"contato\" value=\"" + contato.getContato() + "\">");
				text.append("<input type=\"hidden\" name=\"Status\" value=\"excluir\">");
				text.append("<input class=\"btn btn-danger\" type=\"submit\" value=\"Excluir\" >");
				text.append("</form>");

				text.append("</td>");
				text.append("</tr>");

				i++;
			}

		} else {
			text.append("<div class=\"panel-heading\">Sem contatos para listar  </div>");
			text.append(
					"<a class=\"btn btn-primary\"  style =\"margin-left: 5px;\" onclick=\"chama_modal();\">Novo</a>");
			text.append("<table class=\"table\">");
			text.append("<thead>");
			text.append("<tr>");
			text.append("<th>#</th>");
			text.append("<th>Contato</th>");
			text.append("<th>Telefone</th>");
			text.append("<th>A&ccedil;&otilde;es</th>");
			text.append("</tr>");
			text.append("</thead>");
			text.append("<tbody>");
		}

		text.append("</tbody>");
		text.append("</table>");
		text.append("</div>");
		text.append("</div>");
		text.append("</div>");

		text.append("<div id=\"edit-note-modal\" class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" >");
		text.append("<div class=\"modal-dialog\">");
		text.append("<div class=\"modal-content\">");
		text.append("<div class=\"modal-header\">");
		text.append("<h4 class=\"modal-title\">Add Contato</h4>");
		text.append("<form class=\"form-horizontal\" method=\"post\" action=\"listaContato\">");

		text.append("<div class=\"form-group\">");
		text.append("<label for=\"inputContato\" class=\"col-sm-2 control-label\" >Contato</label>");
		text.append("<div class=\"col-sm-7\">");
		text.append(
				"<input type=\"text\" class=\"form-control\" name=\"novoContato\" id=\"inputContato\" placeholder=\"Contato\" required=\"required\" size=\"8\" maxlength=\"8\">");
		text.append("</div>");
		text.append("</div>");
		text.append("<div class=\"form-group\">");
		text.append("<label for=\"inputTelefone\" class=\"col-sm-2 control-label\" >Telefone</label>");
		text.append("<div class=\"col-sm-6\">");
		text.append(
				"<input type=\"telefone\" class=\"form-control\" name=\"novoTelefone\" id=\"inputTelefone\" placeholder=\"Telefone\" required=\"required\" size=\"8\" maxlength=\"8\">");
		text.append("</div>");
		text.append("</div>");
		text.append("<div class=\"form-group\">");
		text.append("<div class=\"col-sm-offset-1 col-sm-10\">");
		text.append("<button type=\"submit\" class=\"btn btn-default\">Enviar</button>");
		text.append("<a class=\"btn btn-primary\" id=\"btn-fechar\" >fechar</button>");

		text.append("</div>");
		text.append("</div>");
		text.append("</form>");

		text.append("</div>");
		text.append("<div class=\"modal-body\">");
		text.append("</div>");
		text.append("</div>");
		text.append("</div>");
		text.append("</div>");

		text.append("<script type=\"text/javascript\">");
		text.append("function chama_modal()");
		text.append("{   ");

		text.append("document.getElementById(\"edit-note-modal\").className = \"modal  display_block\";");

		text.append("document.getElementById(\"btn-fechar\").onclick = function()");
		text.append("{");
		text.append("document.getElementById(\"edit-note-modal\").className = \"modal fade\";");

		text.append("}");
		text.append("   ; ");
		text.append("}");
		text.append("</script>");

		text.append("</body>");
		text.append("</html>");

		System.out.println(text.toString());
		// writer.print(String.format("Resultado da soma %s", resultado));
		writer.print(String.format(text.toString()));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> readAllLines = new ArrayList();
		readFile(req, resp, leitura(resp, readAllLines));
	}

}
