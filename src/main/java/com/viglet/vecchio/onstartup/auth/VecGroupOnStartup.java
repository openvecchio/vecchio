/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.vecchio.onstartup.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.vecchio.persistence.model.auth.VecGroup;
import com.viglet.vecchio.persistence.repository.auth.VecGroupRepository;

@Component
public class VecGroupOnStartup {
	@Autowired
	private VecGroupRepository vecGroupRepository;

	public void createDefaultRows() {

		if (vecGroupRepository.findAll().isEmpty()) {

			VecGroup vecGroup = new VecGroup();

			vecGroup.setName("Administrator");
			vecGroup.setDescription("Administrator Group");
			vecGroupRepository.save(vecGroup);
		}

	}
}
