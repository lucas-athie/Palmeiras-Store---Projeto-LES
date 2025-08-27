document.addEventListener("DOMContentLoaded", function () {
    const btnSalvar = document.getElementById("btnSalvar");

    const API_BASE = "http://localhost:5500/api"; // ajuste para o back depois

    btnSalvar.addEventListener("click", async function () {
        const cpfFormatado = document.getElementById("cpf").value.trim().replace(/\D/g, '');

        const dados = {
            nome: document.getElementById("nome").value.trim(),
            cpf: cpfFormatado,
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

        try {
            const resCliente = await fetch(`${API_BASE}/clientes`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dados)
            });

            if (!resCliente.ok) throw new Error("Erro ao cadastrar cliente");

            localStorage.setItem("clienteAtual", JSON.stringify(dados));

            const enderecoPadrao = {
                cpfCliente: cpfFormatado,
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

            const resEndereco = await fetch(`${API_BASE}/clientes/${cpfFormatado}/enderecos`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(enderecoPadrao)
            });

            if (!resEndereco.ok) throw new Error("Erro ao cadastrar endereço padrão");

            alert("Cliente e endereço cadastrados com sucesso!");
            window.location.href = "ListaClientes.html";

        } catch (err) {
            console.error(err);
            alert("Ocorreu um erro ao salvar o cliente.");
        }
    });
});