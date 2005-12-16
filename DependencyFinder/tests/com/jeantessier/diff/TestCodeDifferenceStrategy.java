/*
 *  Copyright (c) 2001-2005, Jean Tessier
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

package com.jeantessier.diff;

import com.jeantessier.classreader.*;

public class TestCodeDifferenceStrategy extends TestDifferencesFactoryBase {
    private CodeDifferenceStrategy strategy;

    protected void setUp() throws Exception {
        super.setUp();

        strategy = new CodeDifferenceStrategy();
    }

    public void testUnmodifiedConstantValue() {
        ConstantValue_attribute oldValue = getOldJar().getClassfile("UnmodifiedPackage.UnmodifiedInterface").getField("unmodifiedField").getConstantValue();
        ConstantValue_attribute newValue = getNewJar().getClassfile("UnmodifiedPackage.UnmodifiedInterface").getField("unmodifiedField").getConstantValue();

        assertFalse(strategy.isConstantValueDifferent(oldValue, newValue));
    }

    public void testRemovedConstantValue() {
        ConstantValue_attribute oldValue = getOldJar().getClassfile("ModifiedPackage.ModifiedInterface").getField("removedField").getConstantValue();
        ConstantValue_attribute newValue = null;

        assertTrue(strategy.isConstantValueDifferent(oldValue, newValue));
    }

    public void testModifiedConstantValue() {
        ConstantValue_attribute oldValue = getOldJar().getClassfile("ModifiedPackage.ModifiedInterface").getField("modifiedValueField").getConstantValue();
        ConstantValue_attribute newValue = getNewJar().getClassfile("ModifiedPackage.ModifiedInterface").getField("modifiedValueField").getConstantValue();

        assertTrue(strategy.isConstantValueDifferent(oldValue, newValue));
    }

    public void testNewConstantValue() {
        ConstantValue_attribute oldValue = null;
        ConstantValue_attribute newValue = getNewJar().getClassfile("ModifiedPackage.ModifiedInterface").getField("newField").getConstantValue();

        assertTrue(strategy.isConstantValueDifferent(oldValue, newValue));
    }

    public void testUnmodifiedCode() {
        Code_attribute oldCode = getOldJar().getClassfile("UnmodifiedPackage.UnmodifiedClass").getMethod("unmodifiedMethod()").getCode();
        Code_attribute newCode = getNewJar().getClassfile("UnmodifiedPackage.UnmodifiedClass").getMethod("unmodifiedMethod()").getCode();

        assertFalse(strategy.isCodeDifferent(oldCode, newCode));
    }

    public void testModifiedCode() {
        Code_attribute oldCode = getOldJar().getClassfile("ModifiedPackage.ModifiedClass").getMethod("modifiedCodeMethod()").getCode();
        Code_attribute newCode = getNewJar().getClassfile("ModifiedPackage.ModifiedClass").getMethod("modifiedCodeMethod()").getCode();

        assertTrue(strategy.isCodeDifferent(oldCode, newCode));
    }
}