package com.example.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.*
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.databinding.ActivityTmbBinding
import com.example.fitnesstracker.model.Calc

class TmbActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTmbBinding
    private lateinit var lifestyle : AutoCompleteTextView
    private lateinit var inputPeso: EditText
    private lateinit var inputAltura: EditText
    private lateinit var inputIdade: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTmbBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()

        inputPeso = binding.entradaPesoTmb
        inputAltura = binding.entradaAltTmb
        inputIdade = binding.entradaIdadeTmb

        val buttonCalcular = binding.buttonCalcularTmb
        val buttonSair = binding.buttonSairTmb

        // ADICIONANDO TOOLBAR PARA QUE O MENU SUSPENSO POSSA APARECER NA TELA.
        val toolbar = binding.toolbarTmb
        setSupportActionBar(toolbar)

        //  BUTAO DE CALCULAR O TMB
        buttonCalcular.setOnClickListener {
            if (!validate()) {
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
            val idade = inputIdade.text.toString().toInt()

            val resultado = calculateTmb(peso,altura,idade)
            val responseTmb = tmbRequest(resultado)

            AlertDialog.Builder(this)
                .setMessage(getString(R.string.tmb_response,responseTmb))
                .setPositiveButton(getString(R.string.text_sair)) { dialog, which ->
                }
                .setNegativeButton( R.string.save) {dialog, which ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "tmb", res = responseTmb))

                    }.start()
                }
                .create()
                .show()

        }


        // BOTAO DE SAIR DA TELA DO TMB
        buttonSair.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        //AUTO COMPLETE SELECT
        lifestyle = binding.autoLifestyle
        val items = resources.getStringArray(R.array.tbm_lifestyle)
        lifestyle.setText(items.first())

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items )
        lifestyle.setAdapter(adapter)


    }

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
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", "tmb")
        startActivity(intent)
    }

    private fun tmbRequest(tmb: Double) : Double {
        val items = resources.getStringArray(R.array.tbm_lifestyle)
        return when {
            lifestyle.text.toString() == items [0] ->  tmb * 1.2
            lifestyle.text.toString() == items [1] ->  tmb * 1.375
            lifestyle.text.toString() == items [2] ->  tmb * 1.55
            lifestyle.text.toString() == items [3] ->  tmb * 1.725
            lifestyle.text.toString() == items [4] ->  tmb * 1.9
            else -> 0.0
        }
    }

    private fun calculateTmb(peso: Int, altura: Int, idade: Int) : Double {
        return 66 + (13.8 * peso) + (5 * altura) - (6.8* idade)
    }

    private fun validate(): Boolean{

        val pesoPreenchido = inputPeso.text.toString().isNotEmpty()
        val alturaPreenchida = inputAltura.text.toString().isNotEmpty()
        val idadePreenchida = inputAltura.text.toString().isNotEmpty()
        val pesoInitWithZero = inputPeso.text.toString().startsWith("0")
        val alturaInitWithZero = inputAltura.text.toString().startsWith("0")
        val idadeInitWithZero = inputAltura.text.toString().startsWith("0")

        // RETORNA VERDADEIRO SE TODA A EXPRESSÃO FOR VERDADEIRA/RETORNA FALSE CASO ALGUMA CONDIÇÃO
        // SEJA FALSA
        return (pesoPreenchido && alturaPreenchida && idadePreenchida
                && !pesoInitWithZero
                && !alturaInitWithZero
                && !idadeInitWithZero)
    }

    private fun setupWindow(){
        setOnApplyWindowInsetsListener(findViewById(R.id.main_tmb)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}