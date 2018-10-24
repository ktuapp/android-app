package com.retinaapps.ktuapp

import java.io.Serializable

data class Semester(val course: ArrayList<Course>, val cgpa: String, val sem: Int) : Serializable