document.addEventListener("DOMContentLoaded", function () {
    const btnSalvar = document.getElementById("btnSalvar");
    const btnCancelar = document.getElementById("btnCancelar");
    const btnExcluir = document.getElementById("btnExcluir");

    const clienteEditando = JSON.parse(localStorage.getItem("clienteEditando"));

    if (!clienteEditando) {
        alert("Nenhum cliente selecionado para edição.");
        window.location.href = "ListaClientes.html";
        return;
    }

    for (let campo in clienteEditando) {
        if (document.getElementById(campo)) {
            document.getElementById(campo).value = clienteEditando[campo];
        }
    }

    if (document.getElementById("ativo")) {
        document.getElementById("ativo").value = clienteEditando.ativo !== false ? "true" : "false";
    }

    btnSalvar.addEventListener("click", function () {
        const dadosAtualizados = {
            nome: document.getElementById("nome").value.trim(),
            cpf: document.getElementById("cpf").value.trim(),
            genero: document.getElementById("genero").value,
            senha: document.getElementById("senha").value,
            dataNascimento: document.getElementById("data-nascimento").value,
            tipoTelefone: document.getElementById("tipo-telefone").value,
            telefone: document.getElementById("telefone").value.trim(),
            email: document.getElementById("email").value.trim(),
            tipoEndereco: document.getElementById("tipo-endereco").value,
            tipoLogradouro: document.getElementById("tipo-logradouro").value,
            logradouro: document.getElementById("logradouro").value.trim(),
            numero: document.getElementById("numero").value.trim(),
            bairro: document.getElementById("bairro").value.trim(),
            cep: document.getElementById("cep").value.trim(),
            cidade: document.getElementById("cidade").value.trim(),
            estado: document.getElementById("estado").value.trim(),
            pais: document.getElementById("pais").value.trim(),
            observacao: document.getElementById("obs").value.trim(),
            ativo: document.getElementById("ativo").value === "true"
        };

        let clientes = JSON.parse(localStorage.getItem("clientes")) || [];
        const index = clientes.findIndex(c => c.cpf === clienteEditando.cpf);

        if (index !== -1) {
            clientes[index] = dadosAtualizados;
            localStorage.setItem("clientes", JSON.stringify(clientes));
            localStorage.removeItem("clienteEditando");
            alert("Cliente atualizado com sucesso!");
            window.location.href = "ListaClientes.html";
        } else {
            alert("Erro: cliente não encontrado na lista.");
        }
    });

    if (btnExcluir) {
        btnExcluir.addEventListener("click", function () {
            if (confirm("Tem certeza que deseja excluir este cliente?")) {
                let clientes = JSON.parse(localStorage.getItem("clientes")) || [];
                clientes = clientes.filter(c => c.cpf !== clienteEditando.cpf);
                localStorage.setItem("clientes", JSON.stringify(clientes));
                localStorage.removeItem("clienteEditando");
                alert("Cliente excluído com sucesso!");
                window.location.href = "ListaClientes.html";
            }
        });
    }
});