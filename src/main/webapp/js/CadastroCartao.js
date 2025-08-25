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

    let cartoes = JSON.parse(localStorage.getItem('cartoes')) || [];
    cartoes = cartoes.filter(c => c.cpfCliente?.replace(/\D/g, '') === cpfCliente);

    btnSalvar.addEventListener('click', () => {
        const dados = {
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

        if (dados.preferencial) {
            let todosCartoes = JSON.parse(localStorage.getItem('cartoes')) || [];
            todosCartoes = todosCartoes.map(c => {
                if (c.cpfCliente?.replace(/\D/g, '') === cpfCliente) {
                    return { ...c, preferencial: false };
                }
                return c;
            });
            localStorage.setItem('cartoes', JSON.stringify(todosCartoes));
        }

        let todosCartoes = JSON.parse(localStorage.getItem('cartoes')) || [];
        todosCartoes.push(dados);
        localStorage.setItem('cartoes', JSON.stringify(todosCartoes));

        cartoes.push(dados);
        atualizarTabela();

        form.reset();
    });

    function atualizarTabela() {
        tabela.innerHTML = '';
        cartoes.forEach((cartao, index) => {
            const tr = document.createElement('tr');

            tr.innerHTML = `
                <td>**** **** **** ${cartao.numero.slice(-4)}</td>
                <td>${cartao.nome}</td>
                <td>${cartao.bandeira}</td>
                <td>${cartao.preferencial ? '<span class="preferencial">Sim</span>' : 'Não'}</td>
                <td>
                    <button onclick="definirPreferencial(${index})">Preferencial</button>
                    <button onclick="excluirCartao(${index})">Excluir</button>
                </td>
            `;

            tabela.appendChild(tr);
        });
    }

    window.excluirCartao = function (index) {
        const cartaoRemovido = cartoes.splice(index, 1)[0];

        let todosCartoes = JSON.parse(localStorage.getItem('cartoes')) || [];
        todosCartoes = todosCartoes.filter(c =>
            !(c.cpfCliente?.replace(/\D/g, '') === cpfCliente &&
              c.numero === cartaoRemovido.numero)
        );

        localStorage.setItem('cartoes', JSON.stringify(todosCartoes));
        atualizarTabela();
    };

    window.definirPreferencial = function (index) {
        cartoes = cartoes.map((c, i) => ({ ...c, preferencial: i === index }));

        let todosCartoes = JSON.parse(localStorage.getItem('cartoes')) || [];
        todosCartoes = todosCartoes.map(c => {
            if (c.cpfCliente?.replace(/\D/g, '') === cpfCliente) {
                return { ...c, preferencial: c.numero === cartoes[index].numero };
            }
            return c;
        });

        localStorage.setItem('cartoes', JSON.stringify(todosCartoes));
        atualizarTabela();
    };

    atualizarTabela();
});