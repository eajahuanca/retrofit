package com.example.edwin.appretrofit2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var reunionesAdapter : ReunionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reunionesAdapter = ReunionesAdapter()
        reuniones_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        reuniones_list.adapter = reunionesAdapter

        val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("https://actividades.oopp.gob.bo/reunionesapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val restReuniones = retrofit.create(RestReuniones::class.java)

        restReuniones.getReuniones()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({reunionesAdapter.setReuniones(it.data)},
                        {
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                            Log.i("error : ",it.stackTrace.toString())
                        })
    }

    inner class ReunionesAdapter : RecyclerView.Adapter<ReunionesAdapter.ReunionesViewHolder>(){

        private val reuniones : MutableList<Reuniones> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReunionesViewHolder {
            return ReunionesViewHolder(layoutInflater.inflate(R.layout.item_reuniones_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return reuniones.size
        }

        override fun onBindViewHolder(holder: ReunionesViewHolder, position: Int) {
            holder.bindModel(reuniones[position])
        }

        fun setReuniones(data : List<Reuniones>){
            reuniones.addAll(data)
            notifyDataSetChanged()
        }

        inner class ReunionesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val txtid : TextView = itemView.findViewById(R.id.id_reunion)
            val txtnombre : TextView = itemView.findViewById(R.id.tvnombre)
            val txtfecha_firma : TextView = itemView.findViewById(R.id.tvfecha_firma)
            val txtproyecto : TextView = itemView.findViewById(R.id.tvproyecto)
            val txtavance : TextView = itemView.findViewById(R.id.tvavance)
            val txttel : TextView = itemView.findViewById(R.id.tvtel)
            val txtter : TextView = itemView.findViewById(R.id.tvter)
            val txtvi : TextView = itemView.findViewById(R.id.tvvi)

            fun bindModel(reuniones: Reuniones){
                txtid.text = reuniones.id.toString()
                txtnombre.text = reuniones.nombre.toString()
                txtfecha_firma.text = reuniones.fecha_firma.toString()
                txtavance.text = reuniones.avance.toString()
                txtproyecto.text = reuniones.proyecto.toString()
                txttel.text = reuniones.telecomunicaciones.toString()
                txtter.text = reuniones.transportes.toString()
                txtvi.text = reuniones.vivienda.toString()
            }
        }
    }
}
