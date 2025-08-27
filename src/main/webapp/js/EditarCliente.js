document.addEventListener("DOMContentLoaded", async function () {
    const btnSalvar = document.getElementById("btnSalvar");
    const btnCancelar = document.getElementById("btnCancelar");
    const btnExcluir = document.getElementById("btnExcluir");
    const btnEnderecos = document.getElementById("btnEnderecos");
    const API_BASE = "http://localhost:5500/api"; // ajuste conforme o back

    let clienteEditando = JSON.parse(localStorage.getItem("clienteEditando"));

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

    const cpfCliente = clienteEditando.cpf?.replace(/\D/g, '');

    try {
        const res = await fetch(`${API_BASE}/clientes/${cpfCliente}`);
        if (!res.ok) throw new Error("Erro ao buscar cliente");
        const clienteBack = await res.json();

        for (let campo in clienteBack) {
            if (document.getElementById(campo)) {
                document.getElementById(campo).value = clienteBack[campo];
            }
        }
        if (document.getElementById("ativo")) {
            document.getElementById("ativo").value = clienteBack.ativo !== false ? "true" : "false";
        }
    } catch (err) {
        console.error(err);
        alert("Erro ao carregar dados do cliente.");
        return;
    }

    btnSalvar.addEventListener("click", async function () {
        const dadosAtualizados = {
            nome: document.getElementById("nome").value.trim(),
            cpf: document.getElementById("cpf").value.trim().replace(/\D/g, ''),
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

        try {
            const res = await fetch(`${API_BASE}/clientes/${cpfCliente}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dadosAtualizados)
            });
            if (!res.ok) throw new Error("Erro ao salvar alterações");

            localStorage.setItem("clienteAtual", JSON.stringify(dadosAtualizados));
            localStorage.removeItem("clienteEditando");

            alert("Cliente atualizado com sucesso!");
            window.location.href = "CadastroEndereco.html";
        } catch (err) {
            console.error(err);
            alert("Erro ao salvar cliente.");
        }
    });

    if (btnExcluir) {
        btnExcluir.addEventListener("click", async function () {
            if (!confirm("Tem certeza que deseja excluir este cliente?")) return;
            try {
                const res = await fetch(`${API_BASE}/clientes/${cpfCliente}`, {
                    method: "DELETE"
                });
                if (!res.ok) throw new Error("Erro ao excluir cliente");
                localStorage.removeItem("clienteEditando");
                alert("Cliente excluído com sucesso!");
                window.location.href = "ListaClientes.html";
            } catch (err) {
                console.error(err);
                alert("Erro ao excluir cliente.");
            }
        });
    }

    if (btnEnderecos) {
        btnEnderecos.addEventListener("click", function () {
            localStorage.setItem("clienteAtual", JSON.stringify(clienteEditando));
            window.location.href = "CadastroEndereco.html";
        });
    }
});