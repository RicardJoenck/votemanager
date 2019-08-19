# Documentação API

## Pautas endpoints

Request

|Método|URL                   |
|:-----|:---------------------|
|POST  |api/1/pautas           |

Parâmetros

|Tipo  |Param     |Valor |
|:-----|:---------|:-----|
|BODY  |descricao |string|

Response

|Status|Response                                                                       |
|:-----|:------------------------------------------------------------------------------|
|200   |Exemplo:<br> `{"id":9887,"descricao":"teste performance","limiteVotacao":null}`|
|400   |`{"status": 400,"messagem": "Descrição não pode ser nula"}`|
|400   |`{"status": 400,"messagem": "Descrição não pode ser vazia"}`|
|400   |`{"status": 400,"messagem": "Descrição não pode ultrapassar de 255 caracteres"}`|
|400   |`{"status": 400,"messagem": "Formato de parametros não reconhecido"}`|

Request

|Método|URL                   |
|:-----|:---------------------|
|PUT  |api/1/pautas/<pauta_id>          |

Parâmetros

|Tipo  |Param     |Valor |
|:-----|:---------|:-----|
|BODY  |limiteVotacao |date |
|URL_PARAM  |pauta_id |number|

**limiteVotacao**<br>
formato: "yyyy-MM-dd HH:mm:ss VV" sendo VV o fuso horário<br>
somente o fuso "America/Sao_Paulo" é válido no momento


Response

|Status|Response                                                                       |
|:-----|:------------------------------------------------------------------------------|
|200   |Exemplo:<br> `{"id":9887,"descricao":"teste performance","limiteVotacao":"2019-08-16T23:34:54.481-03:00"}`|
|400   |`{"status": 400,"messagem": "Formato de data inválido. Padrão esperado: yyyy-MM-dd HH:mm:ss VV no fuso horário de America/Sao_Paulo"}`|
|400   |`{"status": 400,"messagem": "Fuso horário America/Anchorag ainda não é suportado, somente America/Sao_Paulo"}`|
|400   |`{"status": 400,"messagem": "Pauta 10034 já tem uma data limite para votação"}`|
|400   |`{"status": 400,"messagem": "Formato de parametros não reconhecido"}`|
|404   |`{"status": 404,"messagem": "Nenhum dado achado com id 1 para Pauta"}`|

## Votos endpoints

Request

|Método|URL                          |
|:-----|:----------------------------|
|POST  |api/1/pautas/<pauta_id>/votos|

Parâmetros

|Tipo       |Param     |Valor |
|:----------|:---------|:-----|
|BODY       |decisao   |string|
|BODY       |cpf       |string|
|URL_PARAM  |pauta_id  |string|

Response

|Status|Response                                                                       |
|:-----|:------------------------------------------------------------------------------|
|200   |Exemplo:<br> `{"id":9887,"descricao":"teste performance","limiteVotacao":null}`|
|400   |`{"status": 400,"messagem": "Votação para pauta 1 não está aberta/disponível"}`|
|400   |`{"status": 400,"messagem": "Já existe um voto do cpf 12345678 para pauta 1"}`|
|400   |`{"status": 400,"messagem": "Decisão inválida. Escolha entre Sim e Não"}`|
|400   |`{"status": 400,"messagem": "Associado 12345678 não está apto para votar"}`|
|400   |`{"status": 400,"messagem": "Formato de parametros não reconhecido"}`|
|404   |`{"status": 404,"messagem": "Associado não existe"}`|
|404   |`{"status": 404,"messagem": "Nenhum dado achado com id 1 para Pauta"}`|

Request

|Método|URL                   |
|:-----|:---------------------|
|GET  |api/1/pautas/<pauta_id>/votos|

Response

|Status|Response                                                                       |
|:-----|:------------------------------------------------------------------------------|
|200   |Exemplo:<br> `{"SIM":2,"NÃO":"3"}`|
|404   |`{"status": 404,"messagem": "Nenhum dado achado com id 1 para Pauta"}`|

## Considerações

### Tarefa Bônus 3

Utilizei JMeter para rodar 100000 pedidos, infelizmente devido ao volume o computador não suportou o teste por muito tempo e forcei o encerramento,
 segue resultados
 
 |# Samples|Avg|Min |Max  |Std. Dev.|Error %|
 |:-----|:---------|:-----|:-----|:---------|:-----|
 |15479|4899|2|21482|7661.38|0.10%|
 
 ### Tarefa Bônus 4
 
Prefiro usar o formato de versionamento "api/versão" na própria URL, este formato providência uma maneira
fácil de manter a documentação organizada por versões, flexível para alterar o backend e o usuário final consegue visualizar
versão de rapidamente.

### Configuração

usei o arquivo application.properties para manter as credencias para o banco de dados. No momento elas estão em branco e devem
ser adicionadas para a aplicação funcionar

### Logs

Usei Spring AOP para automaticamente adicionar um log no começo de métodos nos controllers e services e um log
com o resultado dos métodos de services. Escolhi usar esse módulo para reduzir a redundância de código

### Exceções

Pela mesma razão que nos logs usei a anotação @ControlleAdvice para tratar de erros de maneira centralizada e evitar
redundância de código  


