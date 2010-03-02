/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import org.eclipse.wst.jsdt.debug.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.transport.TimeoutException;

public class RequestVersionTest extends RequestTest {

	public void testVersion() throws DisconnectedException, TimeoutException {
		Request request = new Request("version");
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.getBody().containsKey("javascript.version"));
		assertTrue(response.getBody().containsKey("ecmascript.version"));
		assertTrue(response.getBody().containsKey("javascript.vm.vendor"));
		assertTrue(response.getBody().containsKey("javascript.vm.name"));
		assertTrue(response.getBody().containsKey("javascript.vm.version"));
	}
}