/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.smgp.message;

/**
 * 
 * @author liuzhenxing
 * @version $Id: CommandId.java, v 0.1 2012-5-21 上午9:48:58 liuzhenxing Exp $
 */
public class RequestId {
    
    public static int Login =          0x00000001;
    public static int Login_Resp =     0x80000001;
    public static int Submit =         0x00000002;
    public static int Submit_Resp=     0x80000002;
    public static int Deliver =        0x00000003;
    public static int Deliver_Resp =   0x80000003;
    public static int ActiveTest =     0x00000004;
    public static int ActiveTest_Resp= 0x80000004;
    public static int Exit =           0x00000006;
    public static int Exit_Resp =      0x80000006;

}
