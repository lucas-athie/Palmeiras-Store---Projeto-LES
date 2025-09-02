document.addEventListener("DOMContentLoaded", () => {
  const transacoesMock = [
    { data: "01/09/2025", descricao: "Compra na loja", valor: 150.00, status: "Entregue" },
    { data: "15/08/2025", descricao: "Compra online", valor: 89.90, status: "Em trânsito" },
    { data: "02/08/2025", descricao: "Estorno", valor: -50.00, status: "Concluído" }
  ];

  const tabela = document.querySelector("#tabela-transacoes tbody");
  tabela.innerHTML = "";

  transacoesMock.forEach((t, index) => {
    const tr = document.createElement("tr");
    let acoes = "";

    if (t.status.toLowerCase() === "entregue") {
      acoes = `<button class="btn-troca" data-index="${index}">Solicitar Troca</button>`;
    }

    tr.innerHTML = `
      <td>${t.data}</td>
      <td>${t.descricao}</td>
      <td>${t.valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</td>
      <td>${t.status}</td>
      <td>${acoes}</td>
    `;
    tabela.appendChild(tr);
  });

  const modal = document.getElementById("modalTroca");
  const cancelarBtn = document.getElementById("cancelarTroca");
  const enviarBtn = document.getElementById("enviarTroca");
  let pedidoSelecionado = null;

  tabela.addEventListener("click", (e) => {
    if (e.target.classList.contains("btn-troca")) {
      pedidoSelecionado = e.target.getAttribute("data-index");
      modal.style.display = "block";
    }
  });

  cancelarBtn.addEventListener("click", () => {
    modal.style.display = "none";
    pedidoSelecionado = null;
    document.getElementById("motivoTroca").value = "";
    document.getElementById("imagemTroca").value = "";
  });

  enviarBtn.addEventListener("click", () => {
    const motivo = document.getElementById("motivoTroca").value.trim();
    const imagem = document.getElementById("imagemTroca").files[0];

    if (!motivo) {
      alert("Por favor, informe o motivo da troca.");
      return;
    }

    alert(`Troca solicitada para o pedido ${pedidoSelecionado}.\nMotivo: ${motivo}\nImagem: ${imagem ? imagem.name : "Nenhuma"}`);

    modal.style.display = "none";
    pedidoSelecionado = null;
    document.getElementById("motivoTroca").value = "";
    document.getElementById("imagemTroca").value = "";
  });

  window.addEventListener("click", (e) => {
    if (e.target === modal) {
      modal.style.display = "none";
    }
  });
});