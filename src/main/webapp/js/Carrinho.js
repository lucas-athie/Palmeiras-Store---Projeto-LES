document.addEventListener("DOMContentLoaded", () => {
  const KEY_CARRINHO = "carrinhoLoja";
  const tabela = document.querySelector("#tabela-carrinho tbody");
  const totalGeral = document.getElementById("total-geral");
  const itensInfo = document.getElementById("itens-info");
  const btnLimpar = document.getElementById("btnLimparCarrinho");
  const btnFinalizar = document.getElementById("btnFinalizar");

  function getCarrinho() {
    return JSON.parse(localStorage.getItem(KEY_CARRINHO) || "[]");
  }

  function setCarrinho(carrinho) {
    localStorage.setItem(KEY_CARRINHO, JSON.stringify(carrinho));
  }

  function formatarPreco(valor) {
    return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  }

  function renderCarrinho() {
    const carrinho = getCarrinho();
    tabela.innerHTML = "";
    let total = 0;
    let totalItens = 0;

    if (!carrinho.length) {
      tabela.innerHTML = `<tr><td colspan="5" class="carrinho-vazio">Carrinho vazio</td></tr>`;
      totalGeral.textContent = "Total: R$ 0,00";
      itensInfo.textContent = "0 item(s)";
      return;
    }

    carrinho.forEach((item, index) => {
      const subtotal = item.preco * item.qtd;
      total += subtotal;
      totalItens += item.qtd;

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="produto-cell">
          ${item.imagemUrl ? `<img src="${item.imagemUrl}" alt="${item.nome}" onerror="this.style.display='none'">` : ""}
          ${item.nome}
        </td>
        <td>${formatarPreco(item.preco)}</td>
        <td>
          <div class="qtd-control">
            <button class="qtd-btn" data-acao="diminuir" data-index="${index}">-</button>
            <input type="number" class="qtd-input" min="1" value="${item.qtd}" data-index="${index}">
            <button class="qtd-btn" data-acao="aumentar" data-index="${index}">+</button>
          </div>
        </td>
        <td>${formatarPreco(subtotal)}</td>
        <td>
          <button class="acao-btn btn-remover" data-acao="remover" data-index="${index}">Remover</button>
        </td>
      `;
      tabela.appendChild(tr);
    });

    totalGeral.textContent = `Total: ${formatarPreco(total)}`;
    itensInfo.textContent = `${totalItens} item${totalItens > 1 ? "s" : ""}`;
  }

  tabela.addEventListener("click", (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;
    const acao = btn.getAttribute("data-acao");
    const index = parseInt(btn.getAttribute("data-index"), 10);
    let carrinho = getCarrinho();

    if (acao === "remover") {
      carrinho.splice(index, 1);
    }
    if (acao === "aumentar") {
      carrinho[index].qtd += 1;
    }
    if (acao === "diminuir") {
      if (carrinho[index].qtd > 1) {
        carrinho[index].qtd -= 1;
      }
    }

    setCarrinho(carrinho);
    renderCarrinho();
  });

  tabela.addEventListener("change", (e) => {
    if (e.target.classList.contains("qtd-input")) {
      const index = parseInt(e.target.getAttribute("data-index"), 10);
      let carrinho = getCarrinho();
      let novaQtd = parseInt(e.target.value, 10);
      if (isNaN(novaQtd) || novaQtd < 1) novaQtd = 1;
      carrinho[index].qtd = novaQtd;
      setCarrinho(carrinho);
      renderCarrinho();
    }
  });

  btnLimpar.addEventListener("click", () => {
    if (confirm("Deseja realmente limpar o carrinho?")) {
      setCarrinho([]);
      renderCarrinho();
    }
  });

  btnFinalizar.addEventListener("click", () => {
    console.log("Botão Finalizar clicado");

    const carrinho = getCarrinho();
    if (!carrinho.length) {
      alert("Seu carrinho está vazio.");
      return;
    }

    const subtotal = carrinho.reduce((acc, item) => acc + item.preco * item.qtd, 0);
    const frete = 5;
    const total = subtotal + frete;

    const enderecoMock = "Alto do Ipiranga Rua Eduardo de Castro JR 232";
    const cartaoMock = "2222222222222222 Lucas Athié Mastercard";

    localStorage.setItem("dadosFinalizacao", JSON.stringify({
      produtos: carrinho,
      subtotal,
      frete,
      total,
      endereco: enderecoMock,
      cartao: cartaoMock
    }));

    console.log("Dados salvos para finalização:", JSON.parse(localStorage.getItem("dadosFinalizacao")));

    window.location.href = "FinalizarCompra.html";
  });

  // Inicial
  renderCarrinho();
});