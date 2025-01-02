package com.example.fitnesstracker

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.databinding.ActivityImcActivityBinding
import com.example.fitnesstracker.databinding.ActivityMainBinding
import com.example.fitnesstracker.model.Calc
import com.example.fitnesstracker.model.CalcDao


class ImcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImcActivityBinding
    private lateinit var inputPeso: EditText
    private lateinit var inputAltura: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImcActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()

        inputPeso = binding.entradaPeso
        inputAltura = binding.entradaAltura


        val buttonCalcular = binding.buttonCalcular
        val buttonSair = binding.buttonSair

        // ADICIONANDO TOOLBAR PARA QUE O MENU SUSPENSO POSSA APARECER NA TELA.
        val toolbar = binding.toolbarImc
        setSupportActionBar(toolbar)

        buttonCalcular.setOnClickListener {
            if (!validate()){
                title = getString(R.string.field_messages)
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage("Todos os campos devem ser maiores que ZERO")
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        Log.i("Log função validate(): ", "Formulario preenchido incorretamente")
                    }
                    .create()
                    .show()
                return@setOnClickListener

            }
            val peso = inputPeso.text.toString().toInt()
            val altura = inputAltura.text.toString().toInt()
            val resultado = calcular(peso,altura)

            // ESSA INFORMÇÃO APARECERÁ APENAS NO LOG
            val resultadoString = String.format("%.2f",resultado)
            Log.i("Log função validate(): ", "Resultado: ${resultadoString}")

            // retorno da função que diz o grau do IMC
            val retornoImcResponse = imcResponse(resultado)

            // TITULO QUE SERA MOSTRADO DENTRO DO ALERTDIALOG.
            // CHAMA O RECURSO NO XML E ATRIBUI A ELE O PAREMETRO "resultado" PARA QUE SEJA INCLUSO
            // NA FORMATACAÇÃO FINAL: "%1$.2F"
            val title = getString(R.string.imc_response, resultado)

            // é possivel chamar o builder do alertdialog e usar o ".set" DIRETAMENTE SEM PRECISAR
            // DE VARIAVEIS . eX: .setTitle, .setMessage, .setPositiveButton
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(retornoImcResponse)
                .setPositiveButton(R.string.text_sair) { dialog, which ->
                //IMPLEMENTAÇÃO DA AÇÃO DO BOTAO DO ALERT DIALOG
                    Log.i("LOG:","SUCESSO NO PROCESSO!")
                }
                .setNegativeButton( R.string.save) {dialog, which ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = resultado))
                    }.start()


                }
                .create()
                .show()
            }

        buttonSair.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    // Estou garantindo pra função que o inteiro retornado é obrigatoriamente um recurso do
    // "string.xml", graças a annotation "@StringRes"
    // Mesmo a função retornando um inteiro, deve ser um inteiro que existe nos recursos(XML)


    // FUNÇÃO QUE INFLA O LAYOUT DO MENU NA TELA
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // FUNÇÃO QUE ESCUTA OS EVENTOS DE DENTRO DO MENU
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            finish()
            openListActivity()
            Log.i("MENU eventos: ", "CLICOU NO MENU")

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity(){
        val intent = Intent(this@ImcActivity, ListCalcActivity::class.java)
        intent.putExtra("type","imc")
        startActivity(intent)
    }

    @StringRes
    private fun imcResponse(imc: Double) : Int {
        return when {
            imc < 15.0 ->  R.string.imc_peso_muito_abaixo
            imc < 18.5 ->  R.string.imc_abaixo_do_peso
            imc < 24.9 ->  R.string.imc_peso_normal
            imc < 29.9 ->  R.string.imc_peso_sobrepeso
            imc < 34.9 ->  R.string.imc_peso_obesidade1
            imc < 39.9 ->  R.string.imc_peso_obesidade2
            else ->  R.string.imc_peso_obesidade3
        }
    }

    //RESTRINGIR A INSEÇÃO DE VALRORES NULOS / VAZIOS
    //NÃO PODE COMERÇAR / INSERIR "0 (ZERO)"
    private fun validate(): Boolean{

        val pesoPreenchido = inputPeso.text.toString().isNotEmpty()
        val alturaPreenchida = inputAltura.text.toString().isNotEmpty()
        val pesoInitWithZero = inputPeso.text.toString().startsWith("0")
        val alturaInitWithZero = inputAltura.text.toString().startsWith("0")

        // RETORNA VERDADEIRO SE TODA A EXPRESSÃO FOR VERDADEIRA/RETORNA FALSE CASO ALGUMA CONDIÇÃO
        // SEJA FALSA
        return (pesoPreenchido && alturaPreenchida
                && !pesoInitWithZero
                && !alturaInitWithZero)
    }


    private fun calcular(peso: Int, altura: Int): Double{
        return peso / ((altura/100.0) * (altura/100.0))
    }


    private fun setupWindow(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}