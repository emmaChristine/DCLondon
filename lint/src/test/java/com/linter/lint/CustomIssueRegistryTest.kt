/**
 *
 * Copyright © 2019 Scape Technologies Limited. All rights reserved.
 */
package com.linter.lint

import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class CustomIssueRegistryTest {

    private var mCustomIssueRegistry: MyFirstIssueRegistry? = null

    /**
     * Setup for the other test methods
     */
    @Before
    @Throws(Exception::class)
    fun setUp() {
        mCustomIssueRegistry = MyFirstIssueRegistry()
    }

    /**
     * Test that the Issue Registry contains the correct number of Issues
     */
    @Test
    @Throws(Exception::class)
    fun testNumberOfIssues() {
        val size = mCustomIssueRegistry!!.issues.size
        assertEquals(2, size)
    }

    /**
     * Test that the Issue Registry contains the correct Issues
     */
    @Test
    @Throws(Exception::class)
    fun testGetIssues() {
        val actual = mCustomIssueRegistry!!.issues
        assertEquals(actual[0], FORCE_UNWRAP_ISSUE)
    }

}