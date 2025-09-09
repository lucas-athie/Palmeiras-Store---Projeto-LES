// ../js/EditarCliente.js
document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8000";
  const params = new URLSearchParams(window.location.search);
  const clienteId = params.get("clienteId");
  const btnSalvar = document.getElementById("btnSalvar");
  const btnExcluir = document.getElementById("btnExcluir");
  const btnEnderecos = document.getElementById("btnEnderecos");
  const btnCartao = document.getElementById("btnCartao");

  // injeta clienteId em todos os links do header
  if (clienteId) {
    document.querySelectorAll("header nav a").forEach((link) => {
      const href = link.getAttribute("href");
      if (!href || href.startsWith("#") || href.startsWith("http")) return;
      const [path] = href.split("?");
      link.setAttribute("href", `${path}?clienteId=${clienteId}`);
    });
  }

  if (!clienteId) {
    alert("ID do cliente não informado na URL");
    return;
  }

  // 1) Carrega dados do cliente
  (async function loadCliente() {
    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}`);
      if (!resp.ok) throw new Error("Falha ao buscar cliente");
      const c = await resp.json();

      document.getElementById("nome").value = c.nome || "";
      document.getElementById("cpf").value = c.cpf || "";
      document.getElementById("genero").value = (c.genero || "").toLowerCase();
      document.getElementById("data-nascimento").value =
        c.dataNascimento?.split("T")[0] || "";
      document.getElementById("email").value = c.email || "";
      document.getElementById("obs").value = c.observacao || "";
      document.getElementById("ativo").value = String(c.ativo);

      if (c.telefone) {
        // seleciona exatamente "Fixo" ou "Celular"
        document.getElementById("tipo-telefone").value = c.telefone.tipo;
        // formata (XX) XXXXX-XXXX
        const num = c.telefone.numero || "";
        document.getElementById("telefone").value = `(${
          c.telefone.ddd
        }) ${num.slice(0, -4)}-${num.slice(-4)}`;
      }
    } catch (err) {
      console.error(err);
      alert("Não foi possível carregar os dados do cliente");
    }
  })();

  // 2) Salvar alterações
  btnSalvar.addEventListener("click", async (event) => {
    event.preventDefault();

    const nome = document.getElementById("nome").value.trim();
    const cpf = document.getElementById("cpf").value.trim().replace(/\D/g, "");
    const genero = document.getElementById("genero").value;
    const senha = document.getElementById("senha").value;
    const confirmar = document.getElementById("confirmar-senha").value;
    const dataN = document.getElementById("data-nascimento").value;
    const email = document.getElementById("email").value.trim();
    const obs = document.getElementById("obs").value.trim();
    const ativo = document.getElementById("ativo").value;
    const tipoTel = document.getElementById("tipo-telefone").value; // "Fixo" ou "Celular"
    const rawTel = document.getElementById("telefone").value.trim();

    if (!nome || !cpf || !genero || !dataN || !email || !tipoTel) {
      alert("Preencha todos os campos obrigatórios.");
      return;
    }
    if (senha && senha !== confirmar) {
      alert("As senhas não coincidem!");
      return;
    }

    // extrai DDD e número (padrão "11" se faltar)
    const digits = rawTel.replace(/\D/g, "");
    const ddd = digits.length >= 10 ? digits.slice(0, 2) : "11";
    const num = digits.length >= 10 ? digits.slice(2) : digits;
    if (!num) {
      alert("Informe o número de telefone.");
      return;
    }

    const telefone = {
      tipo: tipoTel,
      ddd,
      numero: num,
    };

    const payload = {
      idCliente: parseInt(clienteId, 10),
      nome,
      cpf,
      genero: genero.toUpperCase(),
      senha: senha || undefined,
      dataNascimento: dataN,
      telefone,
      email,
      observacao: obs,
      ativo: ativo === "true",
    };

    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(payload),
      });
      const data = await resp.json();
      if (!resp.ok) throw new Error(data.error || "Falha ao atualizar cliente");
      alert("Cliente atualizado com sucesso!");
    } catch (err) {
      console.error(err);
      alert("Erro ao salvar alterações: " + err.message);
    }
  });

  // 3) Excluir cliente
  btnExcluir.addEventListener("click", async () => {
    if (!confirm("Deseja realmente excluir este cliente?")) return;
    try {
      const resp = await fetch(`${API_BASE}/clientes/${clienteId}`, {
        method: "DELETE",
      });
      if (!resp.ok) throw new Error("Falha ao excluir cliente");
      alert("Cliente excluído com sucesso");
      window.location.href = `ListaClientes.html?clienteId=${clienteId}`;
    } catch (err) {
      console.error(err);
      alert("Erro ao excluir cliente: " + err.message);
    }
  });

  // 4) Navegação para Endereços e Cartões mantendo clienteId
  btnEnderecos.addEventListener("click", () => {
    window.location.href = `CadastroEndereco.html?clienteId=${clienteId}`;
  });
  btnCartao.addEventListener("click", () => {
    window.location.href = `CadastroCartao.html?clienteId=${clienteId}`;
  });
});
