package com.example.projetofinal.di

import android.content.SharedPreferences
import com.example.projetofinal.repository.ProdutoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
//import com.google.firebase.storage.FirebaseStorage
import org.koin.core.module.Module
import org.koin.dsl.module


val repositoryModule = module {
    single<ProdutoRepository> { ProdutoRepository(get(), get() ) }
}

val firebaseModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    single<FirebaseStorage> {Firebase.storage}
}

val appModules: List<Module> = listOf(
    repositoryModule,
    firebaseModule
)