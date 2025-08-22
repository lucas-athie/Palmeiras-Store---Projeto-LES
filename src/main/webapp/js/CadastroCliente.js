document.addEventListener("DOMContentLoaded", function () {
    const btnSalvar = document.getElementById("btnSalvar");

    btnSalvar.addEventListener("click", function () {
        const dados = {
            nome: document.getElementById("nome").value.trim(),
            cpf: document.getElementById("cpf").value.trim(),
            genero: document.getElementById("genero").value,
            senha: document.getElementById("senha").value,
            confirmarSenha: document.getElementById("confirmar-senha").value,
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
            observacao: document.getElementById("obs").value.trim()
        };

        for (let campo in dados) {
            if (campo !== "observacao" && campo !== "confirmarSenha" && dados[campo] === "") {
                alert(`O campo "${campo}" é obrigatório!`);
                return;
            }
        }

        if (dados.senha !== dados.confirmarSenha) {
            alert("As senhas não coincidem!");
            return;
        }

        delete dados.confirmarSenha;

        let clientes = JSON.parse(localStorage.getItem("clientes")) || [];
        clientes.push(dados);
        localStorage.setItem("clientes", JSON.stringify(clientes));

        localStorage.setItem("clienteAtual", JSON.stringify(dados));

        const enderecoPadrao = {
            nome: "Endereço Principal",
            tipoResidencia: dados.tipoEndereco,
            tipoLogradouro: dados.tipoLogradouro,
            logradouro: dados.logradouro,
            numero: dados.numero,
            bairro: dados.bairro,
            cep: dados.cep,
            cidade: dados.cidade,
            estado: dados.estado,
            pais: dados.pais,
            tipoUso: "ambos",
            observacao: dados.observacao
        };

        let enderecos = JSON.parse(localStorage.getItem("enderecos")) || [];
        enderecos.push(enderecoPadrao);
        localStorage.setItem("enderecos", JSON.stringify(enderecos));

        alert("Cliente e endereço cadastrados com sucesso!");
        window.location.href = "ListaClientes.html";
    });
});