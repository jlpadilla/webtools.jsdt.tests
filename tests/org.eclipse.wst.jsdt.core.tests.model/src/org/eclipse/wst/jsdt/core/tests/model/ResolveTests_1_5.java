/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.*;

import junit.framework.*;

public class ResolveTests_1_5 extends AbstractJavaModelTests {
	IJavaScriptUnit wc = null;
	WorkingCopyOwner owner = null; 
	
static {
	// Names of tests to run: can be "testBugXXXX" or "BugXXXX")
	//TESTS_NAMES = new String[] { "test0095" };
	// Numbers of tests to run: "test<number>" will be run for each number of this array
	//TESTS_NUMBERS = new int[] { 13 };
	// Range numbers of tests to run: all tests between "test<first>" and "test<last>" will be run for { first, last }
	//TESTS_RANGE = new int[] { 16, -1 };
}
public static Test suite() {
	return buildModelTestSuite(ResolveTests_1_5.class);
}
public ResolveTests_1_5(String name) {
	super(name);
}
public IJavaScriptUnit getWorkingCopy(String path, String source) throws JavaScriptModelException {
	return super.getWorkingCopy(path, source, this.owner, null);
}
private IJavaScriptElement[] select(String path, String source, String selection) throws JavaScriptModelException {
	this.wc = getWorkingCopy(path, source);
	String str = wc.getSource();
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	return wc.codeSelect(start, length, this.owner);
}
private IJavaScriptElement[] selectAfter(String path, String source, String selection) throws JavaScriptModelException {
	this.wc = getWorkingCopy(path, source);
	String str = wc.getSource();
	int start = str.lastIndexOf(selection) + selection.length();
	return wc.codeSelect(start, 0, this.owner);
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	
	setUpJavaProject("Resolve", "1.5");
	
	waitUntilIndexesReady();
}
protected void setUp() throws Exception {
	super.setUp();
	
	this.owner = new WorkingCopyOwner(){};
}
public void tearDownSuite() throws Exception {
	deleteProject("Resolve");
	
	super.tearDownSuite();
}

protected void tearDown() throws Exception {
	if(this.wc != null) {
		this.wc.discardWorkingCopy();
	}
	super.tearDown();
}
public void test0001() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0001", "Test.js");
	
	String str = cu.getSource();
	String selection = "iii";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"iii [in foo(Iterable) [in Test [in Test.java [in test0001 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0002() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0002", "Test.js");
	
	String str = cu.getSource();
	String selection = "Y";
	int start = str.indexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Y [in X [in Test [in Test.java [in test0002 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0003() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0003", "Test.js");
	
	String str = cu.getSource();
	String selection = "X";
	int start = str.indexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"X [in Test [in Test.java [in test0003 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0004() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0004/Test.js",
			"package test0004;\n" +
			"public class Test <T> {\n" +
			"	test0004.Test.X<Object>.Y<Object> var;\n" +
			"	public class X <TX> {\n" +
			"		public class Y <TY> {\n" +
			"		}\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test [in [Working copy] Test.java [in test0004 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0005() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0005", "Test.js");
	
	String str = cu.getSource();
	String selection = "test0005";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"test0005 [in src2 [in Resolve]]",
		elements
	);
}
public void test0006() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0006", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0006";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0006> [in Test [in Test.java [in test0006 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0007() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0007", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0007";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0007> [in Test [in Test.java [in test0007 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0008() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0008", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0008";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0008> [in Inner [in Test [in Test.java [in test0008 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0009() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0009", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0009";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0009> [in Inner [in Test [in Test.java [in test0009 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0010() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0010", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0010";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0010> [in Test [in Test.java [in test0010 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0011() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0011", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0011";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0011> [in foo() [in Test [in Test.java [in test0011 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0012() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0012", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0012";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0012> [in foo() [in Test [in Test.java [in test0012 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0013() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0013", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0013";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0013> [in foo() [in Inner [in Test [in Test.java [in test0013 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
public void test0014() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0014", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test0014";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<Test0014> [in foo() [in Inner [in Test [in Test.java [in test0014 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=71852
 */
public void test0015() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0015", "Test.js");
	
	String str = cu.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in foo() [in Test [in Test.java [in test0015 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72105
 */
public void test0016() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0016", "Test.js");
	
	String str = cu.getSource();
	String selection = "T";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in foo() [in Test [in Test.java [in test0016 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72105
 */
public void test0017() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0017", "Test.js");
	
	String str = cu.getSource();
	String selection = "T";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in foo() [in Test [in Test.java [in test0017 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72105
 */
public void test0018() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0018", "Test.js");
	
	String str = cu.getSource();
	String selection = "T";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in foo(T) [in Test [in Test.java [in test0018 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72105
 */
public void test0019() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0019", "Test.js");
	
	String str = cu.getSource();
	String selection = "T";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in foo(Object, T, Object) [in Test [in Test.java [in test0019 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72105
 */
public void test0020() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0020", "Test.js");
	
	String str = cu.getSource();
	String selection = "T";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in foo(X<T>) [in Test [in Test.java [in test0020 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72105
 */
public void test0021() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0021", "Test.js");
	
	String str = cu.getSource();
	String selection = "T";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in foo() [in Test [in Test.java [in test0021 [in src2 [in Resolve]]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74286
 */
public void test0022() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0022", "Test.js");
	
	String str = cu.getSource();
	String selection = "add";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"add(T, A<T>, A<T>.B, A<T>.C<T>, A<T>.B.D<T>) [in X [in X.java [in test0022 [in src2 [in Resolve]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74286
 */
public void test0023() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0023", "Test.js");
	
	String str = cu.getSource();
	String selection = "add";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"add(T, test0023.A<T>, test0023.A<T>.B, test0023.A<T>.C<T>, test0023.A<T>.B.D<T>, test0023.E, test0023.E.F<T>) [in X [in X.class [in test0023 [in test0023.jar [in Resolve]]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77184
 */
public void test0024() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0024", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test [in Test.java [in test0024 [in src2 [in Resolve]]]]",
		elements
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77184
 */
public void test0025() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit("Resolve", "src2", "test0025", "Test.js");
	
	String str = cu.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test [in Test.java [in test0025 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0026() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0026/Test.js",
			"package test0026;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test.Inner x;\n" +
			"}");
	
	String str = this.wc.getSource();
	String selection = "Inn";
	int start = str.lastIndexOf(selection);
	
	IJavaScriptElement[] elements = this.wc.codeSelect(start, 0);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0026 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0027() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0027/Test.js",
			"package test0027;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inn";
	int start = str.lastIndexOf(selection);
	
	IJavaScriptElement[] elements = wc.codeSelect(start, 0);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0027 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0028() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0028/Test.js",
			"package test0028;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test<Object>.Inner x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inn";
	int start = str.lastIndexOf(selection);
	
	IJavaScriptElement[] elements = wc.codeSelect(start, 0);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0028 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0029() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0029/Test.js",
			"package test0029;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test<Object>.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inn";
	int start = str.lastIndexOf(selection);
	
	IJavaScriptElement[] elements = wc.codeSelect(start, 0);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0029 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0030() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0030/Test.js",
			"package test0030;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test.Inner x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0030 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0031() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0031/Test.js",
			"package test0031;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0031 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0032() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0032/Test.js",
			"package test0032;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {	\n" +
			"	}\n" +
			"	Test<Object>.Inner x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0032 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0033() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0033/Test.js",
			"package test0033;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test<Object>.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0033 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0034() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0034/Test.js",
			"package test0034;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test.Inner x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test.Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0034 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0035() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0035/Test.js",
			"package test0035;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {	\n" +
			"	}\n" +
			"	Test.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test.Inner<Object>";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0035 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0036() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0036/Test.js",
			"package test0036;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {	\n" +
			"	}\n" +
			"	Test<Object>.Inner x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test<Object>.Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0036 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0037() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0037/Test.js",
			"package test0037;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {	\n" +
			"	}\n" +
			"	Test<Object>.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test<Object>.Inner<Object>";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0037 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0038() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0038/Test.js",
			"package test0038;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test.Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0038 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0039() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0039/Test.js",
			"package test0039;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test<Object>.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test<Object>.Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0039 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0040() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0040/Test.js",
			"package test0040;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {\n" +
			"	}\n" +
			"	Test<Object>.Inner<Object> x;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner<Object>";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0040 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0041() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0041/Test.js",
			"package test0041;\n" +
			"public class Test<T> {\n" +
			"	void foo() {\n" +
			"		class Local1<T1> {\n" +
			"			class Local2<T2> {\n" +
			"			}\n" +
			"		}\n" +
			"		class Local3<T3> {\n" +
			"		} \n" +
			"		Local1<Local3<Object>>.Local2<Local3<Object>> l;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Local1<Local3<Object>>.Local2<Local3<Object>>";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Local2 [in Local1 [in foo() [in Test [in [Working copy] Test.java [in test0041 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
public void test0042() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0042/Test.js",
			"package test0042;\n" +
			"public class Test<T> {\n" +
			"	public class Inner<U> {	\n" +
			"	}\n" +
			"	Test<? super String>.Inner<? extends String> v;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test<? super String>.Inner<? extends String>";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0042 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0043() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0043/Test.js",
			"package test0043;\n" +
			"public class Test<T> {\n" +
			"	Test<T> var;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test [in [Working copy] Test.java [in test0043 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0044() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0044/Test.js",
			"package test0044;\n" +
			"public class Test<T1> {\n" +
			"}\n" +
			"class Test2<T2> {\n" +
			"	Test<T2> var;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test [in [Working copy] Test.java [in test0044 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0045() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0045/Test.js",
			"package test0045;\n" +
			"public class Test<T1> {\n" +
			"	String var;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Test [in [Working copy] Test.java [in test0045 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0046() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0046/Test.js",
			"package test0046;\n" +
			"public class Test<T1> {\n" +
			"	String var;\n" +
			"	void foo() {\n" +
			"	  var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Test [in [Working copy] Test.java [in test0046 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0047() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0047/Test.js",
			"package test0047;\n" +
			"public class Test<T1> {\n" +
			"	public String var;\n" +
			"	void foo() {\n" +
			"	  Test<String> t = null;\n" +
			"	  t.var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Test [in [Working copy] Test.java [in test0047 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0048() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0048/Test.js",
			"package test0048;\n" +
			"public class Test<T1> {\n" +
			"	public String var;\n" +
			"	void foo() {\n" +
			"	  Test<?> t = new Test<String>;\n" +
			"	  t.var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Test [in [Working copy] Test.java [in test0048 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0049() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0049/Test.js",
			"package test0049;\n" +
			"public class Test<T1> {\n" +
			"	public String var;\n" +
			"	void foo() {\n" +
			"	  Test<T1> t = null;\n" +
			"	  t.var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Test [in [Working copy] Test.java [in test0049 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0050() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0050/Test.js",
			"package test0050;\n" +
			"public class Test<T1> {\n" +
			"	public String var;\n" +
			"	void foo() {\n" +
			"	  Test t = null;\n" +
			"	  t.var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Test [in [Working copy] Test.java [in test0050 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0051() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0051/Test.js",
			"package test0051;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"	  class Inner<T> {\n" +
			"	    public String var;\n" +
			"	  }" +
			"	  Inner<Object> i = null;\n" +
			"	  i.var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Inner [in foo() [in Test [in [Working copy] Test.java [in test0051 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
public void test0052() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0052/Test.js",
			"package test0052;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"	  class Inner<T> {\n" +
			"	    public T var;\n" +
			"	  }" +
			"	  Inner<Object> i = null;\n" +
			"	  i.var = null;\n" +
			"	}\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "var";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"var [in Inner [in foo() [in Test [in [Working copy] Test.java [in test0052 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
public void test0053() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0053/Test.js",
			"package test0053;\n" +
			"public class Test<T> {\n" +
			"	public void foo() {\n" +
			"   }\n" +
			"}\n" +
			"class Test2<T> {\n" +
			"  void bar() {\n" +
			"    Test<String> var = null;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0053 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0054() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0054/Test.js",
			"package test0054;\n" +
			"public class Test<T> {\n" +
			"	public void foo() {\n" +
			"   }\n" +
			"}\n" +
			"class Test2<T> {\n" +
			"  void bar() {\n" +
			"    Test var = null;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0054 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0055() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0055/Test.js",
			"package test0055;\n" +
			"public class Test<T> {\n" +
			"	public void foo() {\n" +
			"   }\n" +
			"}\n" +
			"class Test2<T> {\n" +
			"  void bar() {\n" +
			"    Test<T> var = null;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0055 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0056() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0056/Test.js",
			"package test0056;\n" +
			"public class Test<T> {\n" +
			"  public void foo() {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    Test<T> var = null;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0056 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0057() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0057/Test.js",
			"package test0057;\n" +
			"public class Test<T1> {\n" +
			"  public <T2> void foo() {\n" +
			"  }\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar() {\n" +
			"    Test<String> var = null;\n" +
			"    var.<Object>foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0057 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0058() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0058/Test.js",
			"package test0058;\n" +
			"public class Test<T1> {\n" +
			"  public <T2> void foo() {\n" +
			"  }\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar() {\n" +
			"    Test<String> var = null;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0058 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0059() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0059/Test.js",
			"package test0059;\n" +
			"public class Test {\n" +
			"  public <T2> void foo() {\n" +
			"  }\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar() {\n" +
			"    Test var = null;\n" +
			"    var.<String>foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0059 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0060() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0060/Test.js",
			"package test0060;\n" +
			"public class Test {\n" +
			"  public <T2> void foo() {\n" +
			"  }\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar() {\n" +
			"    Test var = null;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0060 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0061() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0061/Test.js",
			"package test0061;\n" +
			"public class Test {\n" +
			"  public <T2> void foo() {\n" +
			"    Test var;\n" +
			"    var.<T2>foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0061 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0062() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0062/Test.js",
			"package test0062;\n" +
			"public class Test<T1> {\n" +
			"  public <T2> void foo() {\n" +
			"    Test var;\n" +
			"    var.<T1>foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0062 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0063() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0063/Test.js",
			"package test0063;\n" +
			"public class Test<T1> {\n" +
			"  public void foo() {\n" +
			"  }\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar() {\n" +
			"    Test<String> var;\n" +
			"    var.foo();\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "foo";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"foo() [in Test [in [Working copy] Test.java [in test0063 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0064() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0064/Test.js",
			"package test0064;\n" +
			"public class Test {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new <String>Test(null);\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0064 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0065() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0065/Test.js",
			"package test0065;\n" +
			"public class Test {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new Test(null);\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0065 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0066() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0066/Test.js",
			"package test0066;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new <String>Test<String>(null);\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0066 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0067() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0067/Test.js",
			"package test0067;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new Test<String>(null);\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0067 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0068() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0068/Test.js",
			"package test0068;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new Test(null);\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0068 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0069() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0069/Test.js",
			"package test0069;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  public class Inner<V> {\n" +
			"    public <W> Inner(W w) {\n" +
			"    }\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new <String>Test<String>(null).new <String>Inner<String>(null);\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner(W) [in Inner [in Test [in [Working copy] Test.java [in test0069 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0070() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0070/Test.js",
			"package test0070;\n" +
			"public class Test {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new <String>Test(null){};\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0070 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0071() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0071/Test.js",
			"package test0071;\n" +
			"public class Test {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new Test(null){};\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0071 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0072() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0072/Test.js",
			"package test0072;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new <String>Test<String>(null){};\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0072 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0073() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0073/Test.js",
			"package test0073;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new Test<String>(null){};\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0073 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0074() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0074/Test.js",
			"package test0074;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new Test(null){};\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test(U) [in Test [in [Working copy] Test.java [in test0074 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0075() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0075/Test.js",
			"package test0075;\n" +
			"public class Test<T> {\n" +
			"  public <U> Test(U u) {\n" +
			"  }\n" +
			"  public class Inner<V> {\n" +
			"    public <W> Inner(W w) {\n" +
			"    }\n" +
			"  }\n" +
			"  void bar() {\n" +
			"    new <String>Test<String>(null).new <String>Inner<String>(null){};\n" +
			"  }\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner(W) [in Inner [in Test [in [Working copy] Test.java [in test0075 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0076() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0076/Test.js",
			"package test0076;\n" +
			"public class Test<T> {\n" +
			"  public class Inner<U, V> {\n" +
			"  }\n" +
			"  Test<? super String>.Inner<int[][], Test<String[]>> var;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "Inner";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Inner [in Test [in [Working copy] Test.java [in test0076 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0077() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0077/Test.js",
			"package test0077;\n" +
			"@interface MyAnn {\n" +
			"}\n" +
			"public @MyAnn class Test {\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "MyAnn";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"MyAnn [in [Working copy] Test.java [in test0077 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0078() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0078/Test.js",
			"package test0078;\n" +
			"@interface MyAnn {\n" +
			"  String value();\n" +
			"}\n" +
			"public @MyAnn(\"\") class Test {\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "MyAnn";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"MyAnn [in [Working copy] Test.java [in test0078 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0079() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0079/Test.js",
			"package test0079;\n" +
			"@interface MyAnn {\n" +
			"  String value();\n" +
			"}\n" +
			"public @MyAnn class Test {\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "MyAnn";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"MyAnn [in [Working copy] Test.java [in test0079 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0080() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0080/Test.js",
			"package test0080;\n" +
			"@interface MyAnn {\n" +
			"  String value1();\n" +
			"  String value2();\n" +
			"}\n" +
			"public @MyAnn(value1 = \"\", value2 = \"\") class Test {\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "MyAnn";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"MyAnn [in [Working copy] Test.java [in test0080 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0081() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0081/Test.js",
			"package test0080;\n" +
			"@interface MyAnn {\n" +
			"  String value1();\n" +
			"  String value2();\n" +
			"}\n" +
			"public @MyAnn(value1 = \"\", value2 = \"\") class Test {\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "value1";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"value1() [in MyAnn [in [Working copy] Test.java [in test0081 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0082() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0082/Test.js",
			"package test0082;\n" +
			"public class Test<T> {\n" +
			"}",
			"Test");
	assertEquals("test0082.Test<T>", ((IType)elements[0]).getFullyQualifiedParameterizedName());
}
public void test0083() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0083/Test.js",
			"package test0083;\n" +
			"public class Test<T> {\n" +
			"  Test<String> field;\n" +
			"}",
			"Test");
	assertEquals("test0083.Test<java.lang.String>", ((IType)elements[0]).getFullyQualifiedParameterizedName());
}
public void test0084() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0084/Test.js",
			"package test0084;\n" +
			"public class Test<T> {\n" +
			"  Test field;\n" +
			"}",
			"Test");
	assertEquals("test0084.Test", ((IType)elements[0]).getFullyQualifiedParameterizedName());
}
public void test0085() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0085/Test.js",
			"package test0085;\n" +
			"public class Test<T> {\n" +
			"  class Member {\n" +
			"  }\n" +
			"}",
			"Member");
	assertEquals("test0085.Test<T>.Member", ((IType)elements[0]).getFullyQualifiedParameterizedName());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=80957
public void test0086() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Resolve/src2/test0086/Test.js",
			"package test0080;\n" +
			"public class Test {\n" +
			"   List<Integer> list;\n" +
			"}");
	
	String str = wc.getSource();
	String selection = "List";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaScriptElement[] elements = wc.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"",
		elements
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=82137
public void test0087() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/p/MyClass0087.js",
				"package p;\n" +
				"public class MyClass0087 {\n" +
				"   public static int bar = 0;\n" +
				"}");
		
		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0087/Test.js",
				"import static p.MyClass0087.bar;\n" +
				"package test0087;\n" +
				"public class Test {\n" +
				"}",
				"bar");
		
		assertElementsEqual(
			"Unexpected elements",
			"bar [in MyClass0087 [in [Working copy] MyClass0087.java [in p [in src2 [in Resolve]]]]]",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=82137
public void test0088() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/p/MyClass0088.js",
				"package p;\n" +
				"public class MyClass0088 {\n" +
				"   public static void foo() {}\n" +
				"   public static void foo(int i) {}\n" +
				"}");
		
		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0088/Test.js",
				"import static p.MyClass0088.foo;\n" +
				"package test0088;\n" +
				"public class Test {\n" +
				"}",
				"foo");
		
		assertElementsEqual(
			"Unexpected elements",
			"foo(int) [in MyClass0088 [in [Working copy] MyClass0088.java [in p [in src2 [in Resolve]]]]]\n" + 
			"foo() [in MyClass0088 [in [Working copy] MyClass0088.java [in p [in src2 [in Resolve]]]]]",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=82558
public void test0089() throws JavaScriptModelException {
	IJavaScriptElement[] elements = selectAfter(
			"/Resolve/src2/test0089/Test.js",
			"package test0089;\n" +
			"public class Test<T> {\n" +
			"  Test(String t) {}\n" +
			"  Test(Test<String> ts) {}\n" +
			"  void bar() {\n" +
			"    new Test<String>(new Test<String>(\"\"));\n" +
			"  }\n" +
			"}",
			"  new Te");
	
	assertElementsEqual(
		"Unexpected elements",
		"Test(Test<String>) [in Test [in [Working copy] Test.java [in test0089 [in src2 [in Resolve]]]]]",
		elements
	);

}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=83489
public void test0090() throws JavaScriptModelException {
	IJavaScriptElement[] elements = selectAfter(
			"/Resolve/src2/test0090/Test.js",
			"package test0090;\n" +
			"public class Test {\n" +
			"  <T>Test(T t) {}\n" +
			"}",
			"T");
	
	assertElementsEqual(
		"Unexpected elements",
		"<T> [in Test(T) [in Test [in [Working copy] Test.java [in test0090 [in src2 [in Resolve]]]]]]",
		elements
	);

}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=86971
public void test0091() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/test0091/MyAnnot.js",
				"package test0091;\n" +
				"public @interface MyAnnot {\n" +
				"}");
		
		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0091/Test.js",
				"package test0091;\n" +
				"@MyAnnot\n" +
				"public class Test {\n" +
				"}",
				"@MyAnnot");
		
		assertElementsEqual(
			"Unexpected elements",
			"MyAnnot [in [Working copy] MyAnnot.java [in test0091 [in src2 [in Resolve]]]]",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=86971
public void test0092() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/test0092/MyAnnot.js",
				"package test0092;\n" +
				"public @interface MyAnnot {\n" +
				"}");
		
		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0092/Test.js",
				"package test0092;\n" +
				"@MyAnnot @MyAnnot\n" +
				"public class Test {\n" +
				"}",
				"MyAnnot @MyAnnot");
		
		assertElementsEqual(
			"Unexpected elements",
			"",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=85379
public void test0093() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/test0093/MyEnum.js",
				"package test0093;\n" +
				"public enum MyEnum {\n" +
				"  MyEnumConstant;\n" +
				"}");
		
		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0093/Test.js",
				"package test0093;\n" +
				"public class Test {\n" + 
				"  void foo(MyEnum e) {\n" +
				"    switch(e) {\n" +
				"      case MyEnumConstant:\n" +
				"        break;\n" +
				"    }\n" +
				"  }\n" +
				"}\n",
				"MyEnumConstant");
		
		assertElementsEqual(
			"Unexpected elements",
			"MyEnumConstant [in MyEnum [in [Working copy] MyEnum.java [in test0093 [in src2 [in Resolve]]]]]",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
public void test0094() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0094/Test.js",
			"package test0094;\n" +
			"package import;\n" +
			"public class Test {\n" + 
			"  public void goo(ZZArrayList<String> a) {\n" + 
			"    a.get(0);\n" + 
			"  }\n" + 
			"}\n",
			"get");
	
	assertElementsEqual(
		"Unexpected elements",
		"get(int) {key=Ltest0094/ZZArrayList<Ljava/lang/String;>;.get(I)Ljava/lang/String;} [in ZZArrayList [in ZZArrayList.class [in test0094 [in class-folder [in Resolve]]]]]",
		elements,
		true/*show key*/
	);
}
/*
 * Regression test for bug 87929 Wrong decoding of type signature with wildcards
 */
public void test0095() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0095/X.js",
			"package test0095;\n" +
			"public class X {\n" + 
			"	Y<?, ? extends Z<? super Exception>> y;\n" + 
			"}\n" + 
			"class Y<K, V> {\n" + 
			"}\n" + 
			"class Z<T> {\n" + 
			"}",
			"Y<?, ? extends Z<? super Exception>>");
	
	assertElementsEqual(
		"Unexpected elements",
		"Y {key=Ltest0095/X~Y<Ltest0095/X~Y;*Ltest0095/X~Y;+Ltest0095/X~Z<Ltest0095/X~Z;-Ljava/lang/Exception;>;>;} [in [Working copy] X.java [in test0095 [in src2 [in Resolve]]]]",
		elements,
		true/*show key*/
	);
	
	String key = ((IType) elements[0]).getKey();
	String signature = new BindingKey(key).toSignature();
	String[] typeArguments = Signature.getTypeArguments(signature);
	assertStringsEqual(
		"Unexpected type arguments", 
		"*\n" + 
		"+Ltest0095.Z<-Ljava.lang.Exception;>;\n",
		typeArguments);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94653
public void test0096() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0096/X.js",
			"package test0095;\n" +
			"public class X<T> {\n" + 
			"  class Inner<U> {\n" + 
			"  }\n" + 
			"  X<String>.Inner var;\n" + 
			"}",
			"Inner");
	
	assertElementsEqual(
		"Unexpected elements",
		"Inner {key=Ltest0096/X<Ljava/lang/String;>.Inner<>;} [in X [in [Working copy] X.java [in test0096 [in src2 [in Resolve]]]]]",
		elements,
		true/*show key*/
	);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=95481
public void test0097() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/test0097/Key.js",
				"public class Key<\n" +
				"	TT extends Test<KK, TT>,\n" +
				"	KK extends Key<TT, KK>> {\n" +
				"}\n");

		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0097/Test.js",
				"public class Test<\n" +
				"	K extends Key<T, K>,\n" +
				"	T extends Test<K, T>> {\n" +
				"}\n",
				"Key");
		
		assertElementsEqual(
			"Unexpected elements",
			"Key [in [Working copy] Key.java [in test0097 [in src2 [in Resolve]]]]",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=83206
public void test0098() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Resolve/src2/test0098/Color.js",
				"public enum Color<\n" +
				"	BLUE,\n" +
				"	RED;\n" +
				"}\n");

		IJavaScriptElement[] elements = select(
				"/Resolve/src2/test0098/Test.js",
				"public class Test<\n" +
				"	void foo() {\n" +
				"		Color.valueOf(\"RED\");\n" +
				"	}\n" +
				"}\n",
				"valueOf");
		
		assertElementsEqual(
			"Unexpected elements",
			"",
			elements
		);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=99645
public void test0099() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0099/Test.js",
			"public class Test<\n" +
			"	void foo() {\n" +
			"		class Local<TLocal>{}\n" +
			"	}\n" +
			"}\n",
			"TLocal");
	
	assertElementsEqual(
		"Unexpected elements",
		"<TLocal> [in Local [in foo() [in Test [in [Working copy] Test.java [in test0099 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=99645
public void test0100() throws JavaScriptModelException {
	IJavaScriptElement[] elements = select(
			"/Resolve/src2/test0100/Test.js",
			"public class Test<\n" +
			"	void foo() {\n" +
			"		class Local<TLocal>{\n" +
			"			TLocal var;\n" +
			"		}\n" +
			"	}\n" +
			"}\n",
			"TLocal");
	
	assertElementsEqual(
		"Unexpected elements",
		"<TLocal> [in Local [in foo() [in Test [in [Working copy] Test.java [in test0100 [in src2 [in Resolve]]]]]]]",
		elements
	);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=99645
public void test0101() throws JavaScriptModelException {
	IClassFile cu = getClassFile("Resolve", "class-folder", "test0101", "Test.class");

	String str = cu.getSource();
	int start = str.indexOf("T field;");
	int length = "T".length();
	IJavaScriptElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
			"Unexpected elements",
			"<T> [in Test [in Test.class [in test0101 [in class-folder [in Resolve]]]]]",
			elements
	);
	
	ISourceRange nameRange = ((ITypeParameter)elements[0]).getNameRange();
	assertEquals("Offset is not correct" , str.indexOf("T>"), nameRange.getOffset());
	assertEquals("Length is not correct" , "T".length(), nameRange.getLength());
}

/*
 * Ensure that resolving a binary synthetic contructor call in a generic class returns the member type
 * (regression test for bug 131519 JDK with attached source unnavigable.)
 */
public void test0102() throws CoreException, IOException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {}, new String[] {"JCL15_LIB"}, "", "1.5");
		String source =
			"public class X<E> {\n" +
			"  private class Y {\n" +
			"  }\n" +
			"  Object foo() {\n" +
			"    return new Y();\n" +
			"  }\n" +
			"}";
		addLibrary(project, "lib15.jar", "lib15.zip", new String[] {"X.js", source}, "1.5");
		IClassFile classFile = getClassFile("P", "/P/lib15.jar", "", "X.class");
		int start = source.indexOf("Y()");
		int end = source.indexOf("();");
		IJavaScriptElement[] elements = classFile.codeSelect(start, end-start);
		assertElementsEqual(
			"Unexpected selection", 
			"Y [in X$Y.class [in <default> [in lib15.jar [in P]]]]",
			elements);
	} finally {
		deleteProject("P");
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165900
public void test103() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Resolve/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n" + 
		"  <T extends Test1> void foo(T t) {}\n" + 
		"  <T extends Test2> void foo(T t) {}\n" + 
		"  void bar(Object o) {\n" + 
		"    foo(o);\n" + 
		"  }\n" + 
		"}\n" + 
		"class Test1 {\n" + 
		"}\n" + 
		"class Test2 {\n" + 
		"}");

	String str = this.workingCopies[0].getSource();
	int start = str.lastIndexOf("foo(o)");
	int length = "foo".length();
	IJavaScriptElement[] elements =  this.workingCopies[0].codeSelect(start, length, this.wcOwner);
	
	assertElementsEqual(
			"Unexpected elements",
			"foo(T) [in Test [in [Working copy] Test.java [in test [in src [in Resolve]]]]]",
			elements
		);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165900
public void test104() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Resolve/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n" + 
		"  <T extends Test1> T foo(Test3<T> t) {return null;}\n" + 
		"  <T extends Test2> T foo(Test3<T> t) {return null;}\n" + 
		"  void bar(Object o) {\n" + 
		"    foo(o);\n" + 
		"  }\n" + 
		"}\n" + 
		"class Test1 {\n" + 
		"}\n" + 
		"class Test2 {\n" + 
		"}\n" + 
		"class Test3 <U> {\n" + 
		"}");

	String str = this.workingCopies[0].getSource();
	int start = str.lastIndexOf("foo(o)");
	int length = "foo".length();
	IJavaScriptElement[] elements =  this.workingCopies[0].codeSelect(start, length, this.wcOwner);
	
	assertElementsEqual(
			"Unexpected elements",
			"foo(Test3<T>) [in Test [in [Working copy] Test.java [in test [in src [in Resolve]]]]]",
			elements
		);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165900
public void test105() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Resolve/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n" + 
		"  <T extends Test1> T foo(Test3<T> t) {return null;}\n" + 
		"  <T extends Test2> T foo(Test3<T> t) {return null;}\n" + 
		"  void bar(Test3 o) {\n" + 
		"    foo(o);\n" + 
		"  }\n" + 
		"}\n" + 
		"class Test1 {\n" + 
		"}\n" + 
		"class Test2 {\n" + 
		"}\n" + 
		"class Test3 <U> {\n" + 
		"}");

	String str = this.workingCopies[0].getSource();
	int start = str.lastIndexOf("foo(o)");
	int length = "foo".length();
	IJavaScriptElement[] elements =  this.workingCopies[0].codeSelect(start, length, this.wcOwner);
	
	assertElementsEqual(
			"Unexpected elements",
			"foo(Test3<T>) [in Test [in [Working copy] Test.java [in test [in src [in Resolve]]]]]",
			elements
		);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165900
public void test106() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Resolve/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n" + 
		"  <T extends Test1> T foo(Test3<T> t) {return null;}\n" + 
		"  <T extends Test2> T foo(Test3<T> t) {return null;}\n" + 
		"  void bar(Test3<Object> o) {\n" + 
		"    foo(o);\n" + 
		"  }\n" + 
		"}\n" + 
		"class Test1 {\n" + 
		"}\n" + 
		"class Test2 {\n" + 
		"}\n" + 
		"class Test3 <U> {\n" + 
		"}");

	String str = this.workingCopies[0].getSource();
	int start = str.lastIndexOf("foo(o)");
	int length = "foo".length();
	IJavaScriptElement[] elements =  this.workingCopies[0].codeSelect(start, length, this.wcOwner);
	
	assertElementsEqual(
			"Unexpected elements",
			"foo(Test3<T>) [in Test [in [Working copy] Test.java [in test [in src [in Resolve]]]]]",
			elements
		);
}
}