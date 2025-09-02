document.addEventListener("DOMContentLoaded", function () {
    const btnSalvar = document.getElementById("btnSalvar");
    const btnCancelar = document.getElementById("btnCancelar");
    const btnExcluir = document.getElementById("btnExcluir");
    const btnEnderecos = document.getElementById("btnEnderecos");
    const btnCartao = document.getElementById("btnCartao");

    let clienteEditando = JSON.parse(localStorage.getItem("clienteEditando"));

    // Se não houver clienteEditando, tenta usar clienteAtual
    if (!clienteEditando) {
        const clienteAtual = JSON.parse(localStorage.getItem("clienteAtual"));
        if (clienteAtual) {
            clienteEditando = clienteAtual;
            localStorage.setItem("clienteEditando", JSON.stringify(clienteAtual));
        }
    }

    if (!clienteEditando) {
        alert("Nenhum cliente selecionado para edição.");
        window.location.href = "ListaClientes.html";
        return;
    }

    console.log("Cliente editando:", clienteEditando);

    // Preenche o formulário com os dados do cliente
    for (let campo in clienteEditando) {
        const input = document.getElementById(campo);
        if (input) {
            input.value = clienteEditando[campo];
        }
    }
    if (document.getElementById("ativo")) {
        document.getElementById("ativo").value = clienteEditando.ativo !== false ? "true" : "false";
    }

    // Salvar alterações
    btnSalvar.addEventListener("click", function () {
        const dadosAtualizados = {
            nome: document.getElementById("nome").value.trim(),
            cpf: document.getElementById("cpf").value.trim().replace(/\D/g, ''),
            genero: document.getElementById("genero").value,
            senha: document.getElementById("senha").value,
            dataNascimento: document.getElementById("data-nascimento").value,
            tipoTelefone: document.getElementById("tipo-telefone").value,
            telefone: document.getElementById("telefone").value.trim(),
            email: document.getElementById("email").value.trim(),
            observacao: document.getElementById("obs") ? document.getElementById("obs").value.trim() : "",
            ativo: document.getElementById("ativo") ? document.getElementById("ativo").value === "true" : true
        };

        let clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
        const index = clientes.findIndex(c => c.cpf.replace(/\D/g, '') === clienteEditando.cpf.replace(/\D/g, ''));

        if (index !== -1) {
            clientes[index] = dadosAtualizados;
            localStorage.setItem("clientes", JSON.stringify(clientes));
            localStorage.setItem("clienteAtual", JSON.stringify(dadosAtualizados)); // garante atualização
            localStorage.removeItem("clienteEditando");
            alert("Cliente atualizado com sucesso!");
            window.location.href = "CadastroEndereco.html";
        } else {
            alert("Erro: cliente não encontrado na lista.");
        }
    });

    // Excluir cliente
    if (btnExcluir) {
        btnExcluir.addEventListener("click", function () {
            if (!confirm("Tem certeza que deseja excluir este cliente?")) return;
            let clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
            clientes = clientes.filter(c => c.cpf.replace(/\D/g, '') !== clienteEditando.cpf.replace(/\D/g, ''));
            localStorage.setItem("clientes", JSON.stringify(clientes));
            localStorage.removeItem("clienteEditando");
            alert("Cliente excluído com sucesso!");
            window.location.href = "ListaClientes.html";
        });
    }

    // Ir para endereços
    if (btnEnderecos) {
        btnEnderecos.addEventListener("click", function () {
            localStorage.setItem("clienteAtual", JSON.stringify(clienteEditando));
            localStorage.setItem("clienteEditando", JSON.stringify(clienteEditando));
            console.log("Indo para CadastroEndereco com clienteAtual:", clienteEditando);
            window.location.href = "CadastroEndereco.html";
        });
    }

    // Ir para cartões
    if (btnCartao) {
        btnCartao.addEventListener("click", function () {
            localStorage.setItem("clienteAtual", JSON.stringify(clienteEditando));
            localStorage.setItem("clienteEditando", JSON.stringify(clienteEditando));
            console.log("Indo para CadastroCartao com clienteAtual:", clienteEditando);
            window.location.href = "CadastroCartao.html";
        });
    }
});