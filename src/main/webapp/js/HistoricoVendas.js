document.addEventListener("DOMContentLoaded", () => {
  const ctx = document.getElementById("graficoVendas").getContext("2d");
  let chartInstance = null;

  // Dados mockados
  const dadosMock = gerarDadosMock();

  document.getElementById("btnGerar").addEventListener("click", () => {
    const inicio = document.getElementById("dataInicio").value;
    const fim = document.getElementById("dataFim").value;
    const tipo = document.getElementById("tipoConsulta").value;
    const nome = document.getElementById("nomeConsulta").value.trim().toLowerCase();

    if (!inicio || !fim) {
      alert("Selecione as datas de início e fim.");
      return;
    }

    const filtrados = dadosMock.filter(item => {
      const dataItem = new Date(item.data);
      return dataItem >= new Date(inicio) &&
             dataItem <= new Date(fim) &&
             item[tipo].toLowerCase().includes(nome);
    });

    if (!filtrados.length) {
      alert("Nenhum dado encontrado para os filtros selecionados.");
      return;
    }

    const agrupados = {};
    filtrados.forEach(item => {
      if (!agrupados[item[tipo]]) agrupados[item[tipo]] = {};
      if (!agrupados[item[tipo]][item.data]) agrupados[item[tipo]][item.data] = 0;
      agrupados[item[tipo]][item.data] += item.vendas;
    });

    const labels = [...new Set(filtrados.map(f => f.data))].sort((a,b) => new Date(a) - new Date(b));

    const datasets = Object.keys(agrupados).map(chave => ({
      label: chave,
      data: labels.map(l => agrupados[chave][l] || 0),
      borderColor: gerarCorAleatoria(),
      backgroundColor: "transparent",
      tension: 0.3
    }));

    if (chartInstance) chartInstance.destroy();

    chartInstance = new Chart(ctx, {
      type: "line",
      data: { labels, datasets },
      options: {
        responsive: true,
        plugins: {
          legend: { display: true },
          title: { display: true, text: "Histórico de Vendas" }
        },
        scales: {
          y: { beginAtZero: true }
        }
      }
    });
  });

  function gerarDadosMock() {
    const produtos = ["Camisa Verde", "Camisa Branca", "Shorts", "Chuteira"];
    const categorias = ["Roupas", "Calçados"];
    const dados = [];
    const inicio = new Date("2024-01-01");
    const fim = new Date("2025-08-01");

    for (let d = new Date(inicio); d <= fim; d.setMonth(d.getMonth() + 1)) {
      produtos.forEach(prod => {
        const cat = prod === "Chuteira" ? "Calçados" : "Roupas";
        dados.push({
          data: d.toISOString().split("T")[0],
          produto: prod,
          categoria: cat,
          vendas: Math.floor(Math.random() * 200) + 50
        });
      });
    }
    return dados;
  }

  function gerarCorAleatoria() {
    const r = Math.floor(Math.random() * 200);
    const g = Math.floor(Math.random() * 200);
    const b = Math.floor(Math.random() * 200);
    return `rgb(${r},${g},${b})`;
  }
});