package com.example.CandidatosTSE;

import com.example.CandidatosTSE.application.CandidatosTseApplication;
import com.example.CandidatosTSE.model.Candidato;
import com.example.CandidatosTSE.service.CandidatosTseService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CandidatosTseApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConsultaTest {

    @LocalServerPort
    private int porta;

    @Autowired
    private CandidatosTseService service;

    private final HttpClient http = HttpClient.newHttpClient();

    @Test
    void paginaInicialMostraTodosOsCandidatosEOpcoes() throws Exception {
        Document pagina = consultar("/");
        assertThat(service.listarTodos()).isNotEmpty();
        assertThat(pagina.select(".card")).hasSize(service.listarTodos().size());
        assertThat(pagina.selectFirst("#total").text())
                .isEqualTo(String.valueOf(service.listarTodos().size()));
        assertThat(pagina.select("#cargo option")).hasSize(service.listarCargos().size() + 1);
        assertThat(pagina.select("#partido option")).hasSize(service.listarPartidos().size() + 1);
        assertThat(pagina.selectFirst("#texto").val()).isEmpty();
        assertThat(pagina.selectFirst("html").attr("lang")).isEqualTo("pt-BR");
    }

    @Test
    void filtrosIndividuaisFuncionam() throws Exception {
        Candidato candidato = service.listarTodos().getFirst();
        String[][] filtros = {
                {"cargo", candidato.getCargo()},
                {"partido", candidato.getSiglaPartido()},
                {"texto", candidato.getNomeUrna()},
                {"texto", candidato.getNrCandidato()}
        };
        for (String[] filtro : filtros) {
            List<Candidato> esperado = service.filtrar(
                    filtro[0].equals("cargo") ? filtro[1] : null,
                    filtro[0].equals("partido") ? filtro[1] : null,
                    filtro[0].equals("texto") ? filtro[1] : null);
            Document pagina = consultar("/?" + filtro[0] + "=" + codificar(filtro[1]));
            assertThat(pagina.select(".card")).hasSize(esperado.size());
            String seletor = "#" + filtro[0] + (filtro[0].equals("texto") ? "" : " option[selected]");
            assertThat(pagina.selectFirst(seletor).val()).isEqualTo(filtro[1]);
        }
    }

    @Test
    void combinaFiltrosEMantemValoresSelecionados() throws Exception {
        Candidato candidato = service.listarTodos().getFirst();
        String cargo = candidato.getCargo();
        String partido = candidato.getSiglaPartido();
        String texto = candidato.getNomeUrna();
        Document pagina = consultar("/?cargo=" + codificar(cargo)
                + "&partido=" + codificar(partido) + "&texto=" + codificar(texto));
        assertThat(pagina.select(".card")).hasSize(service.filtrar(cargo, partido, texto).size());
        assertThat(pagina.selectFirst("#cargo option[selected]").val()).isEqualTo(cargo);
        assertThat(pagina.selectFirst("#partido option[selected]").val()).isEqualTo(partido);
        assertThat(pagina.selectFirst("#texto").val()).isEqualTo(texto);
        assertThat(pagina.selectFirst(".limpar").attr("href")).isEqualTo("/");
    }

    @Test
    void pesquisaInexistenteMostraEstadoVazio() throws Exception {
        Document pagina = consultar("/?texto=zzzz-sem-candidato-987654321");
        assertThat(pagina.select(".card")).isEmpty();
        assertThat(pagina.selectFirst("#total").text()).isEqualTo("0");
        assertThat(pagina.selectFirst(".vazio").text()).contains("Nenhum candidato");
    }

    @Test
    void valoresVaziosEquivalemAPaginaInicial() throws Exception {
        Document pagina = consultar("/?cargo=&partido=&texto=");
        assertThat(pagina.select(".card")).hasSize(service.listarTodos().size());
    }

    @Test
    void textoDigitadoEExibidoSemExecutarHtml() throws Exception {
        String texto = "<script>alert(1)</script>";
        Document pagina = consultar("/?texto=" + codificar(texto));
        assertThat(pagina.selectFirst("#texto").val()).isEqualTo(texto);
        assertThat(pagina.select("script")).isEmpty();
    }

    @Test
    void cssEEstadoSemFotoFuncionam() throws Exception {
        assertThat(obter("/css/style.css").statusCode()).isEqualTo(200);
        Document pagina = consultar("/");
        assertThat(pagina.select(".foto-candidato")).isEmpty();
        assertThat(pagina.select(".sem-foto")).hasSize(service.listarTodos().size());
    }

    private Document consultar(String caminho) throws Exception {
        var resposta = obter(caminho);
        assertThat(resposta.statusCode()).isEqualTo(200);
        assertThat(resposta.headers().firstValue("content-type").orElse("")).contains("text/html");
        return Jsoup.parse(resposta.body());
    }

    private HttpResponse<String> obter(String caminho) throws Exception {
        var pedido = HttpRequest.newBuilder(URI.create("http://localhost:" + porta + caminho))
                .GET().build();
        return http.send(pedido, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private String codificar(String texto) {
        return URLEncoder.encode(texto, StandardCharsets.UTF_8);
    }
}
