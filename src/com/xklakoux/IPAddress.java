package com.xklakoux;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

/** Class created to ease a bit operations on IP addresses and mask */
public class IPAddress {

	String address = new String();

	IPAddress(String add) {
		address = add;
	}

	/** Get octet from ip address */
	public int getOct(int which) {
		return Integer.valueOf(address.split("\\.")[which - 1]);
	}

	/** Get octet from ip address */
	public static int getOct(String address, int which) {
		return Integer.valueOf(address.split("\\.")[which - 1]);
	}

	/** Get octet on ip address */
	public void setOct(int which, int withWhat) {
		String[] add = address.split("\\.");
		add[which - 1] = String.valueOf(withWhat);
		address = add[0] + "." + add[1] + "." + add[2] + "." + add[3];
	}

	/** Get octet on ip address */
	public static String setOct(String address, int which, int withWhat) {
		String[] add = address.split("\\.");
		add[which - 1] = String.valueOf(withWhat);
		return address = add[0] + "." + add[1] + "." + add[2] + "." + add[3];
	}

	/** Get broadcast address from host or network address */
	public static String getBroadcast(String ip, String mask) {
		String[] ipeek = ip.split("\\.");
		String[] msk = mask.split("\\.");
		String[] brdcst = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {
			brdcst[i] = String.valueOf(Integer.valueOf(ipeek[i])
					| (~(Integer.valueOf(msk[i])) & 255));
		}
		return brdcst[0] + "." + brdcst[1] + "." + brdcst[2] + "." + brdcst[3];
	}

	/**
	 * Get broadcast address from host or network address overloaded method for
	 * cidr mask
	 */
	public static String getBroadcast(String ip, int mask) {
		String[] ipeek = ip.split("\\.");
		String[] msk = getDDMask(mask).split("\\.");
		String[] brdcst = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {
			brdcst[i] = String.valueOf(Integer.valueOf(ipeek[i])
					| (~(Integer.valueOf(msk[i])) & 255));
		}
		return brdcst[0] + "." + brdcst[1] + "." + brdcst[2] + "." + brdcst[3];
	}

	public static String getWildcard(String mask) {
		String[] msk = mask.split("\\.");
		String[] wld = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {
			wld[i] = String.valueOf(255 - Integer.valueOf(Integer
					.valueOf(msk[i])));
		}
		return wld[0] + "." + wld[1] + "." + wld[2] + "." + wld[3];
	}

	public static String getWildcard(int mask) {
		String[] msk = getDDMask(mask).split("\\.");
		String[] wld = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {
			wld[i] = String.valueOf(255 - Integer.valueOf(Integer
					.valueOf(msk[i])));
		}
		return wld[0] + "." + wld[1] + "." + wld[2] + "." + wld[3];
	}

	public static String getNetwork(String ip, String mask) {
		String[] ipeek = ip.split("\\.");
		String[] msk = mask.split("\\.");
		String[] ntwrk = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {

			ntwrk[i] = String.valueOf(Integer.valueOf(Integer.valueOf(ipeek[i])
					& Integer.valueOf(msk[i])));
		}

		return ntwrk[0] + "." + ntwrk[1] + "." + ntwrk[2] + "." + ntwrk[3];
	}

	/** Get network address */
	public static String getNetwork(String ip, int mask) {
		String[] ipeek = ip.split("\\.");
		String[] msk = getDDMask(mask).split("\\.");
		String[] ntwrk = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {

			ntwrk[i] = String.valueOf(Integer.valueOf(Integer.valueOf(ipeek[i])
					& Integer.valueOf(msk[i])));
		}

		return ntwrk[0] + "." + ntwrk[1] + "." + ntwrk[2] + "." + ntwrk[3];
	}

	/** Get number of hosts for from decimal dot mask */
	public static int getUsableHostsCount(String mask) {
		int sum = 1;
		String[] msk = mask.split("\\.");

		for (int i = 0; i < 4; i++) {

			sum *= Integer.valueOf(msk[i]) - 256;
		}
		return sum - 2;
	}

	/** Get number of hosts for from cidr mask */
	public static int getUsableHostsCount(int mask) {
		int sum = 1;
		String[] msk = getDDMask(mask).split("\\.");

		for (int i = 0; i < 4; i++) {

			sum *= Math.abs(Integer.valueOf(msk[i]) - 256);
		}
		return sum - 2;
	}

	/** Get first usable address in subnetwork */
	public static String getFirstAddress(String networkIP) {
		return setOct(networkIP, 4, getOct(networkIP, 4) + 1);
	}

	/** Get last usable address in subnetwork */
	public static String getLastAddress(String networkIP, String mask) {
		return setOct(getBroadcast(networkIP, mask), 4,
				getOct(getBroadcast(networkIP, mask), 4) - 1);
	}

	/**
	 * Get last usable address in subnetwork overloaded method for cidr mask
	 */
	public static String getLastAddress(String networkIP, int mask) {
		return setOct(getBroadcast(networkIP, mask), 4,
				getOct(getBroadcast(networkIP, mask), 4) - 1);
	}

	/** Returns true if given network contains given host addresss */
	public static boolean containsAddress(String networkIP, String mask,
			String hostIP) {
		if (networkIP == getNetwork(hostIP, mask)) {
			return true;
		}
		return false;
	}

	/**
	 * Which mask do you have to use to contain that much usable network
	 * addresses
	 */
	public static int hostsToMask(int howMany, int motherMask) {

		for (int i = 30; i >= motherMask; i--) {
			if (getUsableHostsCount(getDDMask(i)) >= howMany) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Special method that checks if there's enough space for number of hosts in
	 * subnets you need
	 */
	public static boolean fits(int[] howMany, int[] amount, int motherMask) {
		long sum = 0;
		for (int k = 0; k < howMany.length; k++) {
			for (int j = 0; j < amount[k]; j++) {
				for (int i = 30; i > 8; i--) {
					if (getUsableHostsCount(getDDMask(i)) >= howMany[k]) {
						sum += getUsableHostsCount(getDDMask(i));
						Log.d("aa", String.valueOf(sum));
						break;
					}
				}
			}
		}
		if (sum <= getUsableHostsCount(getDDMask(motherMask))) {
			return true;
		} else {
			return false;
		}
	}

	/** Get decimal dot mask from cidr mask */
	public static String getDDMask(int cidr) {
		String[] msk = { "0", "0", "0", "0" };

		for (int i = 0; i < 4; i++) {
			if (cidr >= 8) {
				msk[i] = "255";
				cidr -= 8;

			} else {
				msk[i] = String.valueOf(Integer
						.valueOf((0xFF << 8 - cidr) & 0xFF));
				break;
			}
		}
		return msk[0] + "." + msk[1] + "." + msk[2] + "." + msk[3];
	}

	/** Get cidr mask from decimal dot address */
	public static int getCidrMask(String mask) {

		int cidr = 0;
		String[] msk = mask.split("\\.");

		for (int i = 0; i < 4; i++) {
			if (Integer.valueOf(msk[i]) == 255) {
				cidr += 8;

			} else {
				char[] a = Integer.toBinaryString(
						Integer.valueOf(msk[i]) & 0xFF).toCharArray();
				for (int j = 0; j < a.length; j++) {
					if (a[j] == '1')
						cidr++;
				}
				break;
			}
		}
		return cidr;
	}

	/**
	 * Filter for TextEdit type that checks if IP Address you're typed in is
	 * correct
	 */
	public static boolean isCorrectIP(String ip) {
		final Pattern IP_ADDRESS = Pattern
				.compile("((22[0-3]|2[0-1][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
						+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
						+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
						+ "|[1-9][0-9]|[0-9]))");
		Matcher ipmatcher = IP_ADDRESS.matcher(ip);
		if (ipmatcher.matches())
			return true;
		return false;
	}

	/**
	 * Filter for TextEdit type that checks if mask Address you're typed in is
	 * correct
	 */
	public static boolean isCorrectMask(String mask) {
		final Pattern IP_MASK = Pattern
				.compile("(255|254|252|248|240|224|192|128)\\.(255|254|252|248|240|224|192|128|0)"
						+ "\\.(255|254|252|248|240|224|192|128|0)\\.(255|254|252|248|240|224|192|128|0)");
		Matcher matcher = IP_MASK.matcher(mask.toString());
		if (matcher.matches())
			return true;
		return false;
	}

	/**
	 * Filter for TextEdit type that checks if IP Address you're typing in is
	 * correct
	 */
	public static InputFilter[] getIpFilter() {
		InputFilter[] ipfilter = new InputFilter[1];
		ipfilter[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		return ipfilter;
	}

	/** Filter for TextEdit type that checks if mask you're typing in is correct */
	public static InputFilter[] getMaskFilter() {
		InputFilter[] maskfilter = new InputFilter[1];
		maskfilter[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
							if (!splits[i]
									.matches("\\d{1,2}|255|254|252|248|240|224|192|128")) {

								return "";

							}
							if (i != 0
									&& Integer.valueOf(splits[i - 1]) < Integer
											.valueOf(splits[i])) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		return maskfilter;
	}

};
