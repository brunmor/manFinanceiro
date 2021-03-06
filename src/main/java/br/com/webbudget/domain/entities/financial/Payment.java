/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.infrastructure.utils.RandomCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL;
import static br.com.webbudget.infrastructure.utils.DefaultSchemes.FINANCIAL_AUDIT;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments", schema = FINANCIAL)
@AuditTable(value = "payments", schema = FINANCIAL_AUDIT)
public class Payment extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "{payment.payment-date}")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    @Getter
    @Setter
    @Column(name = "discount", length = 8)
    private BigDecimal discount;
    @Getter
    @Setter
    @Enumerated
    @NotNull(message = "{payment.payment-method}")
    @Column(name = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card")
    private Card card;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_wallet")
    private Wallet wallet;

    /**
     *
     */
    public Payment() {
        this.code = RandomCode.alphanumeric(6);
        this.paymentDate = LocalDate.now();
        this.paymentMethodType = PaymentMethodType.IN_CASH;
    }

    /**
     * Com este metodo construimos a data de vencimendo do cartao caso seja um
     * pagamento via cartao de credito para entao setar no movimento a fim de
     * que ele esteja no vencimento do cartao
     *
     * @param period o periodo que esperamos que a data compreenda
     * @return a data de vencimento do cartao
     */
    public LocalDate getCreditCardInvoiceDueDate(FinancialPeriod period) {

        if (this.card == null) {
            throw new BusinessLogicException("movement.execute.payment-not-credit-card");
        }

        final int expiration = this.card.getExpirationDay();

        if (expiration != 0) {
            return period.getEnd().withDayOfMonth(expiration).plusMonths(1);
        } else {
            return period.getEnd();
        }
    }

    /**
     * @return se este pagamento eh em dinheiro
     */
    public boolean isPaidInCash() {
        return this.paymentMethodType == PaymentMethodType.IN_CASH;
    }

    /**
     * @return se este pagamento eh em credito
     */
    public boolean isPaidOnCredit() {
        return this.paymentMethodType == PaymentMethodType.CREDIT_CARD;
    }

    /**
     * @return se este pagamento eh em debito
     */
    public boolean isPaidOnDebit() {
        return this.paymentMethodType == PaymentMethodType.DEBIT_CARD;
    }

    /**
     * Valida se este pagamento eh valido
     */
    public void validatePaymentMethod() {
        if (this.isPaidInCash() && this.wallet == null) {
            throw new BusinessLogicException("error.payment.no-wallet");
        } else if ((this.isPaidOnCredit() || this.isPaidOnDebit()) && this.card == null) {
            throw new BusinessLogicException("error.payment.no-card");
        }
    }

    /**
     * Valida se o desconto aqui inserido e menor que o valor do movimento 
     * que estamos pagando por este pagamento
     *
     * @param value o valor do movimento para comparacao
     */
    public void validateDiscount(BigDecimal value) {
        if (this.discount != null && this.discount.compareTo(value) >= 0) {
            throw new BusinessLogicException("error.payment.invalid-discount");
        }
    }

    /**
     * @return se temos ou nao um desconto para este pagamento
     */
    boolean hasDiscount() {
        return this.discount != null && this.discount != BigDecimal.ZERO;
    }
}
