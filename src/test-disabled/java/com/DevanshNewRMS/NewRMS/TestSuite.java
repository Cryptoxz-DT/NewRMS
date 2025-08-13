package com.DevanshNewRMS.NewRMS;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("NewRMS Test Suite")
@SelectPackages({
    "com.DevanshNewRMS.NewRMS.model",
    "com.DevanshNewRMS.NewRMS.repository", 
    "com.DevanshNewRMS.NewRMS.service",
    "com.DevanshNewRMS.NewRMS.controller",
    "com.DevanshNewRMS.NewRMS.security",
    "com.DevanshNewRMS.NewRMS.integration"
})
public class TestSuite {
    // Test suite configuration class
}