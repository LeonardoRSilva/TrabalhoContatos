package br.com.fpu.contato;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User usuario = new User();
		readFile(req, resp, leitura(resp), usuario);

	}

	public List<String> leitura(HttpServletResponse resp) throws IOException {
		Path path = null;
		List<String> readAllLines = new ArrayList();

		try {
			path = Paths.get("c:/arquivojava/teste/Users.txt");

			readAllLines = Files.readAllLines(path, Charset.defaultCharset());
			for (String string : readAllLines) {
				System.out.println(string.toString());
			}
			

		} catch (NoSuchFileException x) {
			System.err.format("%s: no such" + " file or directory%n", path);
			
			PrintWriter writer = resp.getWriter();
			writer.write("Erro, Arquivo não encontrado");
			
			// Logic for case when file doesn't exist.
		} catch (IOException x) {
			System.err.format("%s%n", x);
			PrintWriter writer = resp.getWriter();
			writer.write("Erro, Arquivo não encontrado");
			
			// Logic for other sort of file error.
		}

		return readAllLines;
	}

	private boolean readFile(HttpServletRequest req, HttpServletResponse resp, List<String> readAllLines, User usuario)
			throws IOException, FileNotFoundException {

		if (!readAllLines.isEmpty() && readAllLines != null) {

			for (String linha : readAllLines) {

				String[] tic = linha.split(";");
				// resp.sendRedirect("");
				// System.out.println(tic[0]);
				String nome = req.getParameter("nome");
				String password = req.getParameter("password");

				if (nome.equalsIgnoreCase(tic[0]) && password.equalsIgnoreCase(tic[1])) {
					usuario.setNome(tic[0]);
					usuario.setPassword(tic[1]);

					HttpSession sessao = req.getSession();

					sessao.setAttribute("user", nome);
					resp.sendRedirect("listaContato");

					// Object obj = sessao.getAttribute(nome);

					
				} 
					// PrintWriter writer = resp.getWriter();
					// writer.write("Erro, Login ou usuario invalido");

				
				

			}

			paginaErrorLogin(resp);
			return false;

		} else {
			
			return false;
		}

		
		
	}

	private void paginaErrorLogin(HttpServletResponse resp) throws IOException {
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
		text.append("</head>");
		text.append("<body>");
		text.append("<div class=\"container\">");
		text.append("<div class=\"panel panel-default\">");
		text.append("<div class=\"panel-heading\">");
		text.append("<h3 class=\"panel-title\">Trabalho Contato</h3><br>");
		text.append("<a href=\"index.html\" class=\"btn btn-default\">Home</a>");

		text.append("</div>");
		text.append("<div class=\"panel-body\">");
		text.append("<form class=\"form-horizontal\" method=\"post\" action=\"login\">");
		text.append("<p class=\"bg-danger\" style =\"padding: 15px;\">Usuario ou senha inv&aacute;lidos</p>");
		text.append("<div class=\"form-group\">");
		text.append("<label for=\"inputNome\" class=\"col-sm-1 control-label\" >Nome</label>");
		text.append("<div class=\"col-sm-10\">");
		text.append(
				"<input type=\"text\" class=\"form-control\" name=\"nome\" id=\"inputNome\" placeholder=\"Nome\" required=\"required\" size=\"8\" maxlength=\"8\">");
		text.append("</div>");
		text.append("</div>");
		text.append("<div class=\"form-group\">");
		text.append("<label for=\"inputPassword\" class=\"col-sm-1 control-label\" >Password</label>");
		text.append("<div class=\"col-sm-10\">");
		text.append(
				"<input type=\"password\" class=\"form-control\" name=\"password\" id=\"inputPassword\" placeholder=\"Password\" required=\"required\" size=\"8\" maxlength=\"8\">");
		text.append("</div>");
		text.append("</div>");
		text.append("<div class=\"form-group\">");
		text.append("<div class=\"col-sm-offset-1 col-sm-10\">");
		text.append("<button type=\"submit\" class=\"btn btn-default\">Enviar</button>");
		text.append("</div>");
		text.append("</div>");
		text.append("</form>");
		text.append("</div>");
		text.append("</div>");
		text.append("</div>");
		text.append("</body>");
		text.append("</html>");

		System.out.println(text.toString());
		// writer.print(String.format("Resultado da soma %s", resultado));
		writer.print(String.format(text.toString()));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer.write("suporte apenas para metodo post");
	}

}
