document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('form-endereco');
    const tabela = document.querySelector('#tabela-enderecos tbody');
    const btnSalvar = document.getElementById('btnSalvar');

    const API_BASE = "http://localhost:5500/api"; // ajuste conforme o back
    const clienteAtual = JSON.parse(localStorage.getItem('clienteAtual'));

    if (!clienteAtual) {
        alert("Nenhum cliente selecionado.");
        window.location.href = "ListaClientes.html";
        return;
    }
    const cpfCliente = clienteAtual.cpf?.replace(/\D/g, '');

    async function carregarEnderecos() {
        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/enderecos`);
            if (!res.ok) throw new Error("Erro ao buscar endereços");
            const data = await res.json();
            renderTabela(data);
        } catch (err) {
            console.error(err);
            tabela.innerHTML = `<tr><td colspan="10">Erro ao carregar endereços.</td></tr>`;
        }
    }

    function renderTabela(enderecos) {
        tabela.innerHTML = "";
        if (!enderecos.length) {
            tabela.innerHTML = `<tr><td colspan="10">Nenhum endereço cadastrado.</td></tr>`;
            return;
        }
        enderecos.forEach((endereco) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${endereco.nome}</td>
                <td>${endereco.tipoResidencia}</td>
                <td>${endereco.tipoLogradouro} ${endereco.logradouro}</td>
                <td>${endereco.numero}</td>
                <td>${endereco.bairro}</td>
                <td>${endereco.cidade}</td>
                <td>${endereco.estado}</td>
                <td>${endereco.cep}</td>
                <td>
                    <select onchange="alterarTipoUso('${endereco.id}', this.value)">
                        <option value="entrega" ${endereco.tipoUso === 'entrega' ? 'selected' : ''}>Entrega</option>
                        <option value="cobranca" ${endereco.tipoUso === 'cobranca' ? 'selected' : ''}>Cobrança</option>
                        <option value="ambos" ${endereco.tipoUso === 'ambos' ? 'selected' : ''}>Ambos</option>
                    </select>
                </td>
                <td>
                    <button onclick="excluirEndereco('${endereco.id}')">Excluir</button>
                </td>
            `;
            tabela.appendChild(tr);
        });
    }

    btnSalvar.addEventListener('click', async () => {
        const dados = {
            nome: document.getElementById('nome-endereco').value.trim(),
            tipoResidencia: document.getElementById('tipo-residencia').value,
            tipoLogradouro: document.getElementById('tipo-logradouro').value,
            logradouro: document.getElementById('logradouro').value.trim(),
            numero: document.getElementById('numero').value.trim(),
            bairro: document.getElementById('bairro').value.trim(),
            cep: document.getElementById('cep').value.trim(),
            cidade: document.getElementById('cidade').value.trim(),
            estado: document.getElementById('estado').value.trim(),
            pais: document.getElementById('pais').value.trim(),
            tipoUso: document.getElementById('tipo-uso').value,
            observacao: document.getElementById('observacao').value.trim()
        };

        if (!dados.nome || !dados.tipoResidencia || !dados.tipoLogradouro || !dados.logradouro ||
            !dados.numero || !dados.bairro || !dados.cep || !dados.cidade ||
            !dados.estado || !dados.pais || !dados.tipoUso) {
            alert('Preencha todos os campos obrigatórios!');
            return;
        }

        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/enderecos`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dados)
            });
            if (!res.ok) throw new Error("Erro ao salvar endereço");
            await carregarEnderecos();
            form.reset();
        } catch (err) {
            console.error(err);
            alert("Erro ao salvar endereço.");
        }
    });

    window.excluirEndereco = async function (idEndereco) {
        if (!confirm('Deseja realmente excluir este endereço?')) return;
        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/enderecos/${idEndereco}`, {
                method: "DELETE"
            });
            if (!res.ok) throw new Error("Erro ao excluir endereço");
            await carregarEnderecos();
        } catch (err) {
            console.error(err);
            alert("Erro ao excluir endereço.");
        }
    };

    window.alterarTipoUso = async function (idEndereco, novoTipo) {
        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}/enderecos/${idEndereco}/tipo-uso`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ tipoUso: novoTipo })
            });
            if (!res.ok) throw new Error("Erro ao alterar tipo de uso");
            await carregarEnderecos();
        } catch (err) {
            console.error(err);
            alert("Erro ao alterar tipo de uso.");
        }
    };

    carregarEnderecos();
});