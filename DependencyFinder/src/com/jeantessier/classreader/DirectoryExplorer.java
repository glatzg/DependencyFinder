/*
 *  Copyright (c) 2001-2004, Jean Tessier
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  	* Redistributions of source code must retain the above copyright
 *  	  notice, this list of conditions and the following disclaimer.
 *  
 *  	* Redistributions in binary form must reproduce the above copyright
 *  	  notice, this list of conditions and the following disclaimer in the
 *  	  documentation and/or other materials provided with the distribution.
 *  
 *  	* Neither the name of Jean Tessier nor the names of his contributors
 *  	  may be used to endorse or promote products derived from this software
 *  	  without specific prior written permission.
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

package com.jeantessier.classreader;

import java.io.*;
import java.util.*;

public class DirectoryExplorer {
	private Collection collection = new LinkedList();

	public DirectoryExplorer(String[] filenames) throws IOException {
		for (int i=0; i<filenames.length; i++) {
			explore(new File(filenames[i]));
		}
	}

	public DirectoryExplorer(Collection filenames) throws IOException {
		Iterator i = filenames.iterator();
		while (i.hasNext()) {
			explore(new File(i.next().toString()));
		}
	}

	public DirectoryExplorer(String filename) throws IOException {
		this(new File(filename));
	}

	public DirectoryExplorer(File file) throws IOException {
		explore(file);
	}

	private void explore(File file) throws IOException {
		if (file.exists()) {
			collection.add(file);
			
			if (file.isDirectory()) {
				exploreDirectory(file);
			}
		}
	}

	private void exploreDirectory(File dir) throws IOException {
		String[] entries = dir.list();
		for (int i=0; i<entries.length; i++) {
			explore(new File(dir, entries[i]));
		}
	}

	public Collection getCollection() {
		return collection;
	}
}
