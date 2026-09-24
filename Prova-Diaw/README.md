# Candidatos TSE - MA

Filtros laterais e fichas com os dados organizados em duas colunas.

Consulta de candidaturas de Minas Gerais com a base fornecida no material da AV1.
Este pacote nao inclui fotografias e apresenta a indicacao "Sem foto".

## Executar

Requisitos: JDK 25 e Apache Maven. Na primeira execucao, o Maven precisa de internet para baixar as dependencias.

Extraia o ZIP, abra um terminal na pasta que contem o arquivo `pom.xml` e execute:

```sh
mvn spring-boot:run
```

Acesse http://localhost:8080/. Para encerrar, pressione Ctrl+C no terminal.
Execute apenas um dos projetos por vez, pois os tres usam a porta 8080.

## Verificar e empacotar

```sh
mvn clean verify
java -jar target/candidatos-tse-1.0.0.jar
```

Os testes verificam a pagina inicial, os filtros individuais e combinados,
a preservacao dos campos, a lista vazia, a saida de texto e os recursos estaticos.

## Arquivos principais

- `src/main/java/com/example/CandidatosTSE/controller/CandidatosTseController.java`
- `src/main/resources/templates/index.html`
- `src/main/resources/static/css/style.css`

O modelo e o servico foram mantidos conforme o material fornecido.
A classe principal declara a varredura de todos os pacotes da aplicacao.
O CSV usa ISO-8859-1; os arquivos de interface usam UTF-8.
Para adicionar fotos futuramente, copie os arquivos para
`src/main/resources/static/images/candidatos` e altere
`app.exibir-fotos=false` para `app.exibir-fotos=true` em application.properties.
A idade e calculada a partir da data atual, conforme o modelo fornecido.

## Consulta

A rota GET / recebe cargo, partido e texto. O texto pesquisa nome civil,
nome de urna ou numero. Filtros vazios mostram todos os candidatos.
O link Limpar retorna a consulta sem filtros.
