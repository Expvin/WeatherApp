package com.example.weatherapp.business.repos

import com.example.weatherapp.business.ApiProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseRepository<T>(val api: ApiProvider) {
    val dataEmitter: BehaviorSubject<T> = BehaviorSubject.create()
}