package com.puzzletimer.models

class ScramblerInfo(val scramblerId: String, val puzzleId: String, val description: String) {
    override fun toString(): String {
        return description
    }
}