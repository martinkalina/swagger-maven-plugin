package com.github.kongchen.swagger.docgen.reader

import io.swagger.models.Operation
import io.swagger.models.Response
import org.springframework.core.DefaultParameterNameDiscoverer
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KFunction

class KotlinParametersProcessor(
    private val apiReader: SpringMvcApiReader
) {

    fun processParameters(method: Method, operation: Operation) {
        val parameterTypes = method.parameterTypes
        val genericParameterTypes = method.genericParameterTypes
        val paramAnnotations = method.parameterAnnotations
        val parameterNameDiscoverer = DefaultParameterNameDiscoverer()
        val parameterNames = parameterNameDiscoverer.getParameterNames(method)

        val kClass = method.declaringClass.kotlin
        val kFunction = kClass.members.find { it is KFunction && it.name == method.name } as KFunction
        for (i in parameterTypes.indices) {
            val type = genericParameterTypes[i]
            val annotations = Arrays.asList(*paramAnnotations[i])
            val parameters = apiReader.getParameters(type, annotations)

            for (parameter in parameters) {
                if (parameter.name.isEmpty()) {
                    parameter.name = parameterNames[i]
                }
                val kParameter = kFunction.parameters[i + 1]
                if (kParameter.isOptional) parameter.required = false
                operation.parameter(parameter)
            }
        }

        if (operation.responses == null) {
            operation.defaultResponse(Response().description("successful operation"))
        }

        // Process @ApiImplicitParams
        apiReader.readImplicitParameters(method, operation)

        apiReader.processOperationDecorator(operation, method)
    }

}
