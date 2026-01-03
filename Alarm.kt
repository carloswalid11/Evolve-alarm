package com.evolve.alarm

data class Alarm(
    val id: String = java.util.UUID.randomUUID().toString(),
    val time: String = "07:00"
)