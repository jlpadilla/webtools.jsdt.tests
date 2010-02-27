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
package org.eclipse.wst.jsdt.debug.rhino.tests.connect;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.DisconnectedException;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.TimeoutException;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.connect.TransportService;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.connect.TransportService.ListenerKey;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Connection;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Packet;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.PipedTransportService;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;

public class DebugSessionTest extends TestCase {

	boolean complete = false;
	
	public void testReceiveEvent() throws IOException, InterruptedException, TimeoutException, DisconnectedException {
		final TransportService service = new PipedTransportService();
		final ListenerKey key = service.startListening("9000");

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				DebugSession runtime = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);

					runtime = new DebugSession(c);
					runtime.sendEvent(new EventPacket("test"));
					synchronized (Thread.currentThread()) {
						Thread.currentThread().wait();
					}
				} catch (IOException e) {
					e.printStackTrace();
					fail();
				} catch (DisconnectedException e) {
					fail();
				} catch (InterruptedException e) {
					fail();
				} finally {
					if (runtime != null)
						runtime.dispose();
				}
			}
		};
		t.start();

		Connection c = service.attach("9000", 5000, 5000);
		assertNotNull(c);
		DebugSession session = new DebugSession(c);
		try {
			EventPacket event = session.receiveEvent(5000);
			assertTrue(event.getEvent().equals("test"));
		} finally {
			session.dispose();
			synchronized (t) {
				t.notify();
			}
		}
		t.join(5000);
		service.stopListening(key);
	}

	public void testSendRequestReceiveResponse() throws IOException, InterruptedException, DisconnectedException, TimeoutException {
		final TransportService service = new PipedTransportService();
		final ListenerKey key = service.startListening("9000");

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				DebugSession runtime = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);

					runtime = new DebugSession(c);
					Request request = runtime.receiveRequest(5000);
					runtime.sendResponse(new Response(request.getSequence(), request.getCommand()));
					synchronized (Thread.currentThread()) {
						Thread.currentThread().wait();
					}
				} catch (IOException e) {
					e.printStackTrace();
					fail();
				} catch (DisconnectedException e) {
					fail();
				} catch (InterruptedException e) {
					fail();
				} catch (TimeoutException e) {
					fail();
				} finally {
					if (runtime != null)
						runtime.dispose();
				}
			}
		};
		t.start();

		Connection c = service.attach("9000", 5000, 5000);
		assertNotNull(c);
		DebugSession session = new DebugSession(c);
		try {
			Request request = new Request("test");
			session.sendRequest(request);
			Response response = session.receiveResponse(request.getSequence(), 5000);
			assertTrue(response.getCommand().equals("test"));
			assertTrue(response.getRequestSequence() == request.getSequence());
		} finally {
			session.dispose();
			synchronized (t) {
				t.notify();
			}
		}
		t.join(5000);
		service.stopListening(key);
	}
	
	public void testSendEvent() throws IOException, InterruptedException {
		final TransportService service = new PipedTransportService();
		final ListenerKey key = service.startListening("9000");

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				DebugSession runtime = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);

					runtime = new DebugSession(c);
					runtime.sendEvent(new EventPacket("test"));
					synchronized (Thread.currentThread()) {
						if (!complete)
							Thread.currentThread().wait(5000);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail();
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (runtime != null)
						runtime.dispose();
				}
			}
		};
		t.start();

		Connection c = service.attach("9000", 5000, 5000);
		assertNotNull(c);
		try {
			Packet packet = c.readPacket();
			assertTrue(packet instanceof EventPacket);
			EventPacket event = (EventPacket) packet;
			assertTrue(event.getEvent().equals("test"));
		} finally {
			c.close();
			synchronized (t) {
				complete = true;
				t.notify();
			}
		}
		t.join(5000);
		service.stopListening(key);
	}

	public void testReceiveRequestSendResponse() throws IOException, InterruptedException {
		final TransportService service = new PipedTransportService();
		final ListenerKey key = service.startListening("9000");

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				DebugSession runtime = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);

					runtime = new DebugSession(c);
					Request request = runtime.receiveRequest(5000);
					runtime.sendResponse(new Response(request.getSequence(), request.getCommand()));
					synchronized (Thread.currentThread()) {
						Thread.currentThread().wait();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail();
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (runtime != null)
						runtime.dispose();
				}
			}
		};
		t.start();

		Connection c = service.attach("9000", 5000, 5000);
		assertNotNull(c);
		try {
			Request request = new Request("test");
			c.writePacket(request);
			Packet packet = c.readPacket();
			assertTrue(packet instanceof Response);
			Response response = (Response) packet;
			assertTrue(response.getCommand().equals("test"));
			assertTrue(response.getRequestSequence() == request.getSequence());
		} finally {
			c.close();
			synchronized (t) {
				t.notify();
			}
		}
		t.join(5000);
		service.stopListening(key);
	}
}
