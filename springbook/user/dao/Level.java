package springbook.user.dao;

public enum Level {
	//클래스와 비교했을 때 Enum이 가지는 장점이? 타입체크 가능.
	//http://limkydev.tistory.com/50
	GOLD(3,null), SILVER(2,GOLD), BASIC(1,SILVER);

	private final int value;
	private final Level next;
	
	Level(int value, Level next) {
		this.value = value;
		this.next = next;
	}

	public int intValue() {
		return value;
	}

	public Level nextLevel() {
		return this.next;
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
