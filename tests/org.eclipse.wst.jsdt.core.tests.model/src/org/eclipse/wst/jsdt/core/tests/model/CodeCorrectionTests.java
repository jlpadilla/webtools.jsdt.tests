/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import junit.framework.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.search.*;


public class CodeCorrectionTests extends AbstractJavaModelTests {
	public static boolean fgDebug = false;
	public static boolean fgSpecific = false;
	
public CodeCorrectionTests(String name) {
	super(name);
}
private IMarker[] getMarkers(IJavaScriptUnit unit){
	try {
		IResource resource = unit.getCorrespondingResource();
		return resource.findMarkers(IJavaScriptModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
	} catch (CoreException e) {
	}
	return new IMarker[0];
}
private IMarker getMarker(IJavaScriptUnit unit, String message) throws CoreException {
	IMarker[] markers = getMarkers(unit);
	for (int i = 0; i < markers.length; i++) {
		IMarker marker = markers[i];
		if (message.equals(marker.getAttribute(IMarker.MESSAGE))) {
			return marker;
		}
	}
	return null;
}
/**
 * Return the project names to load in the solution
 * when an independent test suite is being run.
 */
public static String[] getProjectNames() {
	return new String[] {"Compiler", "CodeCorrection"};
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	
	IJavaScriptProject project = setUpJavaProject("CodeCorrection");
	
	// dummy query for waiting until the indexes are ready
	SearchEngine engine = new SearchEngine();
	IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
	try {
		engine.searchAllTypeNames(
			null,
			SearchPattern.R_EXACT_MATCH,
			"!@$#!@".toCharArray(),
			SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE,
			IJavaScriptSearchConstants.CLASS,
			scope, 
			new TypeNameRequestor() {
				public void acceptType(
					int modifiers,
					char[] packageName,
					char[] simpleTypeName,
					char[][] enclosingTypeNames,
					String path) {}
			},
			IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			null);
	} catch (CoreException e) {
	}
	//	do a full build to create markers
	JavaScriptCore.setOptions(JavaScriptCore.getDefaultOptions());
	try {
		getWorkspace().getRoot().getProject("CodeCorrection").build(IncrementalProjectBuilder.FULL_BUILD,null);
		waitForAutoBuild();
	} catch (CoreException e) {
		assertTrue("building failed", false);
	}
}
public void tearDownSuite() throws Exception {
	deleteProject("CodeCorrection");
	
	super.tearDownSuite();
}
public static Test suite() {
	return buildModelTestSuite(CodeCorrectionTests.class);
	/*
	Suite suite = new Suite(CodeCorrectionTests.class.getName());
	if (fgSpecific) {
		suite.addTest(new CodeCorrectionTests("testCorrectMethod1"));
		
		return suite;
	}
	suite.addTest(new CodeCorrectionTests("testCorrectFieldType1"));
	suite.addTest(new CodeCorrectionTests("testCorrectFieldType2"));
	suite.addTest(new CodeCorrectionTests("testCorrectFieldType3"));
	suite.addTest(new CodeCorrectionTests("testCorrectLocalVariableType1"));
	suite.addTest(new CodeCorrectionTests("testCorrectLocalVariableType2"));
	suite.addTest(new CodeCorrectionTests("testCorrectImport1"));
	suite.addTest(new CodeCorrectionTests("testCorrectImport2"));
	suite.addTest(new CodeCorrectionTests("testCorrectImport3"));
	suite.addTest(new CodeCorrectionTests("testCorrectSuperClass1"));
	suite.addTest(new CodeCorrectionTests("testCorrectSuperClass2"));
	suite.addTest(new CodeCorrectionTests("testCorrectSuperInterface1"));
	suite.addTest(new CodeCorrectionTests("testCorrectSuperInterface2"));
	suite.addTest(new CodeCorrectionTests("testCorrectException1"));
	suite.addTest(new CodeCorrectionTests("testCorrectException2"));
	suite.addTest(new CodeCorrectionTests("testCorrectMethod1"));
	suite.addTest(new CodeCorrectionTests("testCorrectMethod2"));
	suite.addTest(new CodeCorrectionTests("testCorrectField1"));
	suite.addTest(new CodeCorrectionTests("testCorrectField2"));
	suite.addTest(new CodeCorrectionTests("testCorrectLocalVariable1"));
	suite.addTest(new CodeCorrectionTests("testCorrectArgument1"));
	suite.addTest(new CodeCorrectionTests("testCorrectReturnType1"));
	suite.addTest(new CodeCorrectionTests("testCorrectReturnType2"));
	suite.addTest(new CodeCorrectionTests("testWarningTokens"));

	return suite;
	*/
}
public void testCorrectFieldType1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectFieldType1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);
	
	String src = cu.getSource();
	String error = "dddz";
	int start = src.indexOf(error);
	int end = start + error.length();
		
	assertEquals(
		"should have two suggestions", 
		"ddd\n"+
		"ddd.eee",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectFieldType2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectFieldType2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AClassz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AClass\n"+
		"AClass2",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectFieldType3() throws CoreException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectFieldType3.js");
	IMarker marker = getMarker(cu, "AClassz cannot be resolved to a type");
	assertTrue("Marker not found", marker != null);
	try {
		engine.computeCorrections(marker, null, 0, requestor);
	} catch(CoreException e) {
	}
	
	String src = cu.getSource();
	String error = "AClassz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AClass\n"+
		"AClass2",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectLocalVariableType1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectLocalVariableType1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);
	
	String src = cu.getSource();
	String error = "dddz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"ddd\n"+
		"ddd.eee",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectLocalVariableType2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectLocalVariableType2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AClassz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AClass\n"+
		"AClass2",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectImport1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectImport1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "dddz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"ddd\n"+
		"ddd.eee",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectImport2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectImport2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "dddz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"ddd\n"+
		"ddd.eee",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectImport3() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectImport3.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AClassz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AClass\n"+
		"AClass2",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectSuperClass1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectSuperClass1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "dddz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"ddd\n"+
		"ddd.eee",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectSuperClass2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectSuperClass2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AClassz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AClass\n"+
		"AClass2",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectSuperInterface1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectSuperInterface1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "cccz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions",
		"ccc\n"+
		"cccInterface",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectSuperInterface2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectSuperInterface2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AListenerz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AListener",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectException1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectException1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bbbz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions",
		"bbb\n"+
		"bbb.ccc",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectException2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectException2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AnExceptionz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions",
		"AnException",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectMethod1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectMethod1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bar";
	int start = src.lastIndexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have one suggestion",
		"bar0",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectMethod2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectMethod2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bar";
	int start = src.lastIndexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have one suggestion",
		"bar0",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectField1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectField1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bar";
	int start = src.lastIndexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have one suggestion",
		"bar0",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectField2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectField2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bar";
	int start = src.lastIndexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have one suggestion",
		"bar0",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectLocalVariable1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectLocalVariable1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bar";
	int start = src.lastIndexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have one suggestion",
		"bar0",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectArgument1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectArgument1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "bar";
	int start = src.lastIndexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have one suggestion",
		"bar0",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		""+start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		""+end,
		requestor.getEnds());
}
public void testCorrectReturnType1() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectReturnType1.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "dddz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"ddd\n"+
		"ddd.eee",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testCorrectReturnType2() throws JavaScriptModelException {
	CorrectionEngine engine = new CorrectionEngine(JavaScriptCore.getOptions());
	CodeCorrectionTestsRequestor requestor = new CodeCorrectionTestsRequestor();
	IJavaScriptUnit cu= getCompilationUnit("CodeCorrection", "src", "", "CorrectReturnType2.js");
	IMarker[] markers = getMarkers(cu);
	assertTrue("should have one problem",markers.length == 1);
	engine.computeCorrections(markers[0], null, 0, requestor);

	String src = cu.getSource();
	String error = "AClassz";
	int start = src.indexOf(error);
	int end = start + error.length();
	
	assertEquals(
		"should have two suggestions", 
		"AClass\n"+
		"AClass2",
		requestor.getSuggestions());
	assertEquals(
		"a start of a suggestion is not correct", 
		start+"\n"+
		start,
		requestor.getStarts());
	assertEquals(
		"a end of a suggestion is not correct", 
		end+"\n"+
		end,
		requestor.getEnds());
}
public void testWarningTokens() {
	assertNull("assertIdentifier is a valid token for @SuppressWarnings", CorrectionEngine.getWarningToken(IProblem.UseAssertAsAnIdentifier));
	assertEquals("wrong token", "deprecation", CorrectionEngine.getWarningToken(IProblem.UsingDeprecatedConstructor));
	assertEquals("wrong token", "deprecation", CorrectionEngine.getWarningToken(IProblem.OverridingDeprecatedMethod));
	assertEquals("wrong token", "deprecation", CorrectionEngine.getWarningToken(IProblem.UsingDeprecatedType));
	assertEquals("wrong token", "deprecation", CorrectionEngine.getWarningToken(IProblem.UsingDeprecatedMethod));
	assertEquals("wrong token", "deprecation", CorrectionEngine.getWarningToken(IProblem.UsingDeprecatedField));
	assertEquals("wrong token", "boxing", CorrectionEngine.getWarningToken(IProblem.BoxingConversion));
	assertEquals("wrong token", "boxing", CorrectionEngine.getWarningToken(IProblem.UnboxingConversion));
	assertEquals("wrong token", "dep-ann", CorrectionEngine.getWarningToken(IProblem.FieldMissingDeprecatedAnnotation));
	assertEquals("wrong token", "dep-ann", CorrectionEngine.getWarningToken(IProblem.MethodMissingDeprecatedAnnotation));
	assertEquals("wrong token", "dep-ann", CorrectionEngine.getWarningToken(IProblem.TypeMissingDeprecatedAnnotation));
	assertEquals("wrong token", "finally", CorrectionEngine.getWarningToken(IProblem.FinallyMustCompleteNormally));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.FieldHidingLocalVariable));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.FieldHidingField));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.LocalVariableHidingLocalVariable));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.LocalVariableHidingField));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.ArgumentHidingLocalVariable));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.ArgumentHidingField));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.MaskedCatch));
	assertEquals("wrong token", "hiding", CorrectionEngine.getWarningToken(IProblem.TypeParameterHidingType));
	assertEquals("wrong token", "nls", CorrectionEngine.getWarningToken(IProblem.NonExternalizedStringLiteral));
	assertEquals("wrong token", "incomplete-switch", CorrectionEngine.getWarningToken(IProblem.MissingEnumConstantCase));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedImport));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.LocalVariableIsNeverUsed));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.ArgumentIsNeverUsed));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedPrivateConstructor));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedPrivateMethod));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedPrivateField));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedPrivateType));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedMethodDeclaredThrownException));
	assertEquals("wrong token", "unused", CorrectionEngine.getWarningToken(IProblem.UnusedConstructorDeclaredThrownException));
	assertEquals("wrong token", "static-access", CorrectionEngine.getWarningToken(IProblem.IndirectAccessToStaticMethod));
	assertEquals("wrong token", "static-access", CorrectionEngine.getWarningToken(IProblem.IndirectAccessToStaticField));
	assertEquals("wrong token", "static-access", CorrectionEngine.getWarningToken(IProblem.IndirectAccessToStaticType));
	assertEquals("wrong token", "static-access", CorrectionEngine.getWarningToken(IProblem.NonStaticAccessToStaticMethod));
	assertEquals("wrong token", "static-access", CorrectionEngine.getWarningToken(IProblem.NonStaticAccessToStaticField));
	assertEquals("wrong token", "synthetic-access", CorrectionEngine.getWarningToken(IProblem.NeedToEmulateFieldReadAccess));
	assertEquals("wrong token", "synthetic-access", CorrectionEngine.getWarningToken(IProblem.NeedToEmulateFieldWriteAccess));
	assertEquals("wrong token", "synthetic-access", CorrectionEngine.getWarningToken(IProblem.NeedToEmulateMethodAccess));
	assertEquals("wrong token", "synthetic-access", CorrectionEngine.getWarningToken(IProblem.NeedToEmulateConstructorAccess));
	assertEquals("wrong token", "unqualified-field-access", CorrectionEngine.getWarningToken(IProblem.UnqualifiedFieldAccess));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeRawConstructorInvocation));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeRawMethodInvocation));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeTypeConversion));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeRawFieldAssignment));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeGenericCast));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeReturnTypeOverride));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeRawGenericMethodInvocation));
	assertEquals("wrong token", "unchecked", CorrectionEngine.getWarningToken(IProblem.UnsafeRawGenericConstructorInvocation));
	assertEquals("wrong token", "serial", CorrectionEngine.getWarningToken(IProblem.MissingSerialVersion));
}
}
