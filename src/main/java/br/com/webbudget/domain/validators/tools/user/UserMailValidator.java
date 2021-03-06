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
 * {@link BusinessValidator} for the user e-mail validation logic
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/08/2018
 */
@Dependent
public class UserMailValidator implements UserSavingValidator, UserUpdatingValidator {

    @Inject
    private UserRepository userRepository;

    /**
     * {@inheritDoc }
     * 
     * @param value 
     */
    @Override
    public void validate(User value) {
        
        final Optional<User> userOptional = this.userRepository
                .findOptionalByEmail(value.getEmail());
        
        if (userOptional.isPresent()) {
            
            final User found = userOptional.get();
            
            if (!found.getUsername().equals(value.getUsername())) {
                throw BusinessLogicException.create("error.user.email-duplicated");
            }
        }
    }
}