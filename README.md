# Projeto
Projeto Web de Registros escolares baseado no curso de Spring MVC do professor Xavecoding

## Como usar
1. Clone o projeto
2. Configure o banco de dados no servidor local
	- Crie um banco de dados 
	- Localize e entre no arquivo **application.properties**
	- Altere as variáveis PROD_DB_HOST, PROD_DB_PORT, PROD_DB_NAME, PROD_DB_USERNAME e PROD_DB_PASSWORD de acordo com seu servidor
3. Execute o arquivo **RegescwebApplication.java** e entre na porta especificada acima

## TODO:
- [ ] Consertar bugs:
	- Permitir que apenas professores disponíveis (não demitidos) possam pegar matérias
- [ ] Otimizar o relacionamento entre as entidades
	- Fazer com que o nome de cada objeto seja um link para seus detalhes
	- Tratar quando queremos deletar um professor, mas deixar a disciplina
- [ ] Melhorar o front-end
	- Fazer um modal para mostrar/escolher as disciplinas do aluno
	- Melhorar os links (botões)
- [ ] Paginação
- [ ] Comentar/refatorar código
	- Arrumar a função toDisciplina() para que ela não tenha que receber o repositório de professor
	- Fazer um modal de confirmação para a deleção de um objeto
