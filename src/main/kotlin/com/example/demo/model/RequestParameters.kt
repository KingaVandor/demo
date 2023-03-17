package com.example.demo.model

data class RequestParameters(
    val firstName: String,
    val lastName: String,
    val software: SoftwarePackage,
    val key: String
)