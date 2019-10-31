
package com.linter.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class MyFirstIssueRegistry: IssueRegistry() {
    override val issues: List<Issue>
        get() =  listOf(FORCE_UNWRAP_ISSUE, LATEINIT_ISSUE)

    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API

    override val minApi: Int = -1
}