/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

James G Willmore - LJ Computing - (C) 2023
*/
package net.ljcomputing.insurancexml.validation;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalEventHandler implements ValidationEventHandler {

    @Override
    public boolean handleEvent(ValidationEvent event) {
        log.error("VALIDATION EVENT: ");
        log.error("  SEVERITY:  {}", event.getSeverity());
        log.error("  MESSAGE:  {}", event.getMessage());
        // log.error("  LINKED EXCEPTION: ", event.getLinkedException());
        // log.error("  LINE NUMBER:  {}", event.getLocator().getLineNumber());
        // log.error("  COLUMN NUMBER:  {}", event.getLocator().getColumnNumber());
        // log.error("  OFFSET:  {}", event.getLocator().getOffset());
        log.error("  OBJECT:  {}", event.getLocator().getObject());
        // log.error("  NODE:  {}", event.getLocator().getNode());
        // log.error("  URL:  {}", event.getLocator().getURL());
        return false;
    }
}
