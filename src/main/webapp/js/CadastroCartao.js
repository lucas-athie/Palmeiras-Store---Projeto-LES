// ../js/CadastroCartao.js

document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8000";
  const params = new URLSearchParams(window.location.search);
  const clienteId = params.get("clienteId");
  const tabelaBody = document.querySelector("#tabela-cartoes tbody");
  const form = document.getElementById("form-cartao");
  const btnSalvar = document.getElementById("btnSalvarCartao");

  // Inputs do formulário
  const numeroInput = document.getElementById("numero-cartao");
  const nomeInput = document.getElementById("nome-cartao");
  const bandeiraSelect = document.getElementById("bandeira-cartao");
  const cvvInput = document.getElementById("codigo-seguranca");
  const preferencialCheck = document.getElementById("preferencial");

  let clienteAtual = null;

  if (!clienteId) {
    alert("ID do cliente não informado na URL.");
    return;
  }


  document.querySelectorAll("header nav a").forEach((link) => {
    const href = link.getAttribute("href");
    if (!href || href.startsWith("#") || href.startsWith("http")) return;
    const [path] = href.split("?");
    link.setAttribute("href", `${path}?clienteId=${clienteId}`);
  });


  async function fetchCliente() {
    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}`, {
        headers: { Accept: "application/json" },
      });
      if (!resp.ok) throw new Error(`Status ${resp.status}`);
      clienteAtual = await resp.json();
      localStorage.setItem("clienteAtual", JSON.stringify(clienteAtual));
      return clienteAtual;
    } catch (err) {
      console.error(err);
      alert("Erro ao carregar dados do cliente: " + err.message);
    }
  }


  async function prefillTitular() {
    if (!clienteAtual) {
      await fetchCliente();
    }
    if (clienteAtual) {
      nomeInput.value = clienteAtual.nome;
      nomeInput.readOnly = true;
    }
  }


  async function fetchCartoes() {
    if (!clienteAtual) {
      await fetchCliente();
    }
    return clienteAtual && clienteAtual.cartoes ? clienteAtual.cartoes : [];
  }


  async function carregarCartoes() {
    const lista = await fetchCartoes();
    tabelaBody.innerHTML = "";

    if (!lista.length) {
      tabelaBody.innerHTML = `
        <tr>
          <td colspan="5" style="text-align:center">
            Nenhum cartão cadastrado.
          </td>
        </tr>`;
      return;
    }

    lista.forEach((cartao) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${cartao.numero}</td>
        <td>${cartao.titular}</td>
        <td>${cartao.bandeira}</td>
        <td>${cartao.principal ? "Sim" : "Não"}</td>
        <td>
          <button data-id="${cartao.id}">Excluir</button>
        </td>`;
      tabelaBody.appendChild(tr);

      tr.querySelector("button").addEventListener("click", () => {
        excluirCartao(cartao.id);
      });
    });
  }


  btnSalvar.addEventListener("click", async () => {
    const payload = {
      numero: numeroInput.value.replace(/\s+/g, ""),
      titular: nomeInput.value.trim(),
      bandeira: bandeiraSelect.value.toUpperCase(),
      codigoSeguranca: cvvInput.value.trim(),
      principal: preferencialCheck.checked,
    };


    const required = ["numero", "titular", "bandeira", "codigoSeguranca"];
    for (const field of required) {
      if (!payload[field]) {
        alert(`Preencha o campo: ${field}`);
        return;
      }
    }

    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}/cartoes`, {
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
      alert("Cartão cadastrado com sucesso!");
      form.reset();
      await fetchCliente(); // atualiza clienteAtual com o novo cartão
      carregarCartoes();
    } catch (err) {
      console.error(err);
      alert("Erro ao cadastrar cartão: " + err.message);
    }
  });

  async function excluirCartao(cartaoId) {
    if (!confirm("Deseja realmente excluir este cartão?")) return;
    try {
      const resp = await fetch(
        `${API_BASE}/clientes/${clienteId}/cartoes/${cartaoId}`,
        {
          method: "DELETE",
          headers: { Accept: "application/json" },
        }
      );
      if (!resp.ok) {
        const err = await resp.json().catch(() => ({}));
        throw new Error(err.error || `Status ${resp.status}`);
      }

      clienteAtual.cartoes = clienteAtual.cartoes.filter(
        (c) => c.id !== cartaoId
      );
      carregarCartoes();
    } catch (err) {
      console.error(err);
      alert("Erro ao excluir cartão: " + err.message);
    }
  }


  prefillTitular().then(carregarCartoes);
});
