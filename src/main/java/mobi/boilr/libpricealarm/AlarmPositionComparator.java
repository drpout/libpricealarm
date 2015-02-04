package mobi.boilr.libpricealarm;

import java.util.Comparator;

public class AlarmPositionComparator implements Comparator<Alarm> {
	@Override
	public int compare(Alarm o1, Alarm o2) {
		return o1.getPosition() - o2.getPosition();
	}
}
