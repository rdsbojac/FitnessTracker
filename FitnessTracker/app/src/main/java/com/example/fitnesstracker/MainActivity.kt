package com.example.fitnesstracker


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()
        criarListaComItems()


        /*btnImc.setOnClickListener {
            Log.i("a", "CLICADO!")

            //"this" significa que "essa" atividade atual tem a intenção de executar
            // a "ImcActivity::class.java" e "starActivity" abre definitivamente a tela
            val i = Intent(this, ImcActivity::class.java)
            startActivity(i)

        }*/
    }

    private fun criarListaComItems(){
        val mainItems = mutableListOf<MainItem>()

        // SE EU QUISER QUE TODOS OS ITEMS QUE SERÃO ADICIONADOS A RECYCLERVIEW TENHAM A MESMA COR
        // POSSO DEFINIR O BACKGROUND TINT DIRETO NO XML. LOGO, POSSO REMOVER O PARAMETRO DE COR
        // DA DATA CLASS E DA ESTANCIAÇÃO DO ITEM ESPECIFICO
        mainItems.add(
            MainItem(id = 1,
                textStringId = R.string.text_imc,
                drawableId = R.drawable.baseline_assignment_24,
                // color.BLACK (REMOVIDO)
            )
        )

        mainItems.add(
            MainItem(id = 2,
                textStringId = R.string.text_tmb,
                drawableId = R.drawable.baseline_account_box_24,
            )
        )

        mainItems.add(
            MainItem(id = 3,
                textStringId = R.string.string_config,
                drawableId = R.drawable.baseline_build_24,
            )
        )

        mainItems.add(
            MainItem(id = 4,
                textStringId = R.string.string_sair,
                drawableId = R.drawable.baseline_arrow_back_24,
            )
        )

        // 1. LAYOUT XML
        // 2. ONDE A RECYVLER VIEW VAI APARECER(TELA PRINCIPAL, TELA CHEIA)
        // 3. LOGICA - CONECTAR O XML DA CELULA DENTRO DA RECYVLERVIEW + A SUA QNTD DE ELEMENTOS
        // DINAMICOS
        val adapter = MainAdapter(mainItems, { id ->

            when (id){
                1 -> {
                    val intent = Intent(this@MainActivity, ImcActivity::class.java)
                    startActivity(intent)
                }

                2 -> {
                    val intent = Intent(this@MainActivity, TmbActivity::class.java)
                    startActivity(intent)
                }

                3 -> {
                    title = getString(R.string.configuracoes)
                    AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage("SERÁ IMPLEMENTADA A TELA DE CONFIGURAÇÕES")
                        .create()
                        .show()
                }

                4 -> {
                    title = getString(R.string.sairApp)
                    AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage("NAO PRECISA USAR ESSE BOTÃO PARA FECHAR O APP")
                        .create()
                        .show()
                }
            }

            Log.i("teste","CLICOU NO ITEM $id")
        })

        var mainRecyclerView = binding.rvMain // referencia ao XML

        // O RecyclerView recebe como atributo adpter a intanciação do MainAdapter(LISTA,INTERFACE DE CLIQUE)
        mainRecyclerView.adapter = adapter
        mainRecyclerView.layoutManager = GridLayoutManager(this, 2)
        // CLASSE PARA ADMINISTRAR A RECYCLER E SUAS CELULAS
        // ADAPTER ->
    }

    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListener: (Int) -> Unit
        ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>(){

        // 1 - QUAL É O LAYOUT DO XML DA CELULA ESPECIFICA (item) QUE SERA INFLADO
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            // infla o layout do item especifico que quero jogar na tela e o retorna como
            // "MainViewHolder"
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }
        // 2 - DISPARADO TODA VEZ QUE HOUVER UMA ROLAGEM NA TELA E FOR NECESSARIO TROCAR O CONTEUDO
        // DA CELULA
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)
        }

        // 3 - INFORMA QUANTAS CELULAS ESSA LISTAGEM TERÁ
        override fun getItemCount(): Int {
            return mainItems.size
        }


        // É A CLASSE DA CELULA EM SI!
        // DENTRO DESSA CLASSE EU CONSIGO SOMENTE AS REFERENCIAS DO XML QUE FORAM PASSADOS QUANDO
        // O LAYOUT FOI INFLADO, NESTE CASO FOI O: "val view = layoutInflater.inflate(R.layout.main_item)"
        // O UNICO XML que poderá ser referenciado é o MAIN_ITEM neste caso.
        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            // RECEBE UM "item" QUE É UMA DATACLASS QUE CONTEM OS ATRIBUTOS QUE SERÃO ALTERADOS.
            fun bind(item: MainItem){
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val nome: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container)

                img.setImageResource(item.drawableId)
                nome.setText(item.textStringId)

                container.setOnClickListener {
                    onItemClickListener.invoke(item.id)
                }
            }
        }
    }

    private fun setupWindow(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}