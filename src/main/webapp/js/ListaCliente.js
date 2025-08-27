document.addEventListener("DOMContentLoaded", function () {
    const tabela = document.getElementById("tabela-clientes");
    const filtro = document.getElementById("filtro");

    const API_BASE = "http://localhost:5500/api"; // ajuste depois o back

    async function carregarClientes(termo = "") {
        try {
            let url = `${API_BASE}/clientes`;
            if (termo) {
                url += `?search=${encodeURIComponent(termo)}`;
            }

            const res = await fetch(url);
            if (!res.ok) throw new Error("Erro ao carregar clientes");

            const data = await res.json();
            renderTabela(data);
        } catch (err) {
            console.error(err);
            tabela.innerHTML = `<tr><td colspan="7">Erro ao carregar clientes.</td></tr>`;
        }
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

    carregarClientes();
    window.editarCliente = function (cpf) {
        fetch(`${API_BASE}/clientes/${cpf.replace(/\D/g, '')}`)
            .then(res => {
                if (!res.ok) throw new Error("Erro ao buscar cliente");
                return res.json();
            })
            .then(cliente => {
                localStorage.setItem("clienteEditando", JSON.stringify(cliente));
                window.location.href = "EditarCliente.html";
            })
            .catch(err => {
                console.error(err);
                alert("Erro ao abrir cliente para edição");
            });
    };

    window.excluirCliente = function (cpf) {
        if (!confirm("Tem certeza que deseja excluir este cliente?")) return;
        fetch(`${API_BASE}/clientes/${cpf.replace(/\D/g, '')}`, { method: "DELETE" })
            .then(res => {
                if (!res.ok) throw new Error("Erro ao excluir cliente");
                carregarClientes();
            })
            .catch(err => {
                console.error(err);
                alert("Erro ao excluir cliente");
            });
    };

    window.alternarStatus = function (cpf) {
        fetch(`${API_BASE}/clientes/${cpf.replace(/\D/g, '')}/status`, { method: "PATCH" })
            .then(res => {
                if (!res.ok) throw new Error("Erro ao alterar status");
                carregarClientes();
            })
            .catch(err => {
                console.error(err);
                alert("Erro ao alterar status do cliente");
            });
    };
});