# spring-boot-multitenant

Projeto que gerencia o catálogo de tenants


## Criando um tenant

Para criar um tenant, execute o comando utilizando o verbo POST:

```
POST http://localhost:8083/tenants
```

Como payload da mensagem, envie os dados do tenant a ser criado:

```
{
    "tenantId": "teste5",
    "schema": "tenant5"
}
```

Esta ação irá criar o tenant e executar os scripts mapeados no 
liquibase neste novo tenant, tornando-o atualizado conforme 
definições existentes.


## Aplicando atualização em todos os tenants

Esta ação irá executar o update em todos os tenants existentes 
na tabela `tenants` do catálogo principal da conexão.

Para atualizar todos os tenants, execute o comando utilizando o verbo POST:

```
http://localhost:8083/tenants/updateAll
```

Este comando não tem conteúdo a ser enviado no payload.

O programa irá obter todos os tenants cadastrados e para cada um 
deles irá aplicar o schema mapeado no Liquibase e **caso o tenant não 
exista**, ele vai criá-lo e aplicar a estrutura do liquibase naquele
tenant novo.