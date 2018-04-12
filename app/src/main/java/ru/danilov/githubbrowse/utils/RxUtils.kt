package ru.danilov.githubbrowse.utils

import io.reactivex.*
import ru.danilov.githubbrowse.view.main.MainActivity

/**
 * Created by Alexey Danilov on 24.01.17.
 * 2017
 */
fun <T> Single<T>.async(): Single<T> = subscribeOn(MainActivity.RxSchedulers.io()).observeOn(MainActivity.RxSchedulers.main())

