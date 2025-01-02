package com.example.fitnesstracker

import android.app.Application
import com.example.fitnesstracker.model.AppDataBase

// CLASSE QUE É CHAMADA TODA VEZ QUE O APP INCIALIZAR, ASSIM COMO AS ATIVIDADES ELA PRECISA
// DO METODO "onCreate"
// TAMBEM É NECESSARIO COLOCAR A REFERENCIA DA CLASSE NO "AndroidManifest.xml" DENTR DA TAG
// <application: android: name = ".App">
class App: Application() {

    lateinit var db: AppDataBase

    override fun onCreate() {
        super.onCreate()
        // REFERENCIA DO BANCO DE DADOS EFETIVAMENTE PARA SER MANIPULADO NO APP
        db = AppDataBase.getDataBase(this)
    }


}