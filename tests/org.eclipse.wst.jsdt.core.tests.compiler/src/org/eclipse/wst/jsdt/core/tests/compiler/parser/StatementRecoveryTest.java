/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.parser;

import java.util.Locale;

import junit.framework.Test;

import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.wst.jsdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.wst.jsdt.core.tests.util.Util;
 
public class StatementRecoveryTest extends AbstractCompilerTest {
	public static final boolean ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY = true;
	
	public static boolean optimizeStringLiterals = false;
	public static long sourceLevel = ClassFileConstants.JDK1_3; //$NON-NLS-1$
	
static {
//	TESTS_NAMES = new String[] { "test0037"};
//	TESTS_RANGE = new int[] {10, 20};
}
public static Test suite() {
	return buildAllCompliancesTestSuite(StatementRecoveryTest.class);
}
public StatementRecoveryTest(String testName){
	super(testName);
}
public void checkParse(
	char[] source, 
	String expectedDietUnitToString,
	String expectedDietWithStatementRecoveryUnitToString,
	String expectedDietPlusBodyUnitToString,	
	String expectedDietPlusBodyWithStatementRecoveryUnitToString,	
	String expectedFullUnitToString,
	String expectedFullWithStatementRecoveryUnitToString,
	String testName) {

	/* using regular parser in DIET mode */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);
		parser.setMethodsFullRecovery(false);
		parser.setStatementsRecovery(false);
		
		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure" + testName,
			expectedDietUnitToString,
			computedUnitToString);
	}
	/* using regular parser in DIET mode and statementRecoveryEnabled */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure with statement recovery enabled" + testName,
			expectedDietWithStatementRecoveryUnitToString,
			computedUnitToString);
	}
	/* using regular parser in DIET mode + getMethodBodies */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);
		parser.setMethodsFullRecovery(false);
		parser.setStatementsRecovery(false);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure" + testName,
			expectedDietUnitToString,
			computedUnitToString);
		if (computedUnit.types != null) {
			for (int i = computedUnit.types.length; --i >= 0;){
				computedUnit.types[i].parseMethod(parser, computedUnit);
			}
		}
		computedUnitToString = computedUnit.toString();
		if (!expectedDietPlusBodyUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		
		assertEquals(
			"Invalid unit diet+body structure" + testName,
			expectedDietPlusBodyUnitToString,
			computedUnitToString);
	}
	/* using regular parser in DIET mode + getMethodBodies and statementRecoveryEnabled */
	{
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure" + testName,
			expectedDietWithStatementRecoveryUnitToString,
			computedUnitToString);
		if (computedUnit.types != null) {
			for (int i = computedUnit.types.length; --i >= 0;){
				computedUnit.types[i].parseMethod(parser, computedUnit);
			}
		}
		computedUnitToString = computedUnit.toString();
		if (!expectedDietPlusBodyWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		
		assertEquals(
			"Invalid unit diet+body structure with statement recovery enabled" + testName,
			expectedDietPlusBodyWithStatementRecoveryUnitToString,
			computedUnitToString);
	}
	/* using regular parser in FULL mode */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);
		parser.setMethodsFullRecovery(false);
		parser.setStatementsRecovery(false);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.parse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedFullUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit full structure" + testName,
			expectedFullUnitToString,
			computedUnitToString);

	}
	/* using regular parser in FULL mode and statementRecoveryEnabled */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.parse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedFullWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit full structure with statement recovery enabled" + testName,
			expectedFullWithStatementRecoveryUnitToString,
			computedUnitToString);

	}
}

public void test0001() {

	String s = 
			"  function foo() {								\n"
			+ "    System.out.println();					\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
    	 "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		expectedDietPlusBodyUnitToString;
	
	String expectedFullUnitToString =
		expectedDietPlusBodyUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedFullUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0002() {

	String s = 
		"  function foo() {								\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

public void test0002a() {

	String s = 
		  "    System.out.print1();					\n"
		 + "    #                    					\n"
		+ "    System.out.print2();					\n"
		+ "											\n"; 	

	String expectedDietUnitToString = 
   	    "System.out.print1();\n"+
   	    "System.out.print2();\n"+
 		"";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
   	    "  System.out.println();\n"+
		"\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
   	    "System.out.print1();\n"+
   	    "System.out.print2();\n"+
 		"";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

public void test0002b() {

	String s = 
		"  foo = function () {								\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}




public void test0002c() {

	String s = 
		"  obj ={								\n"
		+"   p1 : 2,							\n"
		+" meth: function () {								\n"
		+ "    #                    					\n"
		+ "    FOO();					\n"
		+ "}											\n" 	
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}



public void test0002d() {

	String s = 
		" a= foo{								\n"
		+"  function () {								\n"
		+ "    #                    					\n"
		+ "    bar();					\n"
		+ "}											\n" 	
		+ ");											\n"; 	

	String expectedDietUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}




public void test0003() {

	String s = 
		"  function foo() {								\n"
		+ "    System.out.println();					\n"
		+ "    #                    					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}


public void test0003b() {

	String s = 
		"  function foo1() {								\n"
		+ "    var i;					\n"
		+ "}											\n" 	
		+ "  function foo2() {								\n"
		+ "    #                    					\n"
		+ "    a=1;					\n"
		+ "}											\n" 	
		+ "  function foo3() {								\n"
		+ "    var k;					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}



public void test0004() {

	String s = 
		"  function foo() {								\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0005() {

	String s = 
		"  function foo() {								\n"
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+ "    #                    					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0006() {

	String s = 
		"function foo() {\n"  
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+"}\n";

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0007() {

	String s = 
		"function foo() {\n"  
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "    if(true) {								\n"
		+ "      System.out.println();					\n"
		+ "    }										\n"
		+ "    System.out.println();					\n"
		+"}\n";

	String expectedDietUnitToString = 
		"function foo() {\n" 
		+ "  System.out.println();\n"
		+ "  if (true)\n" 
		+ "      {\n"
		+ "        System.out.println();\n"
		+ "      }\n"
		+ "  System.out.println();\n"
		+"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" 
		+ "  System.out.println();\n"
		+ "  if (true)\n" 
		+ "      {\n"
		+ "        System.out.println();\n"
		+ "      }\n"
		+ "  System.out.println();\n"
		+"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" 
		+ "  System.out.println();\n"
		+ "  if (true)\n" 
		+ "      {\n"
		+ "        System.out.println();\n"
		+ "      }\n"
		+ "  System.out.println();\n"
		+"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0008() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    if(true) {								\n"
			+ "      System.out.println();					\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #                    					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0009() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    if(true) {								\n"
			+ "      System.out.println();					\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #                    					\n"
			+ "    System.out.println();					\n"
			+ "    if(true) {								\n"
			+ "      System.out.println();					\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0010() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {}							\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietPlusBodyUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietPlusBodyUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0011() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {}							\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0012() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {}							\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0013() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {}							\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    class Y {								\n"
			+ "      void foo() {}							\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    class Y {\n" + 
		"      Y() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0014() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0015() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0016() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0017() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0018() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0019() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0020() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0021() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {}							\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietPlusBodyUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietPlusBodyUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0022() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {}							\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0023() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void bar() {}							\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void bar() {\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0024() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void bar() {}							\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void bar() {}							\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void bar() {\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void bar() {\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0025() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0026() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0027() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0028() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0029() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0030() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0031() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"        System.out.println();\n" + 
		"        if (true)\n" + 
		"            {\n" + 
		"              System.out.println();\n" + 
		"            }\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0032() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    System.out.println();					\n"
			+ "    bar(new Object() {						\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "        #									\n"
			+ "        System.out.println();				\n"
			+ "        if(true) {							\n"
			+ "          System.out.println();				\n"
			+ "        }									\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    });										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    bar(new Object() {\n" + 
		"  () {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"});\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0033() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    class Z {								\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    class Z {\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    }\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0034() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    #										\n"
			+ "    new Object() {							\n"
			+ "      void foo() {							\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    };										\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    new Object() {\n" + 
		"      () {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"      void foo() {\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"    };\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0035() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    bar(\\u0029								\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    bar();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0036() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    if(true) {								\n"
			+ "      foo();									\n"
			+ "    }										\n"
			+ "    for(;									\n"
			+ "    if(true) {								\n"
			+ "      foo();									\n"
			+ "    }										\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          foo();\n" + 
		"        }\n" + 
		"    for (; ; ) \n" + 
		"      ;\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          foo();\n" + 
		"        }\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0037() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    if() {									\n"
			+ "      foo();									\n"
			+ "    }										\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    if ($missing$)\n" + 
		"        {\n" + 
		"          foo();\n" + 
		"        }\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0038() {
	String s = 
		"package p1;										\n"+
			"public class A {									\n"+
			"	public interface B {							\n"+
			"		public abstract void aMethod (int A);		\n"+
			"		public interface C {						\n"+
			"			public abstract void anotherMethod(int A);\n"+
			"		}											\n"+
			"	}												\n"+
			"	public class aClass implements B, B.C {			\n"+
			"		public void aMethod (int A) {				\n"+
			"			public void anotherMethod(int A) {};	\n"+
			"		}											\n"+
			"	}												\n"+
			"   	public static void main (String argv[]) {	\n"+
			"		System.out.println(\"SUCCESS\");			\n"+
			"	}												\n"+
			"}";

	String expectedDietUnitToString = 
		"package p1;\n" + 
		"public class A {\n" + 
		"  public interface B {\n" + 
		"    public interface C {\n" + 
		"      public abstract void anotherMethod(int A);\n" + 
		"    }\n" + 
		"    public abstract void aMethod(int A);\n" + 
		"  }\n" + 
		"  public class aClass implements B, B.C {\n" + 
		"    public aClass() {\n" + 
		"    }\n" + 
		"    public void aMethod(int A) {\n" + 
		"    }\n" + 
		"  }\n" + 
		"  public A() {\n" + 
		"  }\n" + 
		"  public static void main(String[] argv) {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package p1;\n" + 
		"public class A {\n" + 
		"  public interface B {\n" + 
		"    public interface C {\n" + 
		"      public abstract void anotherMethod(int A);\n" + 
		"    }\n" + 
		"    public abstract void aMethod(int A);\n" + 
		"  }\n" + 
		"  public class aClass implements B, B.C {\n" + 
		"    public aClass() {\n" + 
		"      super();\n" + 
		"    }\n" + 
		"    public void aMethod(int A) {\n" + 
		"    }\n" + 
		"  }\n" + 
		"  public A() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  public static void main(String[] argv) {\n" + 
		"    System.out.println(\"SUCCESS\");\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package p1;\n" + 
		"public class A {\n" + 
		"  public interface B {\n" + 
		"    public interface C {\n" + 
		"      public abstract void anotherMethod(int A);\n" + 
		"    }\n" + 
		"    public abstract void aMethod(int A);\n" + 
		"  }\n" + 
		"  public class aClass implements B, B.C {\n" + 
		"    public aClass() {\n" + 
		"      super();\n" + 
		"    }\n" + 
		"    public void aMethod(int A) {\n" + 
		"      public void anotherMethod;\n" + 
		"      int A;\n" + 
		"      ;\n" + 
		"    }\n" + 
		"  }\n" + 
		"  public A() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  public static void main(String[] argv) {\n" + 
		"    System.out.println(\"SUCCESS\");\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		"package p1;\n" + 
		"public class A {\n" + 
		"  public interface B {\n" + 
		"    public interface C {\n" + 
		"      public abstract void anotherMethod(int A);\n" + 
		"    }\n" + 
		"    public abstract void aMethod(int A);\n" + 
		"  }\n" + 
		"  public class aClass implements B, B.C {\n" + 
		"    public aClass() {\n" + 
		"    }\n" + 
		"    public void aMethod(int A) {\n" + 
		"    }\n" + 
		"    public void anotherMethod(int A) {\n" + 
		"    }\n" + 
		"  }\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public A() {\n" + 
		"  }\n" + 
		"  public static void main(String[] argv) {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedFullUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0039() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  {											\n"
			+ "    System.out.println();					\n"
			+ "    foo()									\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    System.out.println();\n" + 
		"    foo();\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0040() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  {											\n"
			+ "    System.out.println();					\n"
			+ "    class Y {								\n"
			+ "      {										\n"
			+ "        System.out.println();				\n"
			+ "        foo()								\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    System.out.println();\n" + 
		"    class Y {\n" + 
		"      {\n" + 
		"        System.out.println();\n" + 
		"        foo();\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"      Y() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0041() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  {											\n"
			+ "    System.out.println();					\n"
			+ "    class Y {								\n"
			+ "      {										\n"
			+ "        System.out.println();				\n"
			+ "        foo()								\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    class Z {								\n"
			+ "      {										\n"
			+ "        System.out.println();				\n"
			+ "        foo()								\n"
			+ "        System.out.println();				\n"
			+ "      }										\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    foo()									\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    System.out.println();\n" + 
		"    class Y {\n" + 
		"      {\n" + 
		"        System.out.println();\n" + 
		"        foo();\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"      Y() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"    class Z {\n" + 
		"      {\n" + 
		"        System.out.println();\n" + 
		"        foo();\n" + 
		"        System.out.println();\n" + 
		"      }\n" + 
		"      Z() {\n" + 
		"        super();\n" + 
		"      }\n" + 
		"    }\n" + 
		"    System.out.println();\n" + 
		"    foo();\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0042() {

	String s = 
		"package a;											\n"
			+ "public class X {								\n"
			+ "  void foo() {								\n"
			+ "    for(int i								\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public class X {\n" + 
		"  public X() {\n" + 
		"    super();\n" + 
		"  }\n" + 
		"  void foo() {\n" + 
		"    for (int i;; ; ) \n" + 
		"      ;\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

//https://bugs.eclipse.org/bugs/show_bug.cgi?id=80339
public void test0043() {

	String s = 
		"package a;											\n"
			+ "public interface Test {						\n"
			+ "  public void myMethod()						\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"package a;\n" + 
		"public interface Test {\n" + 
		"  public void myMethod() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"package a;\n" + 
		"public interface Test {\n" + 
		"  public void myMethod() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"package a;\n" + 
		"public interface Test {\n" + 
		"  public void myMethod() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

}