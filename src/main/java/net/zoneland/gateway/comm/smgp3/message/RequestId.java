/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.smgp3.message;

/**
 * 
 * @author liuzhenxing
 * @version $Id: RequestId.java, v 0.1 2012-5-21 上午9:48:58 liuzhenxing Exp $
 */
public class RequestId {

    public final static int LOGIN                 = 0x00000001;
    public final static int LOGIN_RESP            = 0x80000001;
    public final static int SUBMIT                = 0x00000002;
    public final static int SUBMIT_RESP           = 0x80000002;
    public final static int DELIVER               = 0x00000003;
    public final static int DELIVER_RESP          = 0x80000003;
    public final static int ACTIVETEST            = 0x00000004;
    public final static int ACTIVETEST_RESP       = 0x80000004;
    public final static int FORWARD               = 0x00000005;
    public final static int FORWARD_RESP          = 0x80000005;
    public final static int EXIT                  = 0x00000006;
    public final static int EXIT_RESP             = 0x80000006;
    public final static int QUERY                 = 0x00000007;
    public final static int QUERY_RESP            = 0x80000007;
    public final static int QUERY_TE_ROUTE        = 0x00000008;
    public final static int QUERY_TE_ROUTE_RESP   = 0x80000008;
    public final static int QUERY_SP_ROUTE        = 0x00000009;
    public final static int QUERY_SP_ROUTE_RESP   = 0x80000009;
    public final static int PAYMENT_REQUEST       = 0x0000000A;
    public final static int PAYMENT_REQUEST_RESP  = 0x8000000A;
    public final static int PAYMENT_AFFIRM        = 0x0000000B;
    public final static int PAYMENT_AFFIRM_RESP   = 0x8000000B;
    public final static int QUERY_USERSTATE       = 0x0000000C;
    public final static int QUERY_USERSTATE_RESP  = 0x8000000C;
    public final static int GET_ALL_TE_ROUTE      = 0x0000000D;
    public final static int GET_ALL_TE_ROUTE_RESP = 0x8000000D;
    public final static int GET_ALL_SP_ROUTE      = 0x0000000E;
    public final static int GET_ALL_SP_ROUTE_RESP = 0x8000000E;

}
