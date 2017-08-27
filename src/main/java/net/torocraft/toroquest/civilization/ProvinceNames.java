package net.torocraft.toroquest.civilization;

import java.util.Random;

public class ProvinceNames {

	private static final String[] PARTS1 = { "a", "e", "i", "o", "u", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	private static final String[] PARTS2 = { "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z", "br", "cr", "dr", "fr", "gr", "kr", "pr", "qr", "sr", "tr", "vr", "wr", "yr", "zr", "str",
			"bl", "cl", "fl", "gl", "kl", "pl", "sl", "vl", "yl", "zl", "ch", "kh", "ph", "sh", "yh", "zh" };
	private static final String[] PARTS3 = { "a", "e", "i", "o", "u", "ae", "ai", "ao", "au", "aa", "ee", "ea", "ei", "eo", "eu", "ia", "ie", "io", "iu", "oa", "oe", "oi", "oo", "ou", "ua", "ue", "ui", "uo", "uu", "a", "e", "i", "o", "u",
			"a", "e", "i", "o", "u", "a", "e", "i", "o", "u", "a", "e", "i", "o", "u", "a", "e", "i", "o", "u" };
	private static final String[] PARTS4 = { "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z", "br", "cr", "dr", "fr", "gr", "kr", "pr", "tr", "vr", "wr", "zr", "st", "bl", "cl", "fl",
			"gl", "kl", "pl", "sl", "vl", "zl", "ch", "kh", "ph", "sh", "zh" };
	private static final String[] PARTS5 = { "c", "d", "f", "h", "k", "l", "m", "n", "p", "r", "s", "t", "x", "y", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	private static final String[] PARTS6 = { "aco", "ada", "adena", "ago", "agos", "aka", "ale", "alo", "am", "anbu", "ance", "and", "ando", "ane", "ans", "anta", "arc", "ard", "ares", "ario", "ark", "aso", "athe", "eah", "edo", "ego",
			"eigh", "eim", "eka", "eles", "eley", "ence", "ens", "ento", "erton", "ery", "esa", "ester", "ey", "ia", "ico", "ido", "ila", "ille", "in", "inas", "ine", "ing", "irie", "ison", "ita", "ock", "odon", "oit", "ok", "olis", "olk",
			"oln", "ona", "oni", "onio", "ont", "ora", "ord", "ore", "oria", "ork", "osa", "ose", "ouis", "ouver", "ul", "urg", "urgh", "ury" };
	private static final String[] PARTS7 = { "bert", "bridge", "burg", "burgh", "burn", "bury", "bus", "by", "caster", "cester", "chester", "dale", "dence", "diff", "ding", "don", "fast", "field", "ford", "gan", "gas", "gate", "gend",
			"ginia", "gow", "ham", "hull", "land", "las", "ledo", "lens", "ling", "mery", "mond", "mont", "more", "mouth", "nard", "phia", "phis", "polis", "pool", "port", "pus", "ridge", "rith", "ron", "rora", "ross", "rough", "sa",
			"sall", "sas", "sea", "set", "sey", "shire", "son", "stead", "stin", "ta", "tin", "tol", "ton", "vale", "ver", "ville", "vine", "ving", "well", "wood" };

	public static String random(Random rand) {
		int i = rand.nextInt(10);
		StringBuilder buf = new StringBuilder();
		if (i < 3) {
			buf.append(choose(rand, PARTS1));
			buf.append(choose(rand, PARTS2));
			buf.append(choose(rand, PARTS3));
			buf.append(choose(rand, PARTS5));
			buf.append(choose(rand, PARTS7));
		} else if (i < 5) {
			buf.append(choose(rand, PARTS3));
			buf.append(choose(rand, PARTS4));
			buf.append(choose(rand, PARTS3));
			buf.append(choose(rand, PARTS5));
			buf.append(choose(rand, PARTS7));
		} else if (i < 8) {
			buf.append(choose(rand, PARTS1));
			buf.append(choose(rand, PARTS2));
			buf.append(choose(rand, PARTS6));
		} else {
			buf.append(choose(rand, PARTS1));
			buf.append(choose(rand, PARTS2));
			buf.append(choose(rand, PARTS3));
			buf.append(choose(rand, PARTS4));
			buf.append(choose(rand, PARTS6));
		}

		String name = buf.toString();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private static String choose(Random rand, String[] parts) {
		return parts[rand.nextInt(parts.length)];
	}

}