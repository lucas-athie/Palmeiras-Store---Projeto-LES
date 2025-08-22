document.addEventListener("DOMContentLoaded", function () {
    const tabela = document.getElementById("tabela-clientes");
    const clientes = JSON.parse(localStorage.getItem("clientes")) || [];

    if (clientes.length === 0) {
        tabela.innerHTML = `<tr><td colspan="7">Nenhum cliente cadastrado.</td></tr>`;
        return;
    }

    clientes.forEach((cliente) => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${cliente.nome}</td>
            <td>${cliente.cpf}</td>
            <td>${cliente.email}</td>
            <td>${cliente.telefone}</td>
            <td>${cliente.ativo !== false ? "Ativo" : "Inativo"}</td>
            <td>
                <button onclick='editarCliente(${JSON.stringify(cliente)})'>Editar</button>
                <button onclick='excluirCliente("${cliente.cpf}")'>Excluir</button>
                <button onclick='alternarStatus("${cliente.cpf}")'>
                    ${cliente.ativo !== false ? "Inativar" : "Ativar"}
                </button>
            </td>
        `;

        tabela.appendChild(tr);
    });
});

function editarCliente(cliente) {
    localStorage.setItem("clienteEditando", JSON.stringify(cliente));
    window.location.href = "EditarCliente.html";
}

function excluirCliente(cpf) {
    if (confirm("Tem certeza que deseja excluir este cliente?")) {
        let clientes = JSON.parse(localStorage.getItem("clientes")) || [];
        clientes = clientes.filter(c => c.cpf !== cpf);
        localStorage.setItem("clientes", JSON.stringify(clientes));
        location.reload();
    }
}

function alternarStatus(cpf) {
    let clientes = JSON.parse(localStorage.getItem("clientes")) || [];
    const index = clientes.findIndex(c => c.cpf === cpf);

    if (index !== -1) {
        clientes[index].ativo = !(clientes[index].ativo !== false);
        localStorage.setItem("clientes", JSON.stringify(clientes));
        location.reload();
    }
}