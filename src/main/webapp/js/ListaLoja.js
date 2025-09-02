document.addEventListener("DOMContentLoaded", () => {
  const KEY_PRODUTOS = "produtosLoja";
  const KEY_CARRINHO = "carrinhoLoja";

  const grid = document.getElementById("grid-produtos");
  const contador = document.getElementById("contador");
  const estadoLista = document.getElementById("estado-lista");

  const busca = document.getElementById("busca");
  const categoria = document.getElementById("categoria");
  const precoMin = document.getElementById("precoMin");
  const precoMax = document.getElementById("precoMax");
  const ordenar = document.getElementById("ordenar");
  const btnLimpar = document.getElementById("btnLimpar");

  let debounceTimer = null;

  function getProdutos() {
    const raw = localStorage.getItem(KEY_PRODUTOS);
    if (raw) return JSON.parse(raw);
    const seed = seedProdutos();
    localStorage.setItem(KEY_PRODUTOS, JSON.stringify(seed));
    return seed;
  }

  function setCarrinho(carrinho) {
    localStorage.setItem(KEY_CARRINHO, JSON.stringify(carrinho));
  }
  function getCarrinho() {
    return JSON.parse(localStorage.getItem(KEY_CARRINHO) || "[]");
  }

  function seedProdutos() {
    return [
      { id: "p001", nome: "Camisa Oficial 2025", categoria: "camisas", preco: 349.9, descricao: "Camisa oficial 2025 com tecido respirável e escudo bordado.", imagemUrl: "../img/produtos/camisa-2025.jpg", disponivel: true },
      { id: "p002", nome: "Agasalho Treino", categoria: "agasalhos", preco: 499.0, descricao: "Agasalho completo para treino em dias frios.", imagemUrl: "../img/produtos/agasalho.jpg", disponivel: true },
      { id: "p003", nome: "Boné Oficial", categoria: "acessorios", preco: 129.9, descricao: "Boné oficial ajustável, com logo bordado.", imagemUrl: "../img/produtos/bone.jpg", disponivel: true }
    ];
  }

  function formatarPreco(valor) {
    return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  }

  function renderProdutos(lista) {
    grid.innerHTML = "";
    if (!lista.length) {
      grid.innerHTML = `<div style="grid-column:1/-1;text-align:center;color:#666;">Nenhum produto encontrado.</div>`;
      contador.textContent = "0 produtos";
      return;
    }

    lista.forEach(p => {
      const card = document.createElement("div");
      card.className = "card-produto";
      card.innerHTML = `
        <div class="card-header">
          <img src="${p.imagemUrl}" alt="${p.nome}" onerror="this.style.display='none'">
          <div>
            <p class="card-titulo">${p.nome}</p>
            <p class="card-categoria">${p.categoria}</p>
          </div>
        </div>
        <div class="card-descricao">${p.descricao}</div>
        <div class="card-footer">
          <span class="preco">${formatarPreco(p.preco)}</span>
          <span class="status">${p.disponivel ? "Disponível" : "Indisponível"}</span>
        </div>
        <div class="btns">
          <button class="btn-secundario" data-id="${p.id}" data-acao="detalhes">Ver detalhes</button>
          <button data-id="${p.id}" data-acao="adicionar" ${!p.disponivel ? "disabled" : ""}>Adicionar ao carrinho</button>
        </div>
      `;
      grid.appendChild(card);
    });

    contador.textContent = `${lista.length} produto${lista.length > 1 ? "s" : ""}`;
  }

  function aplicarFiltros(produtos) {
    const q = busca.value.trim().toLowerCase();
    const cat = categoria.value;
    const min = parseFloat(precoMin.value) || null;
    const max = parseFloat(precoMax.value) || null;

    let lista = produtos.filter(p => {
      const matchBusca = !q || p.nome.toLowerCase().includes(q) || p.categoria.toLowerCase().includes(q);
      const matchCat = !cat || p.categoria === cat;
      const matchMin = min === null || p.preco >= min;
      const matchMax = max === null || p.preco <= max;
      return matchBusca && matchCat && matchMin && matchMax;
    });

    if (ordenar.value === "preco_asc") lista.sort((a, b) => a.preco - b.preco);
    if (ordenar.value === "preco_desc") lista.sort((a, b) => b.preco - a.preco);
    if (ordenar.value === "nome_asc") lista.sort((a, b) => a.nome.localeCompare(b.nome));
    if (ordenar.value === "nome_desc") lista.sort((a, b) => b.nome.localeCompare(a.nome));

    return lista;
  }

  function carregarProdutos() {
    const produtos = getProdutos();
    const filtrados = aplicarFiltros(produtos);
    renderProdutos(filtrados);
  }

  busca.addEventListener("input", () => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(carregarProdutos, 300);
  });
  categoria.addEventListener("change", carregarProdutos);
  precoMin.addEventListener("change", carregarProdutos);
  precoMax.addEventListener("change", carregarProdutos);
  ordenar.addEventListener("change", carregarProdutos);
  btnLimpar.addEventListener("click", () => {
    busca.value = "";
    categoria.value = "";
    precoMin.value = "";
    precoMax.value = "";
    ordenar.value = "";
    carregarProdutos();
  });

  grid.addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-acao]");
    if (!btn) return;
    const id = btn.getAttribute("data-id");
    const acao = btn.getAttribute("data-acao");

    if (acao === "detalhes") {
      window.location.href = `DetalheProduto.html?id=${encodeURIComponent(id)}`;
    }

    if (acao === "adicionar") {
      const produtos = getProdutos();
      const prod = produtos.find(p => p.id === id);
      if (!prod) return;
      const carrinho = getCarrinho();
      const idx = carrinho.findIndex(item => item.id === id);
      if (idx >= 0) {
        carrinho[idx].qtd += 1;
      } else {
        carrinho.push({ ...prod, qtd: 1 });
      }
      setCarrinho(carrinho);
      estadoLista.textContent = `${prod.nome} adicionado ao carrinho!`;
      setTimeout(() => estadoLista.textContent = "", 2000);
    }
  });

  carregarProdutos();
});