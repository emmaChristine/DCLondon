package com.linter.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test
import java.io.File


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class ForceUnwrapDetectorTest {

    private val stubKtFileUnwrapProperty: LintDetectorTest.TestFile = TestFiles.kt("""
       package com.scape.lint
       fun notValid() {
          val a:String?  = ""
          a!!.length
        }
        """)

    @Test
    fun forceUnwrapPropertyDetected() {
        lint().files(stubKtFileUnwrapProperty).issues(FORCE_UNWRAP_ISSUE)
            .sdkHome(File(sdkHome))
            .run()
            .expect("src/com/scape/lint/test.kt:5: Error: Force unwrap detected! [ForceUnwrap]\n" +
                    "          a!!.length\n" +
                    "           ~~\n" +
                    "1 errors, 0 warnings")
    }


    val sdkHome = System.getenv("HOME") + "/Library/Android/sdk"

    val stubKtFileUnwrapParam = TestFiles.kt("""
       package com.scape.lint
       fun notValid() {
          val a:String?  = ""

          val b = ""
          b.equals(a!!)
        }
        """)

    val stubCorrectKtFile = TestFiles.kt("""
        package com.scape.lint
        fun notValid() {
            val a:String?  = ""
            a?.let {
                a = "What a wonderful day"
            }
        }
        """)



    @Test
    fun forceUnwrapParamDetected() {
        lint().files(stubKtFileUnwrapParam).issues(FORCE_UNWRAP_ISSUE)
                .sdkHome(File(sdkHome))
                .run()
                .expect("src/com/scape/lint/test.kt:7: Error: Force unwrap detected! [ForceUnwrap]\n" +
                        "          b.equals(a!!)\n" +
                        "                    ~~\n" +
                        "1 errors, 0 warnings")
    }

    @Test
    fun forceUnwrapNotDetected() {
        lint().files(stubCorrectKtFile).issues(FORCE_UNWRAP_ISSUE)
                .sdkHome(File(sdkHome))
                .run()
                .expectClean()
    }
}