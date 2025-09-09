// ../js/ListaClientes.js
document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8000";
  const tabela = document.getElementById("tabela-clientes");
  const params = new URLSearchParams(window.location.search);
  const clienteIdQS = params.get("clienteId");

  // 0) injeta clienteId em todos os links da navbar
  if (clienteIdQS) {
    document.querySelectorAll("header nav a").forEach((link) => {
      const href = link.getAttribute("href");
      if (!href || href.startsWith("#") || href.startsWith("http")) return;
      const [path] = href.split("?");
      link.setAttribute(
        "href",
        `${path}?clienteId=${encodeURIComponent(clienteIdQS)}`
      );
    });
  }

  // 1) Carrega TODA a lista de clientes (ignora qualquer filtro)
  async function carregarClientes() {
    try {
      const resp = await fetch(`${API_BASE}/clientes`, {
        method: "GET",
        headers: { Accept: "application/json" },
      });
      if (!resp.ok) {
        throw new Error(`Erro ${resp.status} ao buscar clientes`);
      }
      const lista = await resp.json();
      renderTabela(lista);
    } catch (err) {
      tabela.innerHTML = `
        <tr><td colspan="6">Erro ao carregar clientes: ${err.message}</td></tr>`;
    }
  }

  // 2) Renderiza a tabela usando idCliente para todas as ações
  function renderTabela(lista) {
    tabela.innerHTML = "";
    if (!lista.length) {
      tabela.innerHTML = `
        <tr><td colspan="6">Nenhum cliente encontrado.</td></tr>`;
      return;
    }

    lista.forEach((c) => {
      const telefoneStr = c.telefone
        ? `${c.telefone.tipo} (${c.telefone.ddd}) ${c.telefone.numero}`
        : "";
      const statusTxt = c.ativo !== false ? "Ativo" : "Inativo";
      const btnStatus = c.ativo !== false ? "Inativar" : "Ativar";

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${c.nome}</td>
        <td>${c.cpf}</td>
        <td>${c.email}</td>
        <td>${telefoneStr}</td>
        <td>${statusTxt}</td>
        <td>
          <button onclick="editarCliente('${c.idCliente}')">Editar</button>
          <button onclick="excluirCliente('${c.idCliente}')">Excluir</button>
          <button onclick="alternarStatus('${c.idCliente}')">${btnStatus}</button>
          <button onclick="verTransacoes('${c.idCliente}')">Transações</button>
        </td>`;
      tabela.appendChild(tr);
    });
  }

  // 3) Funções globais de ação
  window.editarCliente = (id) => {
    // redireciona para edição, passando o id do cliente selecionado
    window.location.href = `EditarCliente.html?clienteId=${encodeURIComponent(
      id
    )}`;
  };

  window.excluirCliente = async (id) => {
    if (!confirm("Tem certeza que deseja excluir este cliente?")) return;
    try {
      const resp = await fetch(
        `${API_BASE}/clientes/${encodeURIComponent(id)}`,
        {
          method: "DELETE",
        }
      );
      if (!resp.ok) throw new Error(`Erro ${resp.status}`);
      carregarClientes();
    } catch (err) {
      alert("Erro ao excluir cliente: " + err.message);
    }
  };

  window.alternarStatus = async (id) => {
    try {
      // busca o cliente para inverter o campo 'ativo'
      const r1 = await fetch(`${API_BASE}/clientes/${encodeURIComponent(id)}`);
      if (!r1.ok) throw new Error("Falha ao buscar cliente");
      const c = await r1.json();
      c.ativo = !c.ativo;

      const r2 = await fetch(`${API_BASE}/clientes/${encodeURIComponent(id)}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(c),
      });
      if (!r2.ok) throw new Error(`Erro ${r2.status}`);
      carregarClientes();
    } catch (err) {
      alert("Erro ao alterar status: " + err.message);
    }
  };

  window.verTransacoes = (id) => {
    window.location.href = `Transacao.html?clienteId=${encodeURIComponent(id)}`;
  };

  // 4) Inicializa a lista
  carregarClientes();
});
