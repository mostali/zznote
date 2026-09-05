package zznote_nett;

import mpu.IT;
import botcore.msgcore.EmsgClbMap;

import java.util.Map;

public class DataClbMap extends EmsgClbMap {
	public DataClbMap(int emsgId) {
		super(emsgId);
	}

	public DataClbMap(String data) {
		super(str2map(data));
	}

	public DataClbMap(Map<String, String> ctx) {
		super(ctx);
	}

	public String quest(String... val) {
		return VAL("q", val);
	}

	public String line(int num, String... val) {
		IT.isBetweenEQ(num, 1, 6);
		return VAL("l" + num, val);
	}

	public boolean isFirst() {
		return line(1) == null;
	}

	public boolean isFirstOnLine4() {
		return line(4) == null;
	}

	public int getLineNext() {
		for (int i = 1; i <= 6; i++) {
			String val = line(i);
			if (val == null || val.length() < 3) {
				return i;
			}
		}
		throw new IllegalStateException("line not found");
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public String[] toLines() {
		String[] lines = new String[6];
		for (int i = 1; i <= 6; i++) {
			lines[i - 1] = line(i);
		}
		return lines;
	}
}
