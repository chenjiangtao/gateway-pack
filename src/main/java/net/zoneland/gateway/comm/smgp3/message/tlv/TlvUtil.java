package net.zoneland.gateway.comm.smgp3.message.tlv;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zoneland.gateway.util.TypeConvert;

public class TlvUtil {
    public static Map<Integer, Tlv> TlvAnalysis(byte[] buffer) {
        int cur = 0;
        byte[] tlv = new byte[buffer.length - cur];
        System.arraycopy(buffer, cur, tlv, 0, tlv.length);
        Map<Integer, Tlv> tmptlv = new HashMap<Integer, Tlv>();
        for (int loc = 0; loc < tlv.length;) {
            int tlv_Tag = TypeConvert.byte2short(tlv, loc + 0);
            int tlv_Length = TypeConvert.byte2short(tlv, loc + 2);
            String tlv_Value = "";
            if (tlv_Tag == TlvId.Mserviceid || tlv_Tag == TlvId.SrcTermPseudo
                || tlv_Tag == TlvId.DestTermPseudo || tlv_Tag == TlvId.ChargeTermPseudo
                || tlv_Tag == TlvId.LinkID || tlv_Tag == TlvId.MsgSrc) {
                tlv_Value = TypeConvert.getString(tlv, loc + 4, 0, tlv_Length);
                loc = loc + 4 + tlv_Length;
            } else {
                tlv_Value = String.valueOf(TypeConvert.byte2tinyint(tlv, loc + 4));
                loc = loc + 4 + 1;
            }

            tmptlv.put(tlv_Tag, new Tlv(tlv_Tag, tlv_Value));

        }
        return tmptlv;
    }

    /**
     * convert tlvs collection to byte[] 
     * @param tlvs
     * @return
     */
    public static byte[] mapToTlvs(Map<Integer, Tlv> tlvs) {
        byte[] result = new byte[0];
        if (tlvs.isEmpty()) {
            result = new byte[0];
        } else {
            Set<Entry<Integer, Tlv>> entrys = tlvs.entrySet();
            Iterator<Entry<Integer, Tlv>> itTlvs = entrys.iterator();
            while (itTlvs.hasNext()) {
                Entry<Integer, Tlv> e = itTlvs.next();
                byte[] tmp = new byte[result.length];
                System.arraycopy(result, 0, tmp, 0, tmp.length);
                result = new byte[result.length + e.getValue().TlvBuf.length];
                System.arraycopy(tmp, 0, result, 0, tmp.length);
                System.arraycopy(e.getValue().TlvBuf, 0, result, tmp.length,
                    e.getValue().TlvBuf.length);
            }
        }
        return result;
    }

    /**
     * convert tlvs collections to string  
     * @param tlvs
     * @return
     */
    public static String mapToString(Map<Integer, Tlv> tlvs) {
        StringBuffer tlvStrs = new StringBuffer(500);
        if (tlvs.get(TlvId.TP_pid) != null) {
            tlvStrs.append(",tp_pid=").append(tlvs.get(TlvId.TP_pid).Value);
        }

        if (tlvs.get(TlvId.TP_udhi) != null) {
            tlvStrs.append(",tp_udhi=").append(tlvs.get(TlvId.TP_udhi).Value);
        }
        if (tlvs.get(TlvId.LinkID) != null) {
            tlvStrs.append(",linkId=").append(tlvs.get(TlvId.LinkID).Value);
        }
        if (tlvs.get(TlvId.ChargeUserType) != null) {
            tlvStrs.append(",ChargeUserType=").append(tlvs.get(TlvId.ChargeUserType).Value);
        }
        if (tlvs.get(TlvId.ChargeTermType) != null) {
            tlvStrs.append(",ChargeTermType=").append(tlvs.get(TlvId.ChargeTermType).Value);
        }
        if (tlvs.get(TlvId.ChargeTermPseudo) != null) {
            tlvStrs.append(",ChargeTermPseudo=").append(tlvs.get(TlvId.ChargeTermPseudo).Value);
        }
        if (tlvs.get(TlvId.DestTermType) != null) {
            tlvStrs.append(",DestTermType=").append(tlvs.get(TlvId.DestTermType).Value);
        }
        if (tlvs.get(TlvId.DestTermPseudo) != null) {
            tlvStrs.append(",DestTermPseudo=").append(tlvs.get(TlvId.DestTermPseudo).Value);
        }
        if (tlvs.get(TlvId.PkTotal) != null) {
            tlvStrs.append(",PkTotal=").append(tlvs.get(TlvId.PkTotal).Value);
        }
        if (tlvs.get(TlvId.PkNumber) != null) {
            tlvStrs.append(",PkNumber=").append(tlvs.get(TlvId.PkNumber).Value);
        }
        if (tlvs.get(TlvId.SubmitMsgType) != null) {
            tlvStrs.append(",SubmitMsgType=").append(tlvs.get(TlvId.SubmitMsgType).Value);
        }
        if (tlvs.get(TlvId.SPDealResult) != null) {
            tlvStrs.append(",SPDealResult=").append(tlvs.get(TlvId.SPDealResult).Value);
        }
        if (tlvs.get(TlvId.SrcTermType) != null) {
            tlvStrs.append(",SrcTermType=").append(tlvs.get(TlvId.SrcTermType).Value);
        }
        if (tlvs.get(TlvId.SrcTermPseudo) != null) {
            tlvStrs.append(",SrcTermPseudo=").append(tlvs.get(TlvId.SrcTermPseudo).Value);
        }
        if (tlvs.get(TlvId.NodesCount) != null) {
            tlvStrs.append(",NodesCount=").append(tlvs.get(TlvId.NodesCount).Value);
        }
        if (tlvs.get(TlvId.MsgSrc) != null) {
            tlvStrs.append(",MsgSrc=").append(tlvs.get(TlvId.MsgSrc).Value);
        }
        if (tlvs.get(TlvId.SrcType) != null) {
            tlvStrs.append(",SrcType=").append(tlvs.get(TlvId.SrcType).Value);
        }
        if (tlvs.get(TlvId.Mserviceid) != null) {
            tlvStrs.append(",Mserviceid=").append(tlvs.get(TlvId.Mserviceid).Value);
        }
        return tlvStrs.toString();
    }
}
