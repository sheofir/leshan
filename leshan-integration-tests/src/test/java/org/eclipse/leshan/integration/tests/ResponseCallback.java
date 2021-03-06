/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Zebra Technologies - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.integration.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.core.response.ExceptionConsumer;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ResponseConsumer;

public class ResponseCallback<T extends LwM2mResponse> implements ResponseConsumer<T>, ExceptionConsumer {

    private final CountDownLatch latch;
    private final AtomicBoolean called;
    private T response;

    public ResponseCallback() {
        called = new AtomicBoolean(false);
        latch = new CountDownLatch(1);
    }

    @Override
    public void accept(T response) {
        this.response = response;
        called.set(true);
        latch.countDown();
    }

    @Override
    public void accept(Exception e) {
        called.set(true);
        latch.countDown();
    }

    public AtomicBoolean isCalled() {
        return called;
    }

    public void waitForResponse(long timeout) throws InterruptedException {
        latch.await(timeout, TimeUnit.MILLISECONDS);
    }

    public ResponseCode getResponseCode() {
        return response.getCode();
    }

    public void reset() {
        called.set(false);
    }

    public T getResponse() {
        return response;
    }
}