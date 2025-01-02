package com.example.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.databinding.ActivityListCalcBinding
import com.example.fitnesstracker.model.Calc
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.jvm.Throws

class ListCalcActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListCalcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()

        val result = mutableListOf<Calc>()
        val adapter = ListCalcAdapter(result)
        val listRecyclerView = binding.rvList

        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = adapter


        // PEGA AS INFORMAÇÕES "EXTRAS" de uma intenção e a joga dentro de uma variavel type
        // para que seja usada como argumento da função do DAO para buscar o registro por tipo
        // e mostra esses itens como uma lista de CALC
        // vou deixar essa linha desativada por hora.
        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found")
        Thread{
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread{
                result.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()

    }

    private inner class ListCalcAdapter(
        private val listCalc: List<Calc>,
    ) : RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcViewHolder {
            val view = layoutInflater.inflate(R.layout.list_calc_item, parent, false)
            return ListCalcViewHolder(view)
        }
        override fun onBindViewHolder(holder: ListCalcViewHolder, position: Int) {
            val itemCurrent = listCalc[position]
            holder.bind(itemCurrent)
        }

        override fun getItemCount(): Int {
            return listCalc.size
        }

        private inner class ListCalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            fun bind(item: Calc){
                val tv = itemView as TextView

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                val date = sdf.format(item.createDate)
                val res = item.res

                tv.text = getString(R.string.list_response, res, date)

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