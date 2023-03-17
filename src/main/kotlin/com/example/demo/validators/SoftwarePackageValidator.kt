package com.example.demo.validators

import com.example.demo.exceptions.ValidationException
import com.example.demo.model.SoftwarePackage
import org.springframework.stereotype.Component

@Component
class SoftwarePackageValidator {
    fun parseSoftwarePackageName(softwarePackageName: String): SoftwarePackage {
        return SoftwarePackage.valueOf(softwarePackageName.trim().uppercase())
    }

    fun getSoftwarePackageNameFromKey(key: String): SoftwarePackage {
        val lastLetterOfKey = key.trim().last().toString()
        for (abb in SoftwarePackage.values()) {
            if (abb.abbreviation == lastLetterOfKey) return abb
        }
        throw ValidationException("couldn't find software package $lastLetterOfKey")
    }

}