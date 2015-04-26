/*Janino - An embedded Java[TM] compiler
 *
 * Copyright (c) 2001-2010, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IExpressionEvaluator;

/**
 * A test program that allows you to play around with the
 * {@link org.codehaus.janino.ExpressionEvaluator ExpressionEvaluator} class.
 */
@SuppressWarnings("rawtypes")
class StringExpression extends DemoBase {

	private Class optionalExpressionType = null;
	private String[] parameterNames = {};
	private Class[] parameterTypes = {};
	private Class[] thrownExceptions = {};
	private String[] optionalDefaultImports = null;
	private IExpressionEvaluator ee;
	private Object[] arguments;

	/**/
	public StringExpression(String[] args) throws Exception {
		int i;
		for (i = 0; i < args.length; ++i) {
			String arg = args[i];
			if (!arg.startsWith("-"))
				break;
			if ("-et".equals(arg)) {
				optionalExpressionType = DemoBase.stringToType(args[++i]);
			} else if ("-pn".equals(arg)) {
				parameterNames = DemoBase.explode(args[++i]);
			} else if ("-pt".equals(arg)) {
				parameterTypes = DemoBase.stringToTypes(args[++i]);
			} else if ("-te".equals(arg)) {
				thrownExceptions = DemoBase.stringToTypes(args[++i]);
			} else if ("-di".equals(arg)) {
				optionalDefaultImports = DemoBase.explode(args[++i]);
			} else if ("-help".equals(arg)) {
				System.err.println("Usage:");
				System.err
						.println("  ExpressionDemo { <option> } <expression> { <parameter-value> }");
				System.err
						.println("Compiles and evaluates the given expression and prints its value.");
				System.err.println("Valid options are");
				System.err
						.println(" -et <expression-type>                        (default: any)");
				System.err
						.println(" -pn <comma-separated-parameter-names>        (default: none)");
				System.err
						.println(" -pt <comma-separated-parameter-types>        (default: none)");
				System.err
						.println(" -te <comma-separated-thrown-exception-types> (default: none)");
				System.err
						.println(" -di <comma-separated-default-imports>        (default: none)");
				System.err.println(" -help");
				System.err
						.println("The number of parameter names, types and values must be identical.");
				System.exit(0);
			} else {
				System.err.println("Invalid command line option \"" + arg
						+ "\"; try \"-help\".");
				System.exit(1);
			}
		}

		if (i >= args.length) {
			System.err.println("Expression missing; try \"-help\".");
			System.exit(1);
		}
		final String expression = args[i++];

		if (parameterTypes.length != parameterNames.length) {
			System.err.println("Parameter type count (" + parameterTypes.length
					+ ") and parameter name count (" + parameterNames.length
					+ ") do not match; try \"-help\".");
			System.exit(1);
		}

		// Create "ExpressionEvaluator" object.
		ee = CompilerFactoryFactory.getDefaultCompilerFactory()
				.newExpressionEvaluator();
		ee.setExpressionType(optionalExpressionType);
		ee.setDefaultImports(optionalDefaultImports);
		ee.setParameters(parameterNames, parameterTypes);
		ee.setThrownExceptions(thrownExceptions);
		ee.cook(expression);
	}

	public IExpressionEvaluator getExpression() throws Exception {
		return ee;
	}

	public Class[] getParameterType() {
		return parameterTypes;
	}
}
