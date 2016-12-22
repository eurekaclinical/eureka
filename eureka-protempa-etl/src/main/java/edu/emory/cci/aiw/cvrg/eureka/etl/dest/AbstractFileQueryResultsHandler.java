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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import org.protempa.PropositionDefinition;
import org.protempa.dest.AbstractQueryResultsHandler;
import org.protempa.dest.QueryResultsHandlerCloseException;
import org.protempa.dest.QueryResultsHandlerProcessingException;

/**
 *
 * @author Andrew Post
 */
public abstract class AbstractFileQueryResultsHandler extends AbstractQueryResultsHandler {

    private OutputStream outputFileOutputStream;
    private final String name;
    private final EtlProperties etlProperties;
    private final String fileName;
	private final FileSupport fileSupport;
    
    protected AbstractFileQueryResultsHandler(DestinationEntity destinationEntity, EtlProperties etlProperties) {
		this.fileSupport = new FileSupport();
        this.name = destinationEntity.getName();
        this.fileName = this.fileSupport.getOutputName(destinationEntity);
        this.etlProperties = etlProperties;
    }

    /**
     * Creates an output stream and calls {@link #start(java.io.OutputStream, java.util.Collection) }.
     * @param cache
     * @throws QueryResultsHandlerProcessingException if an error occurred.
     */
    @Override
    public final void start(Collection<PropositionDefinition> cache) throws QueryResultsHandlerProcessingException {
        try {
            File outputFile = new File(this.etlProperties.outputFileDirectory(this.name), this.fileName);
            this.outputFileOutputStream = new FileOutputStream(outputFile);
            start(this.outputFileOutputStream, cache);
        } catch (IOException ex) {
            throw new QueryResultsHandlerProcessingException("Error starting output", ex);
        }
    }

    /**
     * Calls {@link #cleanup() }, and then closes the output stream that is passed into {@link #start(java.io.OutputStream, java.util.Collection) }.
     * @throws QueryResultsHandlerCloseException if an error occurred.
     */
    @Override
    public void close() throws QueryResultsHandlerCloseException {
        try {
            cleanup();
            this.outputFileOutputStream.close();
        } catch (QueryResultsHandlerCloseException ex) {
            try {
                this.outputFileOutputStream.close();
            } catch (IOException suppress) {
                ex.addSuppressed(suppress);
            }
            throw new QueryResultsHandlerCloseException("Error closing", ex);
        } catch (IOException ex) {
            throw new QueryResultsHandlerCloseException("Error closing", ex);
        }
    }

    /**
     * Called by {@link #start(java.util.Collection) } to given subclasses an opportunity to create
     * resources and access the output stream.
     * 
     * @param outputStream
     * @param cache
     * @throws QueryResultsHandlerProcessingException 
     */
    protected abstract void start(OutputStream outputStream, Collection<PropositionDefinition> cache) throws QueryResultsHandlerProcessingException;

    /**
     * Called by {@link #close()} to give subclasses an opportunity to clean up
     * resources that they have created.
     * 
     * @throws QueryResultsHandlerCloseException if an error occurred.
     */
    protected abstract void cleanup() throws QueryResultsHandlerCloseException;

}
