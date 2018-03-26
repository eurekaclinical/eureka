package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.etl.entity.I2B2DestinationConceptSpecEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.I2B2DestinationModifierSpecEntity;
import edu.emory.cci.aiw.i2b2etl.dest.config.Concepts;
import edu.emory.cci.aiw.i2b2etl.dest.config.FolderSpec;
import edu.emory.cci.aiw.i2b2etl.dest.config.ModifierSpec;
import edu.emory.cci.aiw.i2b2etl.dest.metadata.ValueTypeCode;
import java.util.List;

/**
 * Database-based i2b2 loader concepts section.
 * 
 * @author Andrew Post
 */
class I2b2Concepts implements Concepts {
	private final FolderSpec[] folderSpecs;

	I2b2Concepts(List<I2B2DestinationConceptSpecEntity> conceptSpecs) {
		this.folderSpecs = new FolderSpec[conceptSpecs.size()];
		for (int i = 0, n = conceptSpecs.size(); i < n; i++) {
			I2B2DestinationConceptSpecEntity conceptSpec = conceptSpecs.get(i);
			List<I2B2DestinationModifierSpecEntity> modifierSpecs = conceptSpec.getModifierSpecs();
			ModifierSpec[] ms = new ModifierSpec[modifierSpecs.size()];
			for (int j = 0; j < ms.length; j++) {
				I2B2DestinationModifierSpecEntity mse = modifierSpecs.get(j);
				ms[j] = new ModifierSpec(mse.getName(), mse.getCodePrefix(), mse.getProperty(), mse.getVal());
			}
			this.folderSpecs[i] = new FolderSpec(
					null, 
					new String[]{conceptSpec.getProposition()}, 
					conceptSpec.getProperty(), 
					null, 
					ValueTypeCode.valueOf(conceptSpec.getValueTypeCode().getName()), 
					conceptSpec.isAlreadyLoaded(),
					ms);
		}
	}

	@Override
	public FolderSpec[] getFolderSpecs() {
		return this.folderSpecs;
	}
	
}
