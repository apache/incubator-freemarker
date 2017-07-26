/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.freemarker.core.userpkg;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.freemarker.core.Environment;
import org.apache.freemarker.core.TemplateException;
import org.apache.freemarker.core.model.CallPlace;
import org.apache.freemarker.core.model.TemplateModel;

public class TwoPositionalParamsDirective extends TestTemplateDirectiveModel {

    @Override
    public void execute(TemplateModel[] args, Writer out, Environment env, CallPlace callPlace)
            throws TemplateException, IOException {
        out.write("#p(");
        printParam("p1", args[0], out, true);
        printParam("p2", args[1], out);
        out.write(")");
    }

    @Override
    public int getPredefinedPositionalArgumentCount() {
        return 2;
    }

    @Override
    public boolean hasPositionalVarargsArgument() {
        return false;
    }

    @Override
    public int getNamedArgumentIndex(String name) {
        return -1;
    }

    @Override
    public int getNamedVarargsArgumentIndex() {
        return -1;
    }

    @Override
    public int getTotalArgumentCount() {
        return getPredefinedPositionalArgumentCount();
    }

    @Override
    public Collection<String> getPredefinedNamedArgumentNames() {
        return null;
    }
}