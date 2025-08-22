const form = document.getElementById('form-endereco');
const tabela = document.querySelector('#tabela-enderecos tbody');
let enderecos = JSON.parse(localStorage.getItem('enderecos')) || [];

document.getElementById('btnSalvar').addEventListener('click', () => {
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
        !dados.numero || !dados.bairro || !dados.cep || !dados.cidade || !dados.estado ||
        !dados.pais || !dados.tipoUso) {
        alert('Preencha todos os campos obrigatórios!');
        return;
    }

    enderecos.push(dados);
    salvarLocal();
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
                <button onclick="editarEndereco(${index})">Editar</button>
                <button onclick="excluirEndereco(${index})">Excluir</button>
            </td>
        `;

        tabela.appendChild(tr);
    });
}

function excluirEndereco(index) {
    if (confirm('Deseja realmente excluir este endereço?')) {
        enderecos.splice(index, 1);
        salvarLocal();
        atualizarTabela();
    }
}

function editarEndereco(index) {
    const e = enderecos[index];
    document.getElementById('nome-endereco').value = e.nome;
    document.getElementById('tipo-residencia').value = e.tipoResidencia;
    document.getElementById('tipo-logradouro').value = e.tipoLogradouro;
    document.getElementById('logradouro').value = e.logradouro;
    document.getElementById('numero').value = e.numero;
    document.getElementById('bairro').value = e.bairro;
    document.getElementById('cep').value = e.cep;
    document.getElementById('cidade').value = e.cidade;
    document.getElementById('estado').value = e.estado;
    document.getElementById('pais').value = e.pais;
    document.getElementById('tipo-uso').value = e.tipoUso;
    document.getElementById('observacao').value = e.observacao;

    enderecos.splice(index, 1);
    salvarLocal();
    atualizarTabela();
}

function alterarTipoUso(index, novoTipo) {
    enderecos[index].tipoUso = novoTipo;
    salvarLocal();
}

function salvarLocal() {
    localStorage.setItem('enderecos', JSON.stringify(enderecos));
}

atualizarTabela();