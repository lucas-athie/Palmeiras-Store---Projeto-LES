document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('form-endereco');
    const tabela = document.querySelector('#tabela-enderecos tbody');
    const btnSalvar = document.getElementById('btnSalvar');

    let clienteAtual = JSON.parse(localStorage.getItem('clienteAtual'));

    // Fallback: se não houver clienteAtual, tenta clienteEditando
    if (!clienteAtual) {
        const clienteEditando = JSON.parse(localStorage.getItem('clienteEditando'));
        if (clienteEditando) {
            clienteAtual = clienteEditando;
            localStorage.setItem('clienteAtual', JSON.stringify(clienteEditando));
        }
    }

    if (!clienteAtual) {
        alert("Nenhum cliente selecionado.");
        window.location.href = "ListaClientes.html";
        return;
    }

    const cpfCliente = clienteAtual.cpf?.replace(/\D/g, '');

    console.log("📌 Cliente atual:", clienteAtual);
    console.log("📌 CPF usado para filtro:", cpfCliente);
    console.log("📌 Todos os endereços no localStorage:", JSON.parse(localStorage.getItem('enderecos') || "[]"));

    function getEnderecos() {
        const todos = JSON.parse(localStorage.getItem('enderecos') || "[]");
        const filtrados = todos.filter(e => e.cpfCliente.replace(/\D/g, '') === cpfCliente);
        console.log(`📌 Endereços filtrados para CPF ${cpfCliente}:`, filtrados);
        return filtrados;
    }

    function setEnderecos(enderecosCliente) {
        let todos = JSON.parse(localStorage.getItem('enderecos') || "[]");
        todos = todos.filter(e => e.cpfCliente.replace(/\D/g, '') !== cpfCliente);
        todos.push(...enderecosCliente);
        localStorage.setItem('enderecos', JSON.stringify(todos));
        console.log("📌 Endereços salvos no localStorage:", todos);
    }

    function carregarEnderecos() {
        const enderecos = getEnderecos();
        renderTabela(enderecos);
    }

    function renderTabela(enderecos) {
        tabela.innerHTML = "";

        if (!enderecos.length) {
            const row = tabela.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 10;
            cell.textContent = "Nenhum endereço cadastrado.";
            cell.style.textAlign = "center";
            return;
        }

        enderecos.forEach(endereco => {
            const row = tabela.insertRow();

            row.insertCell().textContent = endereco.nome;
            row.insertCell().textContent = endereco.tipoResidencia;
            row.insertCell().textContent = `${endereco.tipoLogradouro} ${endereco.logradouro}`;
            row.insertCell().textContent = endereco.numero;
            row.insertCell().textContent = endereco.bairro;
            row.insertCell().textContent = endereco.cidade;
            row.insertCell().textContent = endereco.estado;
            row.insertCell().textContent = endereco.cep;

            // Tipo de uso (select)
            const usoCell = row.insertCell();
            const select = document.createElement('select');
            ["entrega", "cobranca", "ambos"].forEach(tipo => {
                const option = document.createElement('option');
                option.value = tipo;
                option.textContent = tipo.charAt(0).toUpperCase() + tipo.slice(1);
                if (endereco.tipoUso === tipo) option.selected = true;
                select.appendChild(option);
            });
            select.addEventListener('change', () => alterarTipoUso(endereco.id, select.value));
            usoCell.appendChild(select);

            // Botão excluir
            const acaoCell = row.insertCell();
            const btnExcluir = document.createElement('button');
            btnExcluir.textContent = "Excluir";
            btnExcluir.addEventListener('click', () => excluirEndereco(endereco.id));
            acaoCell.appendChild(btnExcluir);
        });
    }

    btnSalvar.addEventListener('click', () => {
        const dados = {
            id: Date.now().toString(),
            cpfCliente: cpfCliente,
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

        // Validação
        for (let key in dados) {
            if (key !== "observacao" && !dados[key]) {
                alert(`Preencha o campo: ${key}`);
                return;
            }
        }

        const enderecosCliente = getEnderecos();
        enderecosCliente.push(dados);
        setEnderecos(enderecosCliente);

        carregarEnderecos();
        form.reset();
    });

    function excluirEndereco(idEndereco) {
        if (!confirm('Deseja realmente excluir este endereço?')) return;
        let enderecosCliente = getEnderecos();
        enderecosCliente = enderecosCliente.filter(e => e.id !== idEndereco);
        setEnderecos(enderecosCliente);
        carregarEnderecos();
    }

    function alterarTipoUso(idEndereco, novoTipo) {
        let enderecosCliente = getEnderecos();
        const idx = enderecosCliente.findIndex(e => e.id === idEndereco);
        if (idx >= 0) {
            enderecosCliente[idx].tipoUso = novoTipo;
            setEnderecos(enderecosCliente);
            carregarEnderecos();
        }
    }

    carregarEnderecos();
});