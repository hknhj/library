package library;

import java.util.Scanner;

public class BookTest {
	Scanner scanner = new Scanner(System.in);
	BookDAO bookdao;

	public void run() {
		while(true) {
			bookdao=new BookDAO();
			Banner.banner();
			
			String option = scanner.nextLine();
			
			if(option.equals("1")) {
				BookDTO bookdto = bookdao.BookSearch();
				if(bookdto==null) {
					System.out.println("해당 책이 없습니다.\n");
					continue;
				}
				System.out.println("\n[책 정보]");
				System.out.println(bookdto.toString()+"\n");
			} else if(option.equals("2")) {
				System.out.println("\n준비가 아직 안됐습니다.\n");
			} else if(option.equals("3")) {
				System.out.println("\n프로그램을 종료합니다.");
				break;
			} else {
				System.out.println("\n잘못된 옵션입니다.\n");
			}
		}
	}
}
