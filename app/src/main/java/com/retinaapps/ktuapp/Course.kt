package com.retinaapps.ktuapp

import java.io.Serializable

data class Course(val course: String, val credit: String, var type: String, var completed: String, var grade: String, var earned: String) : Serializable