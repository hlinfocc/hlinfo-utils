package net.hlinfo.opt;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
/**
 * 
 * @author 呐喊
 *
 */
public class HashUtils {
	
	/**
	 * 中国国家商用密码杂凑算法SM3<br>
	 * sm3加密，生成16进制SM3字符串
	 * @param str 源数据
	 * @return SM3字符串
	 */
	public static String sm3(String str) {
		str = (str == null) ? "" : str;
		byte[] pmdata = string2byte(str);
		SM3Digest sm3 = new SM3Digest();
		sm3.update(pmdata, 0, pmdata.length);
		byte[] hash = new byte[sm3.getDigestSize()];
		sm3.doFinal(hash, 0);
		return Hex.toHexString(hash);
	}

	/**
	 * 中国国家商用密码杂凑算法SM3 HMAC消息认证<br>
	 * sm3_hmac加密，生成16进制SM3字符串
	 * @param key 密钥
	 * @param str 源数据
	 * @return SM3字符串
	 */
	public static String sm3Hmac(String key, String str) {
		key = (key == null) ? "" : key;
		str = (str == null) ? "" : str;
		KeyParameter kp = new KeyParameter(string2byte(key));
		byte[] pmdata = string2byte(str);
		SM3Digest sm3 = new SM3Digest();
		HMac hmac = new HMac(sm3);
		hmac.init(kp);
		hmac.update(pmdata, 0, pmdata.length);
		byte[] hash = new byte[hmac.getMacSize()];
		hmac.doFinal(hash, 0);
		return Hex.toHexString(hash);
	}
	/**
	 * sha3加密，生成16进制字符串
	 * @param str 源数据
	 * @return sha3字符串
	 */
	public static String sha3(String str) {
		if (str == null) {
			str = "";
		}
		byte[] pmdata = string2byte(str);
		SHA3Digest sha3 = new SHA3Digest();
		sha3.update(pmdata, 0, pmdata.length);
		byte[] hash = new byte[sha3.getDigestSize()];
		sha3.doFinal(hash, 0);
		return Hex.toHexString(hash);
	}
	/**
	 * sha3 HMAC消息认证，生成16进制字符串
	 * @param key 密钥
	 * @param str 源数据
	 * @return sha3 HMAC字符串
	 */
	public static String sha3Hmac(String key, String str) {
		key = (key == null) ? "" : key;
		str = (str == null) ? "" : str;
		KeyParameter kp = new KeyParameter(string2byte(key));
		byte[] pmdata = string2byte(str);
		SHA3Digest sha3 = new SHA3Digest();
		HMac hmac = new HMac(sha3);
		hmac.init(kp);
		hmac.update(pmdata, 0, pmdata.length);
		byte[] hash = new byte[hmac.getMacSize()];
		hmac.doFinal(hash, 0);
		return Hex.toHexString(hash);
	}
	/**
	 * 将字符串转为字节数组byte[]
	 * @param s 字符串
	 * @return 字节数组byte[]
	 */
	public static byte[] string2byte(String s) {
		byte[] pmdata = null;
		try {
			pmdata = s.getBytes("utf-8");
			return pmdata;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pmdata;
	}
}
