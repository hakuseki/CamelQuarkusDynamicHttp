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

package com.cssregtech.tradechannel.routes;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

//tag::DownloadESMA[]

/**
 * The class DownloadESMA
 *
 * @author mgr, (c) TradeChannel AB, 2018-03-12
 * @version 1.0
 */
public class DownloadESMA extends EndpointRouteBuilder {

    @Override
    public void configure() {


        //tag::esma.download.timer[]
        from(timer("FIRDSDownloader").repeatCount(1))
//                .routeId("FIRDS Download")
//                .autoStartup("{{esma.download.startup}}")
.setHeader("CamelHttpMethod", constant("GET"))
                .toD("https:registers.esma.europa.eu/solr/esma_registers_firds_files/select?q=*&fq=publication_date" +
                             ":%5B${date:now-72h:yyyy-MM-dd}T00:00:00Z%20TO%20${date:now-24h:yyyy-MM-dd}T23:59:59Z%5D&wt=xml&indent=false&start=0&rows=100")
                .log("${body}")
                .end();
        //end::esma.download.timer[]
    }
}
//end::DownloadESMA[]
