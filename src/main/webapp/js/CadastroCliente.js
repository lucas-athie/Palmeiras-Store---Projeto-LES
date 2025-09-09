// ../js/CadastroCliente.js

document.addEventListener("DOMContentLoaded", () => {
  const btnSalvar = document.getElementById("btnSalvar");
  const API_BASE = "http://localhost:8000";

  btnSalvar.addEventListener("click", async (event) => {
    event.preventDefault();

    // 1) Campos básicos
    const nome = document.getElementById("nome").value.trim();
    const cpf = document.getElementById("cpf").value.trim().replace(/\D/g, "");
    const genero = document.getElementById("genero").value;
    const senha = document.getElementById("senha").value;
    const confirmarSenha = document.getElementById("confirmar-senha").value;
    const dataNascimento = document.getElementById("data-nascimento").value;
    const email = document.getElementById("email").value.trim();

    if (!nome || !cpf || !genero || !senha || !dataNascimento || !email) {
      alert("Preencha todos os campos obrigatórios.");
      return;
    }
    if (senha !== confirmarSenha) {
      alert("As senhas não coincidem!");
      return;
    }

    // 2) Telefone (DDD padrão "11")
    const telefone = {
      tipo: document.getElementById("tipo-telefone").value,
      ddd: "11",
      numero: document.getElementById("telefone").value.trim(),
    };
    if (!telefone.numero) {
      alert("Informe o número de telefone.");
      return;
    }

    // 3) Monta Endereços (ENTREGA e COBRANCA)
    function montarEndereco(prefixo, tipoEndereco) {
      const tipoResidencia = document.getElementById(
        `${prefixo}-tipo-endereco`
      ).value; // CASA, APARTAMENTO, CONDOMINIO

      const tipoLogradouro = document.getElementById(
        `${prefixo}-tipo-logradouro`
      ).value; // RUA, AVENIDA, …

      const logradouro = document
        .getElementById(`${prefixo}-logradouro`)
        .value.trim();

      const numero = document.getElementById(`${prefixo}-numero`).value.trim();

      const bairro = document.getElementById(`${prefixo}-bairro`).value.trim();

      const cep = document.getElementById(`${prefixo}-cep`).value.trim();

      const cidadeNome = document
        .getElementById(`${prefixo}-cidade`)
        .value.trim();

      const estadoValor = document
        .getElementById(`${prefixo}-estado`)
        .value.trim();

      const paisNome = document.getElementById(`${prefixo}-pais`).value.trim();

      if (
        !tipoResidencia ||
        !tipoLogradouro ||
        !logradouro ||
        !numero ||
        !bairro ||
        !cep ||
        !cidadeNome ||
        !estadoValor ||
        !paisNome
      ) {
        alert(
          `Preencha todos os campos do endereço de ${tipoEndereco.toLowerCase()}.`
        );
        throw new Error("Campos de endereço incompletos");
      }

      return {
        tipoEndereco, // "ENTREGA" ou "COBRANCA"
        tipoResidencia, // "casa", "apartamento", "condominio"
        tipoLogradouro, // "rua", "avenida", ...
        logradouro: { nome: logradouro },
        numero,
        bairro,
        cep,
        cidade: { nome: cidadeNome },
        estado: { nome: estadoValor, sigla: estadoValor },
        pais: { nome: paisNome },
      };
    }

    let enderecos;
    try {
      const eEntrega = montarEndereco("entrega", "ENTREGA");
      const eCobranca = montarEndereco("cobranca", "COBRANCA");
      enderecos = [eEntrega, eCobranca];
    } catch {
      return; // já exibiu alerta
    }

    // 4) Cartão
    const numeroCartao = document.getElementById("numero-cartao").value.trim();
    const nomeCartao = document.getElementById("nome-cartao").value.trim();
    const bandeira = document.getElementById("bandeira-cartao").value;
    const cvv = document.getElementById("codigo-seguranca").value.trim();

    if (!numeroCartao || !nomeCartao || !bandeira || !cvv) {
      alert("Preencha todos os campos do cartão.");
      return;
    }

    const cartao = {
      principal: true,
      numero: numeroCartao,
      titular: nomeCartao,
      bandeira,
      codigoSeguranca: cvv,
    };

    // 5) Monta payload final
    const payload = {
      nome,
      cpf,
      genero,
      senha,
      dataNascimento,
      telefone,
      email,
      enderecos,
      cartoes: [cartao],
    };

    // 6) Envia ao backend e redireciona com clienteId
    try {
      const resp = await fetch(`${API_BASE}/clientes/completo`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(payload),
      });

      const data = await resp.json();
      if (!resp.ok) {
        throw new Error(data.error || "Erro no cadastro completo");
      }

      const clienteId = data.id;
      alert("Cliente criado com sucesso! ID: " + clienteId);
      window.location.href = `ListaLoja.html?clienteId=${clienteId}`;
    } catch (err) {
      console.error(err);
      alert("Erro no cadastro: " + err.message);
    }
  });
});
