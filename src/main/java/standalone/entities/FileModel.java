/* *****************************************************************************
 * Copyright (c) {2017} {Software Design and Collaboration Laboratory (SDCL)
 *				, University of California, Irvine}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {Software Design and Collaboration Laboratory (SDCL)
 *	, University of California, Irvine}
 *			- initial API and implementation and/or initial documentation
 *******************************************************************************/

/*
 * Created by Thomas Kwak
 */

package standalone.entities;

import standalone.entities.JavaClass;

import java.util.ArrayList;
import java.util.List;

public class FileModel {

	private List<JavaClass> javaClassList;

	public FileModel() {
		this.javaClassList = new ArrayList<>();
	}

	public void addJavaClass(JavaClass co) {
		javaClassList.add(co);
	}

	List<JavaClass> getJavaClassList() {
		return this.javaClassList;
	}

}
