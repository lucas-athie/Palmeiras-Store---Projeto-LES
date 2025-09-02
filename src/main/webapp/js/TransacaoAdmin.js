document.addEventListener("DOMContentLoaded", () => {
  // Lista mockada de transações
  const transacoesMock = [
    { data: "01/09/2025", cliente: "Lucas Athié", descricao: "Compra na loja", valor: 150.00, status: "PROCESSAMENTO", tipo: "normal" },
    { data: "15/08/2025", cliente: "Lucas Athié", descricao: "Compra online", valor: 89.90, status: "EM TRÂNSITO", tipo: "normal" },
    { data: "02/08/2025", cliente: "Lucas Athié", descricao: "Estorno", valor: -50.00, status: "EM TROCA", tipo: "troca" },
    { data: "28/08/2025", cliente: "Maria Silva", descricao: "Compra na loja", valor: 200.00, status: "PROCESSAMENTO", tipo: "normal" },
    { data: "20/08/2025", cliente: "Maria Silva", descricao: "Compra online", valor: 120.50, status: "PROCESSAMENTO", tipo: "normal" },
    { data: "10/08/2025", cliente: "João Pereira", descricao: "Compra na loja", valor: 75.00, status: "EM TRÂNSITO", tipo: "normal" },
    { data: "05/08/2025", cliente: "João Pereira", descricao: "Estorno", valor: -30.00, status: "EM TROCA", tipo: "troca" },
    { data: "01/08/2025", cliente: "Ana Souza", descricao: "Compra online", valor: 300.00, status: "PROCESSAMENTO", tipo: "normal" },
    { data: "25/07/2025", cliente: "Ana Souza", descricao: "Compra na loja", valor: 180.00, status: "EM TRÂNSITO", tipo: "normal" },
    { data: "15/07/2025", cliente: "Carlos Lima", descricao: "Compra online", valor: 99.99, status: "PROCESSAMENTO", tipo: "normal" }
  ];

  const statusNormal = ["PROCESSAMENTO", "EM TRÂNSITO", "ENTREGUE"];
  const statusTroca = ["EM TROCA", "TROCA AUTORIZADA"];

  const tabela = document.querySelector("#tabela-transacoes-admin tbody");
  tabela.innerHTML = "";

  if (!transacoesMock.length) {
    tabela.innerHTML = `<tr><td colspan="5" class="carrinho-vazio">Nenhuma transação encontrada.</td></tr>`;
    return;
  }

  transacoesMock.forEach((t) => {
    const tr = document.createElement("tr");

    const btnStatus = document.createElement("button");
    btnStatus.textContent = t.status;
    btnStatus.style.padding = "6px 10px";
    btnStatus.style.borderRadius = "6px";
    btnStatus.style.border = "none";
    btnStatus.style.cursor = "pointer";
    btnStatus.style.color = "#fff";

    function atualizarCor(status) {
      if (status === "PROCESSAMENTO") btnStatus.style.backgroundColor = "#b36b00"; // laranja
      if (status === "EM TRÂNSITO") btnStatus.style.backgroundColor = "#0066cc"; // azul
      if (status === "ENTREGUE") btnStatus.style.backgroundColor = "#228B22"; // verde
      if (status === "EM TROCA") btnStatus.style.backgroundColor = "#cc6600"; // laranja escuro
      if (status === "TROCA AUTORIZADA") btnStatus.style.backgroundColor = "#9933cc"; // roxo
    }

    atualizarCor(t.status);

    btnStatus.addEventListener("click", () => {
      const fluxo = t.tipo === "troca" ? statusTroca : statusNormal;
      let pos = fluxo.indexOf(t.status);
      if (pos < fluxo.length - 1) {
        t.status = fluxo[pos + 1];
        btnStatus.textContent = t.status;
        atualizarCor(t.status);
      }
    });

    tr.innerHTML = `
      <td>${t.data}</td>
      <td>${t.cliente}</td>
      <td>${t.descricao}</td>
      <td style="text-align:right;">${t.valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</td>
    `;

    const tdStatus = document.createElement("td");
    tdStatus.style.textAlign = "center";
    tdStatus.appendChild(btnStatus);

    tr.appendChild(tdStatus);
    tabela.appendChild(tr);
  });
});