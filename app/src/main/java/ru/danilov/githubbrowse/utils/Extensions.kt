package ru.danilov.githubbrowse.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun <T : View> View.find(id: Int): T = findViewById(id)
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.onClick(block: () -> Unit) {
    setOnClickListener({ block() })
}

inline fun <reified T : Any> Context.intentFor(): Intent = Intent(this, T::class.java)

inline fun <reified T : Any> Context.startActivity() {
    startActivity(this.intentFor<T>())
}
