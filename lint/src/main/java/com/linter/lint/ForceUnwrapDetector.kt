
package com.linter.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UClass
import java.util.*
import java.util.regex.Pattern
import com.android.tools.lint.detector.api.Detector



private val pattern = Pattern.compile("!!")

private val DETECTOR_CLASS = ForceUnwrapDetector::class.java
private val DETECTOR_SCOPE = Scope.JAVA_FILE_SCOPE

val IMPLEMENTATION = Implementation(
    DETECTOR_CLASS,
    DETECTOR_SCOPE)


val FORCE_UNWRAP_ISSUE = Issue.create("ForceUnwrap",
        "Naughty",
        "Should avoid force unwrap, this can result in NPE",
        Category.CORRECTNESS,
        5,
        Severity.ERROR,
        IMPLEMENTATION)


/**
 * Simple pattern detector for force unwraps.
 */
class ForceUnwrapDetector: Detector(), Detector.UastScanner {

    /**
     * UClass is a representation of a Kotlin or Java class.
     */
    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext) =
            ForceUnwrapHandler(context)

    class ForceUnwrapHandler(private val context: JavaContext) :
            UElementHandler() {
        override fun visitClass(node: UClass) {

            val source = context.getContents().toString()
            val matcher = pattern.matcher(source)

            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()

                val location = Location.create(context.file, source, start, end)
                context.report(FORCE_UNWRAP_ISSUE, location, "Force unwrap detected!")
            }
        }

    }
}