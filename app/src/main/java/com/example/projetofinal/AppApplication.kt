package com.example.projetofinal

import android.app.Application
import com.example.projetofinal.di.firebaseModule
import com.example.projetofinal.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(
                listOf(
                    firebaseModule,
                    repositoryModule
                )
            )
        }
    }
}