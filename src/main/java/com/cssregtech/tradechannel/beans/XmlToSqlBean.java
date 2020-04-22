/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * Copyright (c)  2000-2018, TradeChannel AB. All rights reserved.
 * Any right to utilize the System under this Agreement shall be subject to the terms and condition of the
 * License Agreement between Customer "X" and TradeChannel AB.
 *
 * TradeseC contains third party software which includes software owned or licensed by a third party and
 * sub licensed to the Customer by TradeChannel AB in accordance with the License Agreement.
 *
 * TradeChannel AB owns the rights to the software product TradeseC.
 *
 * TradeChannel AB grants a right to the Customer and the Customer accepts a non-exclusive,
 * non-transferrable license to use TradeseC and Third Party Software, in accordance with the conditions
 * specified in this License Agreement.
 *
 * The Customer may not use TradeseC or the Third Party Software for time-sharing, rental,
 * service bureau use, or similar use. The Customer is responsible for that all use of TradeseC
 * and the Third Party Software is in accordance with the License Agreement.
 *
 * The Customer may not transfer, sell, sublicense, let, lend or in any other way permit any person or entity
 * other than the Customer, avail himself, herself or itself of or otherwise any rights to TradeseC or the
 * Third Party Software, either directly or indirectly.
 *
 * The Customer may not use, copy, modify or in any other way transfer or use TradeseC or the
 * Third Party Software wholly or partially, nor allow another person or entity to do so, in any way other than
 * what is expressly permitted according to the License Agreement. Nor, consequently, may the Customer,
 * independently or through an agent, reverse engineer, decompile or disassemble TradeseC, the Third Party Software
 * or any accessories that may be related to it.
 *
 * The Customer acknowledges TradeseC <i>(including but not limited to any copyrights, trademarks,
 * documentation, enhancements or other intellectual property or proprietary rights relating to it)</i>
 * and Third Party Software is the proprietary material of the Supplier and respectively Third Party.
 *
 * The Third Party Software are protected by copyright law.
 *
 * The Customer shall not remove, erase or hide from view any information about a patent, copyright,
 * trademark, confidentiality notice, mark or legend appearing on any of TradeseC or Third Party Software,
 * any medium by which they are made available or any form of output produced by them.
 *
 * The License Agreement will only grant the Customer the right to use TradeseC and Third Party Software
 * under the terms of the License Agreement.
 */

package com.cssregtech.tradechannel.beans;

import org.apache.camel.language.xpath.XPath;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;

//tag::XmlToSqlBean[]

/**
 * The class XmlToSqlBean
 *
 * @author mgr, (c) TradeChannel AB, 2018-03-12
 * @version 1.0
 */
public class XmlToSqlBean {

    /**
     * To sql string.
     *
     * @param isin     the isin
     * @param currency the currency
     * @param fullName the full name
     * @return the string
     */
    public String toSql(@XPath("//Id") final String isin,
                        @XPath("//NtnlCcy") final String currency,
                        @XPath("//FullNm") final String fullName,
                        @XPath("//TradgVnRltdAttrbts/Id") final String venue,
                        @XPath("//ClssfctnTp") final String classification,
                        @XPath("//TradgVnRltdAttrbts/TermntnDt") final String terminationDate) {

        String insertStr = null;
        final String[] fullNameArray = fullName.split(" ");
        String priceCurrency = null;

        String maturityDate = "";

        for (final String tmpCurr : fullNameArray) {
            try {
                final Currency tmpCurrency = Currency.getInstance(tmpCurr);
                if (!tmpCurrency
                        .getCurrencyCode()
                        .equalsIgnoreCase(currency)) {
                    priceCurrency = tmpCurrency.getCurrencyCode();
                    break;
                }
            } catch (final Exception ignored) {
            }
        }
        for (final String tmpDate : fullNameArray) {
            try {
                final LocalDate matDateComputed = LocalDate.parse(tmpDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                maturityDate = matDateComputed.toString();
                break;
            } catch (final Exception ignored) {
            }

        }
        LocalDateTime termDate = LocalDateTime.of(2199, 12, 31, 23, 59, 59, 0);
        try {
            termDate = LocalDateTime.parse(terminationDate);
        } catch (final DateTimeParseException ignored) {
        }

        if (StringUtils.containsIgnoreCase(fullName, "FOREIGN_EXCHANGE")) {
            if (StringUtils.isNotEmpty(currency) &&
                    StringUtils.isNotEmpty(isin) &&
                    StringUtils.isNotEmpty(priceCurrency) &&
                    StringUtils.isNotEmpty(maturityDate) &&
                    termDate.isAfter(LocalDateTime.now())) {
                insertStr = MessageFormat.format(
                        "insert into firds_data (isin, fullname, currency, maturity_date, venue, classification,price_currency, termination_date) " +
                                "values(''{0}'', ''{3}'', ''{1}'', ''{2}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'')",
                        isin,
                        currency,
                        maturityDate,
                        fullName,
                        venue,
                        classification,
                        priceCurrency,
                        termDate.toString());
            }
        }
        return insertStr;
    }
}
//end::XmlToSqlBean[]
