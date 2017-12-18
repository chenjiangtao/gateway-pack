package net.zoneland.gateway.comm.smgp3.message.tlv;

import net.zoneland.gateway.util.TypeConvert;

public class Tlv {
	public int Tag;
	public int Length;
	public String Value;
	public byte[] TlvBuf;

	public Tlv(int tag, String value) {
		this.Tag = tag;
		this.Length = value.length();
		this.Value = value;
    
		if (tag == TlvId.Mserviceid || tag == TlvId.MsgSrc
				|| tag == TlvId.SrcTermPseudo || tag == TlvId.DestTermPseudo
				|| tag == TlvId.ChargeTermPseudo || tag == TlvId.LinkID ) {
		    
			this.TlvBuf = new byte[4 + this.Length];
			TypeConvert.int2byte2(Tag, TlvBuf, 0);
			TypeConvert.int2byte2(this.Length, TlvBuf, 2);
			System.arraycopy(this.Value.getBytes(), 0, TlvBuf, 4, this.Length);

		} else {
			this.TlvBuf = new byte[4 + 1];
			TypeConvert.int2byte2(Tag, TlvBuf, 0);
			TypeConvert.int2byte2(1, TlvBuf, 2);
			TypeConvert.int2byte3(Integer.parseInt(value), TlvBuf, 4);


		}

	}
}
