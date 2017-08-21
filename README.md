# Step

This is my main/final project of my technical course at (<a href="http://www.timoteo.cefetmg.br/" target="_blank">CEFET-MG, Campus Timóteo</a>).
The main objective is creating an antispam aplication.

# Documentation:
This is the official documentation in brazilian portuguese. English version may come not so soon.

### 1. Glossário: definições e siglas

.apk – Android Package, ou Pacote do Android, é o formato em que aplicativos são compactados;<br>
.zip – Formato de arquivo usado para compactação de dados armazenados no computador. O objetivo da compactação é reduzir o tamanho de um arquivo ou agrupar vários arquivos em um só.

### 2. Descrição do mini-mundo do projeto 

A facilidade de comunicação também trouxe problemas. Empresas usam tal tecnologia para promoção de produtos e serviços, tirando o sossego da população que constantemente recebe mensagens e ligações indesejadas.<br><br>
Além disso, há também pessoas má intencionadas que tentam através de ligações e mensagens realizar golpes, podendo variar entre pedidos de transferências bancárias para expedição de prêmios, falsos sequestros que também requerem transferências e mensagens que contêm links que disseminam vírus. Essa gama de golpes vem aumentando até onde a criatividade desses criminosos permite.
Por causa disso, fez-se necessário o desenvolvimento de um aplicativo, o Lista Negra. Ele visa erradicar spam e golpes por meio de uma rede que armazena números e dados a respeito dos mesmos, avisando os usuários de possíveis ameaças, mas sem revogar o direito de atender às chamadas.<br><br>
Ao instalar o aplicativo, são adicionados todos os números presentes na agenda que possuem conta no serviço, pois a arma principal é justamente a possibilidade de interligar os usuários, compartilhando os números cadastrados.
Quando um determinado número é bloqueado, o mesmo é enviado para nosso banco de dados junto à denúncia realizada. Ao receber uma nova chamada, o banco de dados é consultado, verificando-se a existência e quantidade de denúncias e, a partir disso, o usuário toma uma decisão consciente sobre a ligação.<br><br>
Essas e outras funções não citadas têm o objetivo de suprir a falta de controle que dispositivos Android possuem sobre as chamadas, sendo que muitos até permitem o bloqueio, mas o usuário não consegue adivinhar quais números deve bloquear ou não, por isso fica exposto a golpes pelo menos uma vez por número. Além disso, caso troque de celular ou precise resetar o dispositivo, todos os números bloqueados são perdidos, voltando à estaca zero. É nesse ponto que a integração do Lista Negra promete ser um diferencial no mercado, protegendo o usuário ao rotular números.<br>

### 3. Lista de funções do projeto

Foi identificada a seguinte lista de funções para este produto:

<table>
  <tr>
    <th>Número de ordem</th>
    <th>Tipo</th>
    <th>Nome da função</th>
    <th>Descrição</th>
  </tr>
  <tr>
    <td>1</td>
    <td>1</td>
    <td>Cadastro/Login de usuário</td>
    <td>Na primeira vez que o login for realizado, o usuário é cadastrado com o nome de usuário e código originário da rede social escolhida. Na segunda vez e em diante, o sistema faz apenas um login trivial.</td>
  </tr>
  <tr>
    <td>2</td>
    <td>1</td>
    <td>Deletar dados da conta</td>
    <td>Permite ao usuário deletar os dados de sua conta.</td>
  </tr>
  <tr>
    <td>3</td>
    <td>1</td>
    <td>(Administração) Deletar dados da conta</td>
    <td>Permite ao administrador deletar os dados da conta de um usuário.</td>
  </tr>
  <tr>
    <td>4</td>
    <td>1</td>
    <td>(Administração) Deletar denúncias de um número</td>
    <td>Permite ao administrador deletar um determinado número de denúncias de um dado número caso seja requisitado por uma empresa ou usuário.</td>
  </tr>
  <tr>
    <td>5</td>
    <td>2</td>
    <td>Controle de denúncias</td>
    <td>Cadastra, remove ou edita os tipos de denúncia de um determinado número.</td>
  </tr>
  <tr>
    <td>6</td>
    <td>2</td>
    <td>Controle de bloqueio</td>
    <td>Cadastra, remove ou edita os números bloqueados pelo usuário.</td>
  </tr>
  <tr>
    <td>7</td>
    <td>2</td>
    <td>Controle de bloqueios realizados por amigos</td>
    <td>Por meio de notificações exibidas em uma aba do app, permite ao usuário selecionar se deseja ou não bloquear um número bloqueado por seu amigo.</td>
  </tr>
  <tr>
    <td>8</td>
    <td>2</td>
    <td>Controle de amigos</td>
    <td>Após entrar no perfil do amigo, é possível adicioná-lo ou removê-lo.</td>
  </tr>
  <tr>
    <td>9</td>
    <td>2</td>
    <td>Configuração de tipo de conexão para download do banco de dados</td>
    <td>Caso tenha selecionado que deseja baixar o banco de dados completo, ou pelo menos os números com determinados tipos de denúncias, poderá também configurar se quer fazer o download somente em Wi-Fi ou em Wi-Fi e redes móveis.</td>
  </tr>
  <tr>
    <td>10</td>
    <td>2</td>
    <td>Configuração de bloqueio</td>
    <td>Permite ao usuário selecionar quais serviços usarão o recurso de bloqueio, seja mensagens, chamadas ou mensageiros como o Whatsapp e Telegram.</td>
  </tr>
  <tr>
    <td>11</td>
    <td>2</td>
    <td>Configuração de chamadas</td>
    <td>Permite ao usuário selecionar o que o aplicativo deverá fazer ao receber ligação de números com determinadas denúncias, como por exemplo, silenciar a chamada ou automaticamente bloquear o número. Também poderá ser configurado o limite de denúncias que uma chamada recebível pode ter.</td>
  </tr>
  <tr>
    <td>12</td>
    <td>2</td>
    <td>Configuração de feedback</td>
    <td>Permite ao usuário configurar se quer ou não que apareça a tela de feedback pós chamadas ou em quais situações ela deve aparecer, seja após chamada de número suspeito, contato ou indefinido.</td>
  </tr>
  <tr>
    <td>13</td>
    <td>2</td>
    <td>Feedback pós chamadas</td>
    <td>Esta funcionalidade será exibida logo após o fim da chamada, e pedirá ao usuário informações a respeito da ligação. A configuração inicial define que contatos não terão feedback.</td>
  </tr>
  <tr>
    <td>14</td>
    <td>2</td>
    <td>Introdução</td>
    <td>A introdução serve para dar uma breve explicação sobre o funcionamento do app, pedir algumas permissões, realizar login ou cadastro e fazer as configurações básicas e essenciais</td>
  </tr>
  <tr>
    <td>15</td>
    <td>2</td>
    <td>Gerenciamento de permissões</td>
    <td>Esta funcionalidade será exibida apenas na introdução para dispositivos com Android 6.0 ou superior, dando ao aplicativo a capacidade de funcionar corretamente através do uso das permissões requisitadas. Mesmo que as permissões sejam mudadas através das configurações do Android, essa funcionalidade é exclusiva da introdução e não será exibida novamente.</td>
  </tr>
  <tr>
    <td>16</td>
    <td>3</td>
    <td>Tela de chamadas</td>
    <td>Sobrescreve o aplicativo padrão de chamadas, cumprindo as mesmas funções, além de exibir informações a respeito do número.</td>
  </tr>
  <tr>
    <td>17</td>
    <td>3</td>
    <td>Exibição de denúncias</td>
    <td>Carrega as denúncias de um determinado número e exibe ao usuário.</td>
  </tr>
  <tr>
    <td>18</td>
    <td>3</td>
    <td>Exibição de números bloqueados</td>
    <td>Carrega os números cadastrados na lista negra de um determinado usuário.</td>
  </tr>
  <tr>
    <td>19</td>
    <td>3</td>
    <td>Exibição de amigos</td>
    <td>Carrega uma lista dos amigos do usuário.</td>
  </tr>
  <tr>
    <td>20</td>
    <td>3</td>
    <td>Busca por usuários</td>
    <td>Exibe um usuário a partir da pesquisa do seu nome de usuário.</td>
  </tr>
  <tr>
    <td>21</td>
    <td>4</td>
    <td>Guia rápido</td>
    <td>Exibe um guia rápido para a maioria das funcionalidades.</td>
  </tr>
  <tr>
    <td>22</td>
    <td>4</td>
    <td>Login</td>
    <td>Permite ao usuário usar o serviço. É necessário fazer login apenas uma vez no celular, a não ser que os dados sejam perdidos.</td>
  </tr>
  <tr>
    <td>23</td>
    <td>4</td>
    <td>(Administração) Login</td>
    <td>Permite ao administrador logar no sistema de controle.</td>
  </tr>
</table>

<b>Tipos:</b>
1 - Cadastro (entrada básica)<br>
2 - Controle (entrada mais complexa)<br>
3 - Relatório (Saída)<br>
4 - Controle de Acesso<br>

### 4. Materiais de referência

<table>
  <tr>
    <th colspan="2">Tipos de materias de referência</th>
  </tr>
  <tr>
    <td>Reclamações</td>
    <td>Baseado nelas notamos a necessidade dos tipos de denúncias necessárias para o software relatar.</td>
  </tr>
  <tr>
    <td>Jornais</td>
    <td>Baseados neles nós temos as descrições de golpes para classificá-los da de forma devida. Ex.: Falso sequestro, falsas empresas, entre outros.</td>
  </tr>
  <tr>
    <td>Entrevistas informais</td>
    <td>Conversas informais com o professores e alunos e sugestões de recursos para o aplicativo.</td>
  </tr>
</table>

### 5. Requisitos de software 

#### 5.1. Descrição dos Atores

<table>
  <tr>
    <th>Número</th>
    <th>Nome</th>
    <th>Descrição</th>
    <th>Frequência de uso</th>
    <th>Proficiência em informática</th>
  </tr>
  <tr>
    <td>1</td>
    <td>Usuário</td>
    <td>Os usuários buscam usufruir das aplicabilidades do nosso serviço, podendo realizar cadastros, remoções e edições, adicionar e remover amigos, configurar o aplicativo a gosto.</td>
    <td>Eventual</td>
    <td>Baixa</td>
  </tr>
  <tr>
    <td>2</td>
    <td>Sistema</td>
    <td>O sistema é responsável por ações que envolvem funcionalidades automáticas, como mostrar quantidade de denúncias, pedir feedback e atualizar o banco de dados.</td>
    <td>Diário</td>
    <td>-</td>
  </tr>
  <tr>
    <td>3</td>
    <td>Administrador</td>
    <td>O administrador é responsável pelo gerenciamento do banco de dados do aplicativo, moderando números e contas</td>
    <td>Diário</td>
    <td>Média</td>
  </tr>
</table>

#### 5.2. Casos de uso

<a href="https://ibb.co/m0abcv"><img src="https://image.ibb.co/gNowcv/Picture1.png" alt="Diagrama casos de uso" border="0"></a>


### 6. Projeto de interface

#### Android

##### Introdução

<a href="https://ibb.co/fokL4a"><img height="500px" src="https://preview.ibb.co/b4zPVF/Intro_1.jpg" alt="Intro_1" border="0"></a>
<a href="https://ibb.co/iNGhHv"><img height="500px" src="https://preview.ibb.co/irPrAF/Intro_2.jpg" alt="Intro_2" border="0"></a>
<a href="https://ibb.co/bFnYPa"><img height="500px" src="https://preview.ibb.co/kj37ja/Intro_3.jpg" alt="Intro_3" border="0"></a>
<a href="https://ibb.co/mnP2Hv"><img height="500px" src="https://preview.ibb.co/mAYwcv/Intro_4.jpg" alt="Intro_4" border="0"></a>
<a href="https://ibb.co/kuAbcv"><img height="500px" src="https://preview.ibb.co/cKvnja/Intro_5.jpg" alt="Intro_5" border="0"></a>

##### Home

<a href="https://ibb.co/meQOqF"><img height="500px" src="https://preview.ibb.co/j7qpVF/Controle_de_den_ncias_e_edi_o_de_n_mero.jpg" alt="Controle_de_den_ncias_e_edi_o_de_n_mero" border="0"></a>
<a href="https://ibb.co/cKe54a"><img height="500px" src="https://preview.ibb.co/iiiXja/Exemplo_de_guia_r_pido.jpg" alt="Exemplo_de_guia_r_pido" border="0"></a>
<a href="https://ibb.co/bAziqF"><img height="500px" src="https://preview.ibb.co/nMGUVF/Home.jpg" alt="Home" border="0"></a>
<a href="https://ibb.co/iF8yPa"><img height="500px" src="https://preview.ibb.co/hhXQ4a/Pesquisa.jpg" alt="Pesquisa" border="0"></a>
<a href="https://ibb.co/gWyyPa"><img height="500px" src="https://preview.ibb.co/fMWJPa/Remover_n_mero.jpg" alt="Remover_n_mero" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Perfil

<a href="https://ibb.co/gPotqF"><img height="500px" src="https://preview.ibb.co/fVn8Pa/Exibi_o_de_den_ncias.jpg" alt="Exibi_o_de_den_ncias" border="0"></a>
<a href="https://ibb.co/fhs8Pa"><img height="500px" src="https://preview.ibb.co/bDX8Pa/Perfil.jpg" alt="Perfil" border="0"></a>
<a href="https://ibb.co/eM7mAF"><img height="500px" src="https://preview.ibb.co/npecHv/Perfil_pr_prio.jpg" alt="Perfil_pr_prio" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Notificações

<a href="https://ibb.co/kcFpVF"><img height="500px" src="https://preview.ibb.co/nBZ54a/Notifica_es.jpg" alt="Notifica_es" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Chamadas

<a href="https://ibb.co/bMQpVF"><img height="500px" src="https://preview.ibb.co/jpxgcv/Chamada_normal.jpg" alt="Chamada_normal" border="0"></a>
<a href="https://ibb.co/g5Nsja"><img height="500px" src="https://preview.ibb.co/jnP54a/Chamada_sobrescrita.jpg" alt="Chamada_sobrescrita" border="0"></a>
<a href="https://ibb.co/cXUiqF"><img height="500px" src="https://preview.ibb.co/iwY9VF/Feedback_p_s_chamada.jpg" alt="Feedback_p_s_chamada" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Cadastro de números

<a href="https://ibb.co/gRuoPa"><img height="500px" src="https://preview.ibb.co/fGS8Pa/Cadastro_de_n_meros.jpg" alt="Cadastro_de_n_meros" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Ajustes

<a href="https://ibb.co/dTVHHv"><img height="500px" src="https://preview.ibb.co/ejwDqF/Ajustes_Parte_1.jpg" alt="Ajustes_Parte_1" border="0"></a>
<a href="https://ibb.co/b9reVF"><img height="500px" src="https://preview.ibb.co/gHUBcv/Ajustes_Parte_2.jpg" alt="Ajustes_Parte_2" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

#### Web

##### Login

<a href="https://ibb.co/dYDhja"><img width="700px" src="https://preview.ibb.co/dnvv4a/Login.jpg" alt="Login" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Usuários

<a href="https://ibb.co/jX1Pxv"><img width="700px" src="https://preview.ibb.co/naUoPa/Usu_rios.jpg" alt="Usu_rios" border="0"></a>
<a href="https://ibb.co/dCcKVF"><img width="700px" src="https://preview.ibb.co/fjDtqF/Usu_rio_selecionado.jpg" alt="Usu_rio_selecionado" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

##### Números

<a href="https://ibb.co/makHHv"><img width="700px" src="https://preview.ibb.co/kwATPa/N_meros.jpg" alt="N_meros" border="0"></a>
<a href="https://ibb.co/jtuBcv"><img width="700px" src="https://preview.ibb.co/e7PYqF/N_mero_selecionado.jpg" alt="N_mero_selecionado" border="0"></a><br /><a target='_blank' href='https://pt.imgbb.com/'></a><br />

### 7. Modelo de análise: projeto de dados

<img src="https://image.ibb.co/cyn7rk/DER.png" alt="DER" border="0" />
