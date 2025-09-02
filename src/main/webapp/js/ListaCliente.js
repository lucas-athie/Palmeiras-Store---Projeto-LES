document.addEventListener("DOMContentLoaded", function () {
    const tabela = document.getElementById("tabela-clientes");
    const filtro = document.getElementById("filtro");

    function carregarClientes(termo = "") {
        let clientes = JSON.parse(localStorage.getItem("clientes") || "[]");

        if (termo) {
            const termoLower = termo.toLowerCase();
            clientes = clientes.filter(cliente =>
                Object.entries(cliente)
                    .filter(([chave]) => chave !== "senha") // não busca na senha
                    .some(([_, valor]) =>
                        String(valor).toLowerCase().includes(termoLower)
                    )
            );
        }

        renderTabela(clientes);
    }

    function renderTabela(lista) {
        tabela.innerHTML = "";
        if (!lista.length) {
            tabela.innerHTML = `<tr><td colspan="7">Nenhum cliente encontrado.</td></tr>`;
            return;
        }

        lista.forEach((cliente) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${cliente.nome}</td>
                <td>${cliente.cpf}</td>
                <td>${cliente.email}</td>
                <td>${cliente.telefone}</td>
                <td>${cliente.ativo !== false ? "Ativo" : "Inativo"}</td>
                <td>
                    <button onclick='editarCliente("${cliente.cpf}")'>Editar</button>
                    <button onclick='excluirCliente("${cliente.cpf}")'>Excluir</button>
                    <button onclick='alternarStatus("${cliente.cpf}")'>
                        ${cliente.ativo !== false ? "Inativar" : "Ativar"}
                    </button>
                    <button onclick='verTransacoes("${cliente.cpf}")'>Transações</button>
                </td>
            `;
            tabela.appendChild(tr);
        });
    }

    if (filtro) {
        filtro.addEventListener("input", () => {
            const termo = filtro.value.trim();
            carregarClientes(termo);
        });
    }

    // Inicial
    carregarClientes();

    // Funções globais para botões
    window.editarCliente = function (cpf) {
        const clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
        const cliente = clientes.find(c => c.cpf.replace(/\D/g, '') === cpf.replace(/\D/g, ''));
        if (cliente) {
            localStorage.setItem("clienteEditando", JSON.stringify(cliente));
            localStorage.setItem("clienteAtual", JSON.stringify(cliente));
            window.location.href = "EditarCliente.html";
        } else {
            alert("Cliente não encontrado.");
        }
    };

    window.excluirCliente = function (cpf) {
        if (!confirm("Tem certeza que deseja excluir este cliente?")) return;
        let clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
        clientes = clientes.filter(c => c.cpf.replace(/\D/g, '') !== cpf.replace(/\D/g, ''));
        localStorage.setItem("clientes", JSON.stringify(clientes));
        carregarClientes(filtro.value.trim());
    };

    window.alternarStatus = function (cpf) {
        let clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
        const index = clientes.findIndex(c => c.cpf.replace(/\D/g, '') === cpf.replace(/\D/g, ''));
        if (index !== -1) {
            clientes[index].ativo = !(clientes[index].ativo !== false);
            localStorage.setItem("clientes", JSON.stringify(clientes));
            carregarClientes(filtro.value.trim());
        }
    };

    // Novo: Botão Transações
    window.verTransacoes = function (cpf) {
        const clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
        const cliente = clientes.find(c => c.cpf.replace(/\D/g, '') === cpf.replace(/\D/g, ''));
        if (cliente) {
            localStorage.setItem("clienteAtual", JSON.stringify(cliente));
            window.location.href = "Transacao.html";
        } else {
            alert("Cliente não encontrado.");
        }
    };
});