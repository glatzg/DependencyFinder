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

package com.jeantessier.dependencyfinder.gui;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.jeantessier.classreader.*;
import com.jeantessier.metrics.*;

public class MetricsExtractAction extends AbstractAction implements Runnable {
	private OOMetrics model;
	private File[]    files;

	private ClassfileLoader loader;

	public MetricsExtractAction(OOMetrics model) {
		this.model = model;

		putValue(Action.LONG_DESCRIPTION, "Extract metrics from compiled classes");
		putValue(Action.NAME, "Extract");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("icons/extract.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(model.InputFile());
		chooser.addChoosableFileFilter(new JavaBytecodeFileFilter());
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		int returnValue = chooser.showDialog(model, "Extract");
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			files = chooser.getSelectedFiles();
			model.InputFile(files[0]);
			new Thread(this).start();
		}
	}

	public void run() {
		Date start = new Date();

		model.StatusLine().ShowInfo("Scanning ...");
		ClassfileScanner scanner = new ClassfileScanner();
		scanner.load(Arrays.asList(files));

		model.ProgressBar().setMaximum(scanner.NbFiles() + scanner.NbClasses());

		MetricsVerboseListener verbose_listener = new MetricsVerboseListener(model.StatusLine(), model.ProgressBar());
		
		loader = new AggregatingClassfileLoader();
		loader.addLoadListener(verbose_listener);
		loader.load(Arrays.asList(files));
		
		com.jeantessier.metrics.MetricsGatherer gatherer = new com.jeantessier.metrics.MetricsGatherer("Project", model.MetricsFactory());
		gatherer.addMetricsListener(verbose_listener);
		gatherer.visitClassfiles(loader.getAllClassfiles());

		// JDK 1.4 feature
		// model.ProgressBar().setIndeterminate(true);
		
		model.StatusLine().ShowInfo("Generating method results ...");
		model.MethodsModel().Metrics(model.MetricsFactory().MethodMetrics());
		
		model.StatusLine().ShowInfo("Generating class results ...");
		model.ClassesModel().Metrics(model.MetricsFactory().ClassMetrics());
		
		model.StatusLine().ShowInfo("Generating group results ...");
		model.GroupsModel().Metrics(model.MetricsFactory().GroupMetrics());
		
		model.StatusLine().ShowInfo("Generating project results ...");
		StringWriter out = new StringWriter();
		com.jeantessier.metrics.Printer printer = new com.jeantessier.metrics.TextPrinter(new PrintWriter(out), model.MetricsFactory().Configuration().ProjectMeasurements());
		printer.VisitMetrics(model.MetricsFactory().ProjectMetrics());
		model.ProjectArea().setText(out.toString());
		
		Date stop = new Date();
		
		model.StatusLine().ShowInfo("Done (" + ((stop.getTime() - start.getTime()) / (double) 1000) + " secs).");
		// JDK 1.4 feature
		// model.ProgressBar().setIndeterminate(false);
		model.setTitle("OO Metrics - Extractor");
	}
}
