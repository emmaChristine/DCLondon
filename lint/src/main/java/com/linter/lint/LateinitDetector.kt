
package com.linter.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UClass
import java.util.*
import java.util.regex.Pattern

private val pattern = Pattern.compile("lateinit")

val LATEINIT_ISSUE = Issue.create("Lateinit",
        "Naughty",
        "Should avoid lateinit properties, this can result in NPE",
        Category.CORRECTNESS,
        5,
        Severity.ERROR,
        Implementation(LateinitDetector::class.java, EnumSet.of(Scope.JAVA_FILE)))


/**
 * Simple pattern detector for lateinit properties.
 */
class LateinitDetector: Detector(), Detector.UastScanner {

    /**
     * UClass is a representation of a Kotlin or Java class.
     */
    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext) =
            LateinitHandler(context)

    class LateinitHandler(private val context: JavaContext) :
            UElementHandler() {
        override fun visitClass(node: UClass) {

            val source = context.getContents().toString()
            val matcher = pattern.matcher(source)

            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()

                val location = Location.create(context.file, source, start, end)

                val fix = LintFix.create()
                    .replace()
                    .text("lateinit")
                    .with("")
                    .build()

                context.report(LATEINIT_ISSUE, location, "Lateinit detected!", fix)
            }
        }

    }
}