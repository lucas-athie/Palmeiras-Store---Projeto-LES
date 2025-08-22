const form = document.getElementById('form-endereco');
const tabela = document.querySelector('#tabela-enderecos tbody');

const clienteAtual = JSON.parse(localStorage.getItem('clienteAtual'));
const cpfPadronizado = clienteAtual?.cpf?.replace(/\D/g, '');

let enderecos = (JSON.parse(localStorage.getItem('enderecos')) || [])
    .filter(e => e.cpfCliente?.replace(/\D/g, '') === cpfPadronizado);

document.getElementById('btnSalvar').addEventListener('click', () => {
    const dados = {
        cpfCliente: cpfPadronizado,
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
        !dados.numero || !dados.bairro || !dados.cep || !dados.cidade || !dados.estado ||
        !dados.pais || !dados.tipoUso) {
        alert('Preencha todos os campos obrigatórios!');
        return;
    }

    let todosEnderecos = JSON.parse(localStorage.getItem('enderecos')) || [];
    todosEnderecos.push(dados);
    localStorage.setItem('enderecos', JSON.stringify(todosEnderecos));

    enderecos.push(dados);
    atualizarTabela();

    form.reset();
});

function atualizarTabela() {
    tabela.innerHTML = '';
    enderecos.forEach((endereco, index) => {
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
                <select onchange="alterarTipoUso(${index}, this.value)">
                    <option value="entrega" ${endereco.tipoUso === 'entrega' ? 'selected' : ''}>Entrega</option>
                    <option value="cobranca" ${endereco.tipoUso === 'cobranca' ? 'selected' : ''}>Cobrança</option>
                    <option value="ambos" ${endereco.tipoUso === 'ambos' ? 'selected' : ''}>Ambos</option>
                </select>
            </td>
            <td>
                <button onclick="excluirEndereco(${index})">Excluir</button>
            </td>
        `;

        tabela.appendChild(tr);
    });
}

function excluirEndereco(index) {
    if (confirm('Deseja realmente excluir este endereço?')) {
        const enderecoRemovido = enderecos.splice(index, 1)[0];

        let todosEnderecos = JSON.parse(localStorage.getItem('enderecos')) || [];
        todosEnderecos = todosEnderecos.filter(e =>
            !(e.cpfCliente?.replace(/\D/g, '') === enderecoRemovido.cpfCliente?.replace(/\D/g, '') &&
              e.cep === enderecoRemovido.cep &&
              e.numero === enderecoRemovido.numero)
        );

        localStorage.setItem('enderecos', JSON.stringify(todosEnderecos));
        atualizarTabela();
    }
}

function alterarTipoUso(index, novoTipo) {
    enderecos[index].tipoUso = novoTipo;

    let todosEnderecos = JSON.parse(localStorage.getItem('enderecos')) || [];
    const idx = todosEnderecos.findIndex(e =>
        e.cpfCliente?.replace(/\D/g, '') === enderecos[index].cpfCliente?.replace(/\D/g, '') &&
        e.cep === enderecos[index].cep &&
        e.numero === enderecos[index].numero
    );
    if (idx > -1) {
        todosEnderecos[idx].tipoUso = novoTipo;
        localStorage.setItem('enderecos', JSON.stringify(todosEnderecos));
    }
}

atualizarTabela();