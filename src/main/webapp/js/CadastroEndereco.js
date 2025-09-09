// ../js/CadastroEndereco.js

document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8000";
  const params = new URLSearchParams(window.location.search);
  const clienteId = params.get("clienteId");

  if (!clienteId) {
    alert("ID do cliente não informado na URL.");
    return;
  }

  // 1) Propagar clienteId em todos os links da navbar
  document.querySelectorAll("header nav a").forEach((link) => {
    const href = link.getAttribute("href");
    if (!href || href.startsWith("#") || href.startsWith("http")) return;
    const [path] = href.split("?");
    link.setAttribute("href", `${path}?clienteId=${clienteId}`);
  });

  const tabelaBody = document.querySelector("#tabela-enderecos tbody");
  const form = document.getElementById("form-endereco");
  const btnSalvar = document.getElementById("btnSalvar");

  // 2) Busca o cliente completo (inclui endereços e cartões) e retorna só o array de endereços
  async function fetchEnderecos() {
    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}`, {
        headers: { Accept: "application/json" },
      });
      if (!resp.ok) throw new Error(`Erro ${resp.status}`);
      const cliente = await resp.json();
      return cliente.enderecos || [];
    } catch (e) {
      console.error(e);
      alert("Não foi possível carregar endereços:\n" + e.message);
      return [];
    }
  }

  // 3) Renderiza a tabela de endereços
  async function carregarEnderecos() {
    const lista = await fetchEnderecos();
    tabelaBody.innerHTML = "";

    if (lista.length === 0) {
      tabelaBody.innerHTML = `
        <tr>
          <td colspan="10" style="text-align:center">
            Nenhum endereço cadastrado.
          </td>
        </tr>`;
      return;
    }

    lista.forEach((end) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${end.apelido || ""}</td>
        <td>${end.tipoResidencia}</td>
        <td>${end.tipoLogradouro} ${end.logradouro.nome || end.logradouro}</td>
        <td>${end.numero}</td>
        <td>${end.bairro}</td>
        <td>${end.cidade.nome || end.cidade}</td>
        <td>${end.estado.sigla || end.estado}</td>
        <td>${end.cep}</td>
        <td>${end.tipoEndereco}</td>
        <td>
          <button data-id="${end.idEndereco}">Excluir</button>
        </td>`;
      tabelaBody.appendChild(tr);

      // botão Excluir
      tr.querySelector("button").addEventListener("click", () => {
        excluirEndereco(end.idEndereco);
      });
    });
  }

  // 4) Handler para criar um novo endereço
  btnSalvar.addEventListener("click", async () => {
    const payload = {
      apelido: document.getElementById("nome-endereco").value.trim(),
      tipoResidencia: document.getElementById("tipo-residencia").value,
      tipoLogradouro: document.getElementById("tipo-logradouro").value,
      logradouro: document.getElementById("logradouro").value.trim(),
      numero: document.getElementById("numero").value.trim(),
      bairro: document.getElementById("bairro").value.trim(),
      cep: document.getElementById("cep").value.trim(),
      cidade: document.getElementById("cidade").value.trim(),
      estado: document.getElementById("estado").value.trim(),
      pais: document.getElementById("pais").value.trim(),
      tipoEndereco: document.getElementById("tipo-uso").value,
      observacoes: document.getElementById("observacao").value.trim(),
    };

    // validação básica
    for (const [field, val] of Object.entries(payload)) {
      if (field !== "observacoes" && !val) {
        alert(`Preencha o campo: ${field}`);
        return;
      }
    }

    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}/enderecos`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(payload),
      });
      if (!resp.ok) {
        const err = await resp.json().catch(() => ({}));
        throw new Error(err.error || `Status ${resp.status}`);
      }
      alert("Endereço cadastrado com sucesso!");
      form.reset();
      carregarEnderecos();
    } catch (e) {
      console.error(e);
      alert("Erro ao cadastrar endereço:\n" + e.message);
    }
  });

  // 5) Excluir endereço
  async function excluirEndereco(idEndereco) {
    if (!confirm("Deseja realmente excluir este endereço?")) return;
    try {
      const resp = await fetch(
        `${API_BASE}/clientes/${clienteId}/enderecos/${idEndereco}`,
        {
          method: "DELETE",
          headers: { Accept: "application/json" },
        }
      );
      if (!resp.ok) {
        const err = await resp.json().catch(() => ({}));
        throw new Error(err.error || `Status ${resp.status}`);
      }
      carregarEnderecos();
    } catch (e) {
      console.error(e);
      alert("Erro ao excluir endereço:\n" + e.message);
    }
  }

  // 6) Inicializa
  carregarEnderecos();
});
