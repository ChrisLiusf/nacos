/*
 * Copyright (C) 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.nacos.naming.push;

import com.alibaba.nacos.api.naming.push.AckEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pbting
 * @date 2019-08-28 9:30 AM
 */
public abstract class AbstractReTransmitter implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractReTransmitter.class);

    protected PushService pushService;
    protected AckEntry ackEntry;

    public AbstractReTransmitter(PushService pushService, AckEntry ackEntry) {
        this.pushService = pushService;
        this.ackEntry = ackEntry;
    }

    @Override
    public void run() {
        // Receive the client's response message correctly
        if (!pushService.containsAckEntry(ackEntry.getKey())) {
            return;
        }
        logger.warn("No client response message was received for more than {} seconds", PushService.ACK_TIMEOUT_SECONDS);
        reTransmitter(pushService);
    }

    /**
     * re-transmitter by the push service
     *
     * @param pushService
     */
    public abstract void reTransmitter(PushService pushService);
}
