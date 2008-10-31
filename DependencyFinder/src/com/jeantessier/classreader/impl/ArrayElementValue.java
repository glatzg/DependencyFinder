/*
 *  Copyright (c) 2001-2008, Jean Tessier
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of Jean Tessier nor the names of his contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jeantessier.classreader.impl;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

import com.jeantessier.classreader.*;

public class ArrayElementValue extends ElementValue implements com.jeantessier.classreader.ArrayElementValue {
    private Collection<ElementValue> values = new ArrayList<ElementValue>();

    public ArrayElementValue(Classfile classfile, DataInput in) throws IOException {
        super(classfile);

        int numValues = in.readUnsignedShort();
        Logger.getLogger(getClass()).debug("Reading " + numValues + " value(s) ...");
        for (int i=0; i<numValues; i++) {
            Logger.getLogger(getClass()).debug("value " + i + ":");
            // TODO: Has to instantiate the right subclass of ElementValue, somehow
            char tag = (char) in.readUnsignedByte();
            values.add(new ClassElementValue(classfile, in));
        }
    }

    public Collection<? extends ElementValue> getValues() {
        return values;
    }

    public char getTag() {
        return '[';
    }

    public void accept(Visitor visitor) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}