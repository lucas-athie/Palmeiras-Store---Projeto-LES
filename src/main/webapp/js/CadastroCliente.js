document.addEventListener("DOMContentLoaded", function () {
    const btnSalvar = document.getElementById("btnSalvar");

    btnSalvar.addEventListener("click", function () {
        const cpfFormatado = document.getElementById("cpf").value.trim().replace(/\D/g, '');

        const cliente = {
            nome: document.getElementById("nome").value.trim(),
            cpf: cpfFormatado,
            genero: document.getElementById("genero").value,
            senha: document.getElementById("senha").value,
            confirmarSenha: document.getElementById("confirmar-senha").value,
            dataNascimento: document.getElementById("data-nascimento").value,
            tipoTelefone: document.getElementById("tipo-telefone").value,
            telefone: document.getElementById("telefone").value.trim(),
            email: document.getElementById("email").value.trim(),
            observacao: document.getElementById("obs").value.trim()
        };

        // Validação básica
        for (let campo in cliente) {
            if (campo !== "observacao" && campo !== "confirmarSenha" && cliente[campo] === "") {
                alert(`O campo "${campo}" é obrigatório!`);
                return;
            }
        }
        if (cliente.senha !== cliente.confirmarSenha) {
            alert("As senhas não coincidem!");
            return;
        }
        delete cliente.confirmarSenha;

        // Endereço de entrega
        const enderecoEntrega = {
            id: Date.now() + "_entrega",
            cpfCliente: cpfFormatado,
            nome: "Endereço de Entrega",
            tipoResidencia: document.getElementById("entrega-tipo-endereco").value,
            tipoLogradouro: document.getElementById("entrega-tipo-logradouro").value,
            logradouro: document.getElementById("entrega-logradouro").value.trim(),
            numero: document.getElementById("entrega-numero").value.trim(),
            bairro: document.getElementById("entrega-bairro").value.trim(),
            cep: document.getElementById("entrega-cep").value.trim(),
            cidade: document.getElementById("entrega-cidade").value.trim(),
            estado: document.getElementById("entrega-estado").value.trim(),
            pais: document.getElementById("entrega-pais").value.trim(),
            tipoUso: "entrega",
            observacao: ""
        };

        // Endereço de cobrança
        const enderecoCobranca = {
            id: Date.now() + "_cobranca",
            cpfCliente: cpfFormatado,
            nome: "Endereço de Cobrança",
            tipoResidencia: document.getElementById("cobranca-tipo-endereco").value,
            tipoLogradouro: document.getElementById("cobranca-tipo-logradouro").value,
            logradouro: document.getElementById("cobranca-logradouro").value.trim(),
            numero: document.getElementById("cobranca-numero").value.trim(),
            bairro: document.getElementById("cobranca-bairro").value.trim(),
            cep: document.getElementById("cobranca-cep").value.trim(),
            cidade: document.getElementById("cobranca-cidade").value.trim(),
            estado: document.getElementById("cobranca-estado").value.trim(),
            pais: document.getElementById("cobranca-pais").value.trim(),
            tipoUso: "cobranca",
            observacao: ""
        };

        // Cartão
        const cartao = {
            id: Date.now() + "_cartao",
            cpfCliente: cpfFormatado,
            numero: document.getElementById("numero-cartao").value.trim(),
            nome: document.getElementById("nome-cartao").value.trim(),
            bandeira: document.getElementById("bandeira-cartao").value,
            codigoSeguranca: document.getElementById("codigo-seguranca").value.trim(),
            preferencial: true
        };

        // Salvar no localStorage
        let clientes = JSON.parse(localStorage.getItem("clientes") || "[]");
        clientes.push(cliente);
        localStorage.setItem("clientes", JSON.stringify(clientes));

        // Define cliente atual
        localStorage.setItem("clienteAtual", JSON.stringify(cliente));

        // Salva endereços
        let enderecos = JSON.parse(localStorage.getItem("enderecos") || "[]");
        enderecos.push(enderecoEntrega, enderecoCobranca);
        localStorage.setItem("enderecos", JSON.stringify(enderecos));

        // Salva cartão
        let cartoes = JSON.parse(localStorage.getItem("cartoes") || "[]");
        cartoes.push(cartao);
        localStorage.setItem("cartoes", JSON.stringify(cartoes));

        alert("Cliente, endereços e cartão cadastrados com sucesso!");
        window.location.href = "ListaLoja.html";
    });
});