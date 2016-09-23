import java.io.File;

public class Test {
	public static void main(String[] args) {
		File file = new File("a");
		Object object = file;
		if(object instanceof File) {
			System.out.println("instance of file");
		} else {
			System.out.println("not instance of file");
		}
	}
}
