/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.entity.logbook;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representacao das ocorrencias de um registro do logbook
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.1, 09/05/2016
 */
@Entity
@ToString(callSuper = true)
@Table(name = "occurrences")
@EqualsAndHashCode(callSuper = true)
public class Occurrence extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_cost_center")
    @NotNull(message = "{occurrence.cost-center}")
    private CostCenter costCenter;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_movement_class")
    @NotNull(message = "{occurrence.movement-class}")
    private MovementClass movementClass;
}
