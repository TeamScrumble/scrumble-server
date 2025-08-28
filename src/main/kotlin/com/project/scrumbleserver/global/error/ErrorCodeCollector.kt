package com.project.scrumbleserver.global.error

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.stereotype.Component

@Component
class ErrorCodeCollector {

    companion object {
        private const val SCAN_BASED_PACKAGE = "com.project.scrumbleserver"
    }

    fun collect(): Map<String, List<ErrorCode>> {
        val scanner = ClassPathScanningCandidateComponentProvider(false).apply {
            addIncludeFilter(AssignableTypeFilter(ErrorCode::class.java))
        }

        return scanner.findCandidateComponents(SCAN_BASED_PACKAGE).associate { beanDefinition ->
            val clazz = Class.forName(beanDefinition.beanClassName)
            if (clazz.isEnum && ErrorCode::class.java.isAssignableFrom(clazz)) {
                clazz.simpleName to clazz.asSubclass(Enum::class.java).enumConstants.mapNotNull {
                    it as? ErrorCode
                }
            } else {
                clazz.simpleName to emptyList()
            }
        }
    }
}