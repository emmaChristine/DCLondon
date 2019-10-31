
package com.linter.lint

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test
import java.io.File

class LateinitDetectorTest {
    val sdkHome = System.getenv("HOME") + "/Library/Android/sdk"



    val stubCorrectKtFile = TestFiles.kt("""
        package com.scape.lint
        fun notValid() {
            var a:String? = ""
            a?.let {
                a = "What a wonderful day"
            }
        }
        """)

    val stubKtFileLateinitProperty = TestFiles.kt("""
       package com.scape.lint
       fun notValid() {
          lateinit var a:String
          a.length
        }
        """)

    @Test
    fun lateinitPropertyDetected() {
        lint().files(stubKtFileLateinitProperty).issues(LATEINIT_ISSUE)
                .sdkHome(File(sdkHome))
                .run()
                .expect("src/com/scape/lint/test.kt:4: Error: Lateinit detected! [Lateinit]\n" +
                        "          lateinit var a:String\n" +
                        "          ~~~~~~~~\n" +
                        "1 errors, 0 warnings")
            .expectFixDiffs("Fix for src/com/scape/lint/test.kt line 4: Delete \"lateinit\":\n" +
                    "@@ -4 +4\n" +
                    "-           lateinit var a:String\n" +
                    "+            var a:String")
    }

    @Test
    fun lateinitNotDetected() {
        lint().files(stubCorrectKtFile).issues(LATEINIT_ISSUE)
                .sdkHome(File(sdkHome))
                .run()
                .expectClean()
    }
}