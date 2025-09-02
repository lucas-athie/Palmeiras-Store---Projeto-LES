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

    function getCartoes() {
        return JSON.parse(localStorage.getItem('cartoes') || "[]")
            .filter(c => c.cpfCliente.replace(/\D/g, '') === cpfCliente);
    }

    function setCartoes(cartoesCliente) {
        let todos = JSON.parse(localStorage.getItem('cartoes') || "[]");
        todos = todos.filter(c => c.cpfCliente.replace(/\D/g, '') !== cpfCliente);
        todos.push(...cartoesCliente);
        localStorage.setItem('cartoes', JSON.stringify(todos));
    }

    function carregarCartoes() {
        const cartoes = getCartoes();
        renderTabela(cartoes);
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

    btnSalvar.addEventListener('click', () => {
        const dados = {
            id: Date.now().toString(),
            cpfCliente: cpfCliente,
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

        let cartoesCliente = getCartoes();

        if (dados.preferencial) {
            cartoesCliente.forEach(c => c.preferencial = false);
        }

        cartoesCliente.push(dados);
        setCartoes(cartoesCliente);

        carregarCartoes();
        form.reset();
    });

    window.excluirCartao = function (idCartao) {
        if (!confirm("Deseja realmente excluir este cartão?")) return;
        let cartoesCliente = getCartoes();
        cartoesCliente = cartoesCliente.filter(c => c.id !== idCartao);
        setCartoes(cartoesCliente);
        carregarCartoes();
    };

    window.definirPreferencial = function (idCartao) {
        let cartoesCliente = getCartoes();
        cartoesCliente.forEach(c => c.preferencial = (c.id === idCartao));
        setCartoes(cartoesCliente);
        carregarCartoes();
    };

    carregarCartoes();
});