
public enum Level {
	//클래스와 비교했을 때 Enum이 가지는 장점이? 타입체크 가능.
	//http://limkydev.tistory.com/50
	BASIC(1), SILVER(2), GOLD(3);

	private final int value;

	Level(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public static Level valueOf(int value) {
		switch (value) {
		case 1:
			return BASIC;
		case 2:
			return SILVER;
		case 3:
			return GOLD;
		default:
			throw new AssertionError("Unknown value : " + value);
		}
	}
}
