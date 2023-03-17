package com.example.demo.validators

import com.example.demo.model.RequestParameters
import com.example.demo.model.RequestType
import org.springframework.stereotype.Component

@Component
class ParameterParser(
    private val softwareValidator: SoftwarePackageValidator,
    private val stringValidator: StringParameterValidator
) {

    fun validateAndParseParameters(userFirstName: String, userLastName: String, key: String, softwarePackageName: String?, type: RequestType): RequestParameters {
        val software = when (type) {
            RequestType.KEYGEN -> softwareValidator.parseSoftwarePackageName(softwarePackageName!!)
            RequestType.KEYVAL -> softwareValidator.getSoftwarePackageNameFromKey(key)
        }

        val firstName = stringValidator.validateString(userFirstName)
        val lastName = stringValidator.validateString(userLastName)
        val secret = when (type) {
            RequestType.KEYGEN -> stringValidator.validateString(key, 200, false)
            RequestType.KEYVAL -> stringValidator.validateString(key, 200, false).dropLast(1)
        }

        return RequestParameters(firstName, lastName, software, secret)
    }
}