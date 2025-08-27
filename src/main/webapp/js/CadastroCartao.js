document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('form-cartao');
    const tabela = document.querySelector('#tabela-cartoes tbody');
    const btnSalvar = document.getElementById('btnSalvarCartao');

    const clienteAtual = JSON.parse(localStorage.getItem('clienteAtual'));
    if (!clienteAtual) {
        alert("Nenhum cliente selecionado.");
        window.location.href = "ListaClientes.html";
        return;
    }

    const cpfCliente = clienteAtual.cpf?.replace(/\D/g, '');
    const API_BASE = "http://localhost:5500/api"; // ajuste depois para o back

    async function carregarCartoes() {
        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/cartoes`);
            if (!res.ok) throw new Error("Erro ao buscar cartões");
            const data = await res.json();
            renderTabela(data);
        } catch (err) {
            console.error(err);
            tabela.innerHTML = `<tr><td colspan="5">Erro ao carregar cartões.</td></tr>`;
        }
    }

    function renderTabela(cartoes) {
        tabela.innerHTML = "";
        if (!cartoes.length) {
            tabela.innerHTML = `<tr><td colspan="5">Nenhum cartão cadastrado.</td></tr>`;
            return;
        }
        cartoes.forEach((cartao) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>**** **** **** ${cartao.numero.slice(-4)}</td>
                <td>${cartao.nome}</td>
                <td>${cartao.bandeira}</td>
                <td>${cartao.preferencial ? '<span class="preferencial">Sim</span>' : 'Não'}</td>
                <td class="acoes">
                    <button onclick="definirPreferencial('${cartao.id}')">Preferencial</button>
                    <button onclick="excluirCartao('${cartao.id}')">Excluir</button>
                </td>
            `;
            tabela.appendChild(tr);
        });
    }

    btnSalvar.addEventListener('click', async () => {
        const dados = {
            numero: document.getElementById('numero-cartao').value.trim(),
            nome: document.getElementById('nome-cartao').value.trim(),
            bandeira: document.getElementById('bandeira-cartao').value,
            codigoSeguranca: document.getElementById('codigo-seguranca').value.trim(),
            preferencial: document.getElementById('preferencial').checked
        };

        if (!dados.numero || !dados.nome || !dados.bandeira || !dados.codigoSeguranca) {
            alert('Preencha todos os campos obrigatórios!');
            return;
        }

        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/cartoes`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dados)
            });
            if (!res.ok) throw new Error("Erro ao salvar cartão");
            await carregarCartoes();
            form.reset();
        } catch (err) {
            console.error(err);
            alert("Erro ao salvar cartão.");
        }
    });

    window.excluirCartao = async function (idCartao) {
        if (!confirm("Deseja realmente excluir este cartão?")) return;
        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/cartoes/${idCartao}`, {
                method: "DELETE"
            });
            if (!res.ok) throw new Error("Erro ao excluir");
            await carregarCartoes();
        } catch (err) {
            console.error(err);
            alert("Erro ao excluir cartão.");
        }
    };

    window.definirPreferencial = async function (idCartao) {
        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/cartoes/${idCartao}/preferencial`, {
                method: "PATCH"
            });
            if (!res.ok) throw new Error("Erro ao atualizar preferencial");
            await carregarCartoes();
        } catch (err) {
            console.error(err);
            alert("Erro ao atualizar preferencial.");
        }
    };

    carregarCartoes();
});