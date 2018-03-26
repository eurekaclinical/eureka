package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*-
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2016 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import au.com.bytecode.opencsv.CSVParser;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.protempa.dest.table.ConstantColumnSpec;
import org.protempa.dest.table.Link;
import org.protempa.dest.table.OutputConfig;
import org.protempa.dest.table.PropositionColumnSpec;
import org.protempa.dest.table.Reference;

/**
 *
 * @author Andrew Post
 */
public class TableColumnSpecFormat extends Format {

	private static final long serialVersionUID = 1L;
	private final String columnName;
	private final Format positionFormat;

	public TableColumnSpecFormat(String columnName) {
		this(columnName, null);
	}

	public TableColumnSpecFormat(String columnName, Format positionFormat) {
		this.columnName = columnName;
		this.positionFormat = positionFormat;
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if (!(obj instanceof TableColumnSpecWrapper)) {
			throw new IllegalArgumentException(
					"This Format only formats objects of type " + TableColumnSpecWrapper.class.getName()
					+ "; you supplied a " + obj.getClass().getName());
		}
		TableColumnSpecWrapper theObj = (TableColumnSpecWrapper) obj;
		throw new IllegalArgumentException("Not supported yet.");
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		try {
			Object result = doParse(source.substring(pos.getIndex()));
			pos.setIndex(source.length());
			return result;
		} catch (IOException ex) {
			pos.setErrorIndex(0);
			return null;
		}
	}

	private TableColumnSpecWrapper doParse(String links) throws IOException {
		if (links != null) {
			if (links.startsWith("[")) {
				CSVParser referenceNameParser = new CSVParser(',');
				String tokens = "[ ]>.$";
				char[] tokensArr = tokens.toCharArray();
				StringTokenizer st = new StringTokenizer(links, tokens, true);
				String lastToken = null;
				String propId = null;
				String propType = null;
				String referenceNames = null;
				String propertyName = null;
				boolean inPropSpec = false;
				int index = 0;
				List<Link> linksList = new ArrayList<>();
				String firstPropId = null;
				OUTER:
				while (st.hasMoreTokens()) {
					String nextToken = st.nextToken();
					for (char token : tokensArr) {
						if (nextToken.charAt(0) == token) {
							lastToken = nextToken;
							if (token == ']') {
								inPropSpec = false;
								//do something with propId
								if (referenceNames != null) {
									String[] parseLine = referenceNameParser.parseLine(referenceNames);
									if (parseLine.length < 1 || parseLine.length > 2) {
										String msg = MessageFormat.format("Invalid reference: expected referenceName[,backReferenceName] but was {1}", new Object[]{referenceNames});
										throw new IOException(msg);
									}
									linksList.add(new Reference(new String[]{parseLine[0]}, new String[]{propId}));
									referenceNames = null;
								}
								propId = null;
								propType = null;
							}
							continue OUTER;
						}
					}
					switch (lastToken) {
						case "[":
							inPropSpec = true;
							propId = nextToken;
							if (firstPropId == null) {
								firstPropId = propId;
							}
							break;
						case " ":
							if (inPropSpec) {
								if (propType == null) {
									propType = nextToken;
								} else {
									index = Integer.parseInt(nextToken);
								}
							}
							break;
						case ">":
							referenceNames = nextToken;
							break;
						case ".":
							propertyName = nextToken;
							break;
						case "$":
							if ("value".equals(propertyName)) {
								//print a value
							} else {
								//print a property value
							}
					}
				}
				OutputConfig outputConfig = null;
				if (propertyName != null) {
					switch (propertyName) {
						case "value":
							propertyName = null;
							outputConfig = new OutputConfig(false, true, false, false, false, false, false, false, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, null, this.positionFormat);
							break;
						case "position":
						case "start":
							propertyName = null;
							outputConfig = new OutputConfig(false, false, false, false, true, false, false, false, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, null, this.positionFormat);
							break;
						case "finish":
							propertyName = null;
							outputConfig = new OutputConfig(false, false, false, false, false, true, false, false, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, null, this.positionFormat);
							break;
						case "uniqueId":
							propertyName = null;
							outputConfig = new OutputConfig(false, false, false, false, false, false, false, true, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, null, this.positionFormat);
							break;
						default:
							Map<String, String> propertyHeadings = new HashMap<>();
							propertyHeadings.put(propertyName, this.columnName);
							outputConfig = new OutputConfig(false, false, false, false, false, false, false, false, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, this.columnName, propertyHeadings, this.positionFormat);
					}
				}
				return new TableColumnSpecWrapper(firstPropId, new PropositionColumnSpec(this.columnName, propertyName != null ? new String[]{propertyName} : null, outputConfig, null, linksList.toArray(new Link[linksList.size()]), 1));
			} else {
				return new TableColumnSpecWrapper(null, new ConstantColumnSpec(this.columnName, links));
			}
		} else {
			return null;
		}
	}

}
