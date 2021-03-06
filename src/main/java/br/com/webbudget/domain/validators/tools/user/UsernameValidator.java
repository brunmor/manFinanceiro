/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.validators.tools.user;

import br.com.webbudget.domain.entities.tools.User;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import br.com.webbudget.domain.validators.BusinessValidator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

/**
 * {@link BusinessValidator} for the username validation logic
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/08/2018
 */
@Dependent
public class UsernameValidator implements UserSavingValidator {

    @Inject
    private UserRepository userRepository;

    /**
     * {@inheritDoc }
     *
     * @param value
     */
    @Override
    public void validate(User value) {

        final Optional<User> usernameOptional = this.userRepository
                .findOptionalByUsername(value.getUsername());

        if (usernameOptional.isPresent()) {
            throw BusinessLogicException.create("error.user.username-duplicated");
        }
    }
}