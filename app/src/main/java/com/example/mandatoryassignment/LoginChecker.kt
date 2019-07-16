package com.example.mandatoryassignment

object LoginChecker {


    fun Check(username: String, password: String): Boolean {
        return if (username == "soeren")
            password == "test" && username == "soeren"
        else if (username == "anbo")
            password == "test" && username == "anbo"
        else
            false
    }
}
