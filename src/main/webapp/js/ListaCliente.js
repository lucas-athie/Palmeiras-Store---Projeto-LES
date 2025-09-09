// ListaClientes.js
document.addEventListener("DOMContentLoaded", () => {
  const tabela = document.getElementById("tabela-clientes");
  const filtro = document.getElementById("filtro");
  const API_BASE = "http://localhost:8000";

  let clientesCache = [];

  // Busca todos os clientes
  async function fetchClientes() {
    const resp = await fetch(`${API_BASE}/clientes`, {
      method: "GET",
      headers: { Accept: "application/json" },
    });
    if (!resp.ok) {
      throw new Error(`Erro ${resp.status} ao buscar clientes`);
    }
    return resp.json();
  }

  // Carrega e filtra a lista
  async function carregarClientes(termo = "") {
    try {
      let lista = await fetchClientes();
      clientesCache = lista;

      if (termo) {
        const t = termo.toLowerCase();
        lista = lista.filter((c) =>
          Object.entries(c)
            .filter(([k]) => k !== "senha")
            .some(([_, v]) => String(v).toLowerCase().includes(t))
        );
      }

      renderTabela(lista);
    } catch (err) {
      tabela.innerHTML = `<tr><td colspan="7">Erro ao carregar: ${err.message}</td></tr>`;
    }
  }

  // Renderiza a tabela
  function renderTabela(lista) {
    tabela.innerHTML = "";
    if (!lista.length) {
      tabela.innerHTML = `<tr><td colspan="7">Nenhum cliente encontrado.</td></tr>`;
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
          <button onclick="editarCliente('${c.cpf}')">Editar</button>
          <button onclick="excluirCliente('${c.idCliente}')">Excluir</button>
          <button onclick="alternarStatus('${c.idCliente}')">${btnStatus}</button>
          <button onclick="verTransacoes('${c.idCliente}')">Transações</button>
        </td>`;
      tabela.appendChild(tr);
    });
  }

  // Filtro em tempo real
  if (filtro) {
    filtro.addEventListener("input", () => {
      carregarClientes(filtro.value.trim());
    });
  }

  // Inicial
  carregarClientes();

  // Ações globais
  window.editarCliente = (cpf) => {
    // Armazena para edição ou redireciona
    window.location.href = `EditarCliente.html?cpf=${cpf}`;
  };

  window.excluirCliente = (id) => {
    if (!confirm("Tem certeza que deseja excluir este cliente?")) return;
    fetch(`${API_BASE}/clientes/${id}`, { method: "DELETE" })
      .then((r) => {
        if (!r.ok) throw new Error(r.statusText);
        carregarClientes(filtro.value.trim());
      })
      .catch((e) => alert("Erro ao excluir: " + e.message));
  };

  window.alternarStatus = async (id) => {
    try {
      const cliente = clientesCache.find(
        (c) => String(c.idCliente) === String(id)
      );
      if (!cliente) throw new Error("Cliente não encontrado");

      // Inverter ativo
      cliente.ativo = cliente.ativo === false ? true : false;

      const resp = await fetch(`${API_BASE}/clientes/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cliente),
      });
      if (!resp.ok) {
        const err = await resp.json().catch(() => ({}));
        throw new Error(err.error || resp.statusText);
      }

      carregarClientes(filtro.value.trim());
    } catch (e) {
      alert("Erro ao alterar status: " + e.message);
    }
  };

  window.verTransacoes = (id) => {
    window.location.href = `Transacao.html?clienteId=${id}`;
  };
});
