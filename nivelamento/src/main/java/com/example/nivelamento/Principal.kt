package com.example.nivelamento

import java.util.Scanner

//Inicialização
fun main(){

    //Exercício 01 - Curso Android - 2026/03/12
    class Pessoa(
        var nome: String = "",
        var idade: Int?,
        var email: String = ""
    ) {}

    val scn = Scanner(System.`in`, "UTF-8")
    val lstCadastro: MutableList<Pessoa> = mutableListOf()
    var opcao: Int = 0

    abstract class Crud {
        abstract fun cadastrar() : Boolean
        abstract fun listar()
        abstract fun pesquisar(email: String) : Pessoa?
        abstract fun alterar(email: String) : Boolean
        abstract fun remover(email: String)
        abstract fun validarEmail(email: String) : Boolean
        abstract fun validarNome(nome: String) : Boolean
    }

    class Acoes : Crud() {

        override fun cadastrar(): Boolean {
            println("<<<<< Cadastro de pessoas >>>>>")
            println("")
            print("Email: ")
            var email = scn.nextLine()
            while (pesquisar(email) != null && email != "sair") {
                println("Email já cadastrado! Digite sair para terminar ou digite outro email.")
                print("Email: ")
                email = scn.nextLine()
            }
            if (email == "sair") {
                return false
            }
            print("Nome: ")
            val nome = scn.nextLine()
            print("Idade ou ENTER para não informar: ")
            val idade = scn.nextLine()
            scn.nextLine()
            if (validarEmail(email) && validarNome(nome)) {
                lstCadastro.add(Pessoa(nome, idade.toIntOrNull(), email))
                println("Pessoa cadastrada com sucesso!")
                return true
            } else {
                println("Dados inválidos! - Verifique o e-mail e o nome.")
                return false
            }
        }

        override fun listar() {
            println("<<<<< Pessoas cadastradas >>>>>")
            println("")
            lstCadastro.forEach({
                println("Nome: ${it.nome}")
                println("Idade: ${it.idade ?: "Não informada"}")
                println("Email: ${it.email}")
                println("")
            })
            println("")
            println("<<<<< Fim da lista >>>>>")
        }

        override fun pesquisar(email: String): Pessoa? {
            return lstCadastro.find { it.email == email }
        }

        override fun alterar(email: String): Boolean {
            println("<<<<< Alteração de pessoas >>>>>")
            println("")
            val pessoa = pesquisar(email)
            if (pessoa != null) {
                print("Nome: ")
                val nome = scn.nextLine()
                print("Idade ou ENTER para não informar: ")
                val idade = scn.nextInt()
                scn.nextLine()
                if (validarEmail(email) && validarNome(nome)) {
                    pessoa.nome = nome
                    pessoa.idade = idade
                    println("Pessoa alterada com sucesso!")
                    return true
                } else {
                    println("Dados inválidos! - Verifique o e-mail e o nome.")
                    return false
                }
            } else {
                println("Pessoa não encontrada!")
                return false
            }
        }

        override fun remover(email: String) {
            if (lstCadastro.remove(pesquisar(email))) {
                println("Pessoa removida com sucesso!")
            } else {
                println("Pessoa não encontrada!")
            }
        }

        override fun validarEmail(email: String): Boolean {
            return email.contains("@") && email.contains(".") && email.length >= 5
        }

        override fun validarNome(nome: String): Boolean {
            return nome.length >= 3
        }

    }

    //Opções
    do {
        println("Bem-vindo ao cadastro de pessoas:")
        println("")
        println("1 - Cadastrar")
        println("2 - Listar")
        println("3 - Pesquisar")
        println("4 - Alterar")
        println("5 - Remover")
        println("6 - Finalizar progrma")
        println("")
        print("Escolha uma opção: ")
        opcao = scn.nextInt()
        println("*******************************************************")
        scn.nextLine()

        val crud = Acoes()

        when (opcao) {
            1 -> { crud.cadastrar() }
            2 -> { crud.listar() }
            3 -> {
                print("Digite o e-mail da pessoa que deseja pesquisar: ")
                val pessoa = crud.pesquisar(scn.nextLine())
                if (pessoa != null) {
                    println("Nome: ${pessoa.nome}")
                    println("Idade: ${pessoa.idade ?: "Não informada"}")
                    println("Email: ${pessoa.email}")
                    println("")
                } else {
                    println("Pessoa não encontrada!")
                }
            }
            4 -> {
                print("Digite o e-mail da pessoa que deseja alterar: ")
                crud.alterar(scn.nextLine())
            }
            5 -> {
                print("Digite o e-mail da pessoa que deseja remover: ")
                crud.remover(scn.nextLine())
            }
            else -> { println("Opção inválida!") }
        }

        if (opcao != 6) {
            println("Pressione ENTER para continuar...")
            scn.nextLine()
        }

    } while (opcao != 6)

}