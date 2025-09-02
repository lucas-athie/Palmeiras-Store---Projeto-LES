document.addEventListener("DOMContentLoaded", () => {
  const tabelaResumo = document.querySelector("#tabela-resumo tbody");
  const subtotalEl = document.getElementById("subtotal");
  const freteEl = document.getElementById("valor-frete");
  const totalEl = document.getElementById("total-geral");
  const enderecoSelect = document.getElementById("endereco");
  const cartaoSelect = document.getElementById("cartao");
  const btnConfirmar = document.getElementById("btnConfirmarCompra");

  // Recupera dados salvos pelo carrinho
  const dados = JSON.parse(localStorage.getItem("dadosFinalizacao") || "{}");

  if (!dados.produtos || !dados.produtos.length) {
    tabelaResumo.innerHTML = `<tr><td colspan="4" style="text-align:center;">Nenhum produto para finalizar.</td></tr>`;
    return;
  }

  // Renderiza produtos
  tabelaResumo.innerHTML = "";
  dados.produtos.forEach(item => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${item.nome}</td>
      <td>${item.qtd}</td>
      <td>${item.preco.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</td>
      <td>${(item.qtd * item.preco).toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</td>
    `;
    tabelaResumo.appendChild(tr);
  });

  // Calcula valores
  const subtotal = dados.subtotal ?? dados.produtos.reduce((acc, item) => acc + item.preco * item.qtd, 0);
  const frete = dados.frete ?? 5;
  const total = dados.total ?? subtotal + frete;

  subtotalEl.textContent = subtotal.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  freteEl.textContent = frete.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  totalEl.textContent = total.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

  // Endereço mockado
  enderecoSelect.innerHTML = "";
  const optEndereco = document.createElement("option");
  optEndereco.value = "mock";
  optEndereco.textContent = "Alto do Ipiranga Rua Eduardo de Castro JR 232 08730830";
  enderecoSelect.appendChild(optEndereco);

  // Cartão mockado
  cartaoSelect.innerHTML = "";
  const optCartao = document.createElement("option");
  optCartao.value = "mock";
  optCartao.textContent = "2222222222222222 Lucas Athié Mastercard";
  cartaoSelect.appendChild(optCartao);

  // Confirmar compra
  btnConfirmar.addEventListener("click", () => {
    alert("Compra confirmada! Obrigado por comprar na Palmeiras Store.");
    localStorage.removeItem("dadosFinalizacao");
    localStorage.removeItem("carrinhoLoja");
    window.location.href = "ListaLoja.html";
  });
});